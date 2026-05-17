package net.sandwich.mobtowers.block.custom;

import javax.annotation.Nullable;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.sandwich.mobtowers.saveddata.MobRegionSavedData;
import net.sandwich.mobtowers.voronoi.CellCenter;
import net.sandwich.mobtowers.voronoi.Voronoi;

public class VoronoiBlock extends Block{
	public VoronoiBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
			MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);
			
			CellCenter c = Voronoi.getVoronoiCellCenter(pos);
			long cellID = Voronoi.getVoronoiCellID(pos);
			data.addCellID(cellID);

			System.out.println("My Cell Center Is: " + cellID + ", which is at " + c.x + ", " + c.z);
		}
	}
}
