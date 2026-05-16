package net.sandwich.mobtowers.mobregion;

import com.jcraft.jorbis.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.sandwich.mobtowers.saveddata.MobRegionSavedData;
import net.sandwich.mobtowers.voronoi.Voronoi;

public class MobRegion {

	public static void setMobRegionEnabled(boolean isEnabled, BlockPos blockPos, ServerLevel serverLevel) {
		setMobRegionEnabled(isEnabled, new ChunkPos(blockPos), serverLevel);
	}

	public static void setMobRegionEnabled(boolean isEnabled, ChunkPos chunkPos, ServerLevel serverLevel) {
		if (isEnabled){
			enableMobRegion(chunkPos, serverLevel);
		}
		else {
			disableMobRegion(chunkPos, serverLevel);
		}
	}

	public static void disableMobRegion(ChunkPos chunkPos, ServerLevel serverLevel) {
		long cellID = Voronoi.getVoronoiCellID(chunkPos);
		disableMobRegion(cellID, serverLevel);
	}

	public static void disableMobRegion(long cellID, ServerLevel serverLevel) {
		MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);
		data.addCellID(cellID);
	}

	public static void enableMobRegion(ChunkPos chunkPos, ServerLevel serverLevel) {
		long cellID = Voronoi.getVoronoiCellID(chunkPos);
		enableMobRegion(cellID, serverLevel);
	}

	public static void enableMobRegion(long cellID, ServerLevel serverLevel) {
		MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);
		data.removeCellID(cellID);
	}

	public static boolean isMobRegionEnabled(BlockPos blockPos, ServerLevel serverLevel) {
		return isMobRegionEnabled(new ChunkPos(blockPos), serverLevel);
	}

	public static boolean isMobRegionEnabled(ChunkPos chunkPos, ServerLevel serverLevel) {
		long cellID = Voronoi.getVoronoiCellID(chunkPos);
		return isMobRegionEnabled(cellID, serverLevel);
	}

	public static boolean isMobRegionEnabled(long cellID, ServerLevel serverLevel) {
		MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);
		return !data.containsCellID(cellID);
	}
}
