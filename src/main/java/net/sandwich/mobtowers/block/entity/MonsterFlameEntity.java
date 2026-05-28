package net.sandwich.mobtowers.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MonsterFlameEntity extends BlockEntity {

	public static final int animationLength = 100;
	public int animationTime = animationLength;

	public MonsterFlameEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MONSTER_FLAME_BE.get(), pos, blockState);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, MonsterFlameEntity blockEntity) {
		blockEntity.animationTime += 1;
		// System.out.println("CLIENT:" + blockEntity.animationTime);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, MonsterFlameEntity blockEntity) {
		blockEntity.animationTime += 1;
		// System.out.println("SERVER:" + blockEntity.animationTime);
	}

	public static void resetAnimationTime(MonsterFlameEntity blockEntity) {
		blockEntity.animationTime = 0;
	}

	
}
