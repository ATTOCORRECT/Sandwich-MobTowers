package net.sandwich.mobtowers.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MonsterFlameEntity extends BlockEntity {
	public MonsterFlameEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MONSTER_FLAME_BE.get(), pos, blockState);
	}

	
}
