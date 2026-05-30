package net.sandwich.mobtowers.block.custom;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity.Server;
import net.sandwich.mobtowers.block.entity.ModBlockEntities;
import net.sandwich.mobtowers.block.entity.MonsterFlame.MonsterFlameAnimationState;
import net.sandwich.mobtowers.block.entity.MonsterFlame.MonsterFlameEntity;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.sandwich.mobtowers.sound.ModSounds;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MonsterFlame extends BaseEntityBlock {

	public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);
	public static final MapCodec<MonsterFlame> CODEC = simpleCodec(MonsterFlame::new);

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new MonsterFlameEntity(blockPos, blockState);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

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

	// https://docs.neoforged.net/docs/blockentities/
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModBlockEntities.MONSTER_FLAME_BE.get(), level.isClientSide ? MonsterFlameEntity::clientTick : MonsterFlameEntity::serverTick);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof MonsterFlameEntity monsterFlameEntity) {
			if (monsterFlameEntity.animationState != MonsterFlameAnimationState.PLAYING && monsterFlameEntity.animationState != MonsterFlameAnimationState.STOPPED) return InteractionResult.FAIL;
				
			toggleAnimation(state, monsterFlameEntity);

			if (level.isClientSide) {
				renderFlameParticles(level, pos);
				return InteractionResult.SUCCESS;
			}
			else {
				toggleState(state, level, pos, (Player)null);
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.FAIL;
	}

	private void toggleState(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
		state = (BlockState)state.cycle(LIT);
		level.setBlock(pos, state, 3);
		level.gameEvent(player, (Boolean)state.getValue(LIT) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
		
		boolean isActive = (Boolean)state.getValue(LIT);

		if (level instanceof ServerLevel serverLevel) {
			ServerLevel sLevel = (ServerLevel)level;
			boolean isLit = (Boolean)state.getValue(LIT);
			MobRegion.setMobRegionEnabled(isLit, pos, serverLevel);

			if (isActive) {
				level.playSound((Player)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), ModSounds.MONSTER_FLAME_ENABLE.get(), SoundSource.BLOCKS, 5f, 1f);
			} else {
				level.playSound((Player)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), ModSounds.MONSTER_FLAME_DISABLE.get(), SoundSource.BLOCKS, 5f, 1f);
			}
		}
	}

	private void renderFlameParticles(Level level, BlockPos pos) {
		for (int i = 0; i < 10; i++) {
			RandomSource randomsource = level.getRandom();
			double d0 = (double)pos.getX() + randomsource.nextDouble();
			double d1 = (double)pos.getY() + randomsource.nextDouble();
			double d2 = (double)pos.getZ() + randomsource.nextDouble();
			level.addParticle(ParticleTypes.FLAME, true, d0, d1, d2, 0.0, 0.1, 0.0);
		}
	}

	private void toggleAnimation(BlockState state, MonsterFlameEntity monsterFlameEntity) {
		if ((Boolean)state.getValue(LIT)) {
			monsterFlameEntity.playStopAnimation();
		}
		else{
			monsterFlameEntity.playStartAnimation();
		}
	}
}
