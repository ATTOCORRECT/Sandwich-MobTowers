package net.sandwich.mobtowers.block.custom;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import net.sandwich.mobtowers.block.custom.states.SeepingActivation;
import net.sandwich.mobtowers.mobregion.MobRegion;

public class SeepingGrimstone extends Block {

	public static final EnumProperty<SeepingActivation> ACTIVATION = EnumProperty.create("activation", SeepingActivation.class);
	public static final BooleanProperty TICKED = BooleanProperty.create("ticked");

	public static SeepingActivation getActivation(BlockState state) {
		return (SeepingActivation)state.getValue(ACTIVATION);
	}

	public static int getLight(BlockState state) {
		SeepingActivation activation = (SeepingActivation)state.getValue(ACTIVATION);
		if (activation == SeepingActivation.ACTIVE) {
			return 6;
		}
		if (activation == SeepingActivation.MID) {
			return 3;
		}
		return 0;
	}

	public SeepingGrimstone(Properties properties) {
		super(properties);
		this.registerDefaultState((BlockState)this.defaultBlockState().setValue(ACTIVATION, SeepingActivation.ACTIVE));
		this.registerDefaultState((BlockState)this.defaultBlockState().setValue(TICKED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVATION);
		builder.add(TICKED);
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		level.scheduleTick(pos, this, 60 + level.getRandom().nextInt(40));
		state = (BlockState)state.setValue(TICKED, true);
		level.setBlock(pos, state, 3);
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state) {
		return !(Boolean)state.getValue(TICKED);
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		boolean hasTicked = (Boolean)state.getValue(TICKED);
		if (!hasTicked) {
			level.scheduleTick(pos, this, 1);
			state = (BlockState)state.setValue(TICKED, true);
			level.setBlock(pos, state, 3);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		updateActivation(state, level, pos);
		
		level.scheduleTick(pos, this, 60 + level.getRandom().nextInt(40));
	}

	private void updateActivation(BlockState state, ServerLevel level, BlockPos pos){
		boolean mobRegionEnabled = MobRegion.isMobRegionEnabled(pos, level);
		SeepingActivation activation = (SeepingActivation)state.getValue(ACTIVATION);
		
		if (activation == SeepingActivation.ACTIVE && mobRegionEnabled) return;
		if (activation == SeepingActivation.DORMANT && !mobRegionEnabled) return;
		
		if (activation == SeepingActivation.ACTIVE && !mobRegionEnabled) {
			state = (BlockState)state.setValue(ACTIVATION, SeepingActivation.MID);
			level.setBlock(pos, state, 3);
		}

		if (activation == SeepingActivation.MID && !mobRegionEnabled) {
			state = (BlockState)state.setValue(ACTIVATION, SeepingActivation.DORMANT);
			level.setBlock(pos, state, 3);
		}

		if (activation == SeepingActivation.DORMANT && mobRegionEnabled) {
			state = (BlockState)state.setValue(ACTIVATION, SeepingActivation.MID);
			level.setBlock(pos, state, 3);
		}

		if (activation == SeepingActivation.MID && mobRegionEnabled) {
			state = (BlockState)state.setValue(ACTIVATION, SeepingActivation.ACTIVE);
			level.setBlock(pos, state, 3);
		}
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(ACTIVATION, SeepingActivation.DORMANT);
	}
}
