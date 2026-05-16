package net.sandwich.mobtowers.block.custom;

import javax.annotation.Nullable;


import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.sandwich.mobtowers.saveddata.MobRegionSavedData;
import net.sandwich.mobtowers.voronoi.CellCenter;
import net.sandwich.mobtowers.voronoi.Voronoi;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class MonsterFlame extends Block{

	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public MonsterFlame(Properties properties) {
		super(properties);
		this.registerDefaultState((BlockState)this.defaultBlockState().setValue(LIT, true));
	}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // this is where the properties are actually added to the state
        builder.add(LIT);
    }


	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			this.toggle(state, level, pos, (Player)null);
			return InteractionResult.CONSUME;
		}
	}

	public void toggle(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
		if (level instanceof ServerLevel serverLevel) {

			int x = pos.getX();
			int z = pos.getZ();

			boolean isLit = (Boolean)state.getValue(LIT);
			
			MobRegion.setMobRegionEnabled(isLit, pos, serverLevel);

			Component message = Component.literal("Yup! I am lit: " + isLit + " at pos " + x + ", " + z);
			serverLevel.players().forEach(p -> p.sendSystemMessage(message));

		}
		state = (BlockState)state.cycle(LIT);
		level.setBlock(pos, state, 3);
		level.gameEvent(player, (Boolean)state.getValue(LIT) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
	}



	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (level instanceof ServerLevel serverLevel) {
			
		}
	}
}
