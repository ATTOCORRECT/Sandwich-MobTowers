package net.sandwich.mobtowers.block.custom;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.sandwich.mobtowers.voronoi.CellCenter;
import net.sandwich.mobtowers.voronoi.Voronoi;

public class VoronoiBlock extends Block{
	public VoronoiBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (level instanceof ServerLevel serverLevel) {
			CellCenter center = Voronoi.getVoronoiCellCenter(pos);
			System.out.println(pos);
			Component message = Component.literal("My Cell Center Is: " + center.x + " " + center.z);
			serverLevel.players().forEach(p -> p.sendSystemMessage(message));
		}
	}
}
