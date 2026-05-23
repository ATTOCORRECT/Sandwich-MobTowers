package net.sandwich.mobtowers.mobregion;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.sandwich.mobtowers.saveddata.MobRegionSavedData;
import net.sandwich.mobtowers.voronoi.Voronoi;
import net.sandwich.mobtowers.voronoi.CellCenter;

public class MobRegion {

	private static int[] cellColors = {
		0xFF00FA7B,
		0xFF00C140,
		0xFF10A300,
		0xFF7DD700,
		0xFFA0DD7C,
		0xFF32BFA8,
		0xFF00D769,
		0xFF3AFA00,
		0xFF38FF00
	};

	public static int getMobRegionColor(BlockPos blockPos) {
		return getMobRegionColor(new ChunkPos(blockPos));
	}

	public static int getMobRegionColor(ChunkPos chunkPos) {
		CellCenter center = Voronoi.getVoronoiCellCenter(chunkPos.x, chunkPos.z);

		if (center.x == chunkPos.x && center.z == chunkPos.z) { // for marking centers
			return 0x000000;
		}

		int colorIndex = Math.floorMod(center.gridX, 3) + Math.floorMod(center.gridZ, 3) * 3;
		colorIndex = Math.min(colorIndex, cellColors.length - 1); // make sure we dont go over
		return cellColors[colorIndex];
	}

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
		long cellID = Voronoi.getVoronoiCellID(chunkPos.x, chunkPos.z);
		disableMobRegion(cellID, serverLevel);
	}

	public static void disableMobRegion(long cellID, ServerLevel serverLevel) {
		MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);
		data.addCellID(cellID);
	}

	public static void enableMobRegion(ChunkPos chunkPos, ServerLevel serverLevel) {
		long cellID = Voronoi.getVoronoiCellID(chunkPos.x, chunkPos.z);
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
		long cellID = Voronoi.getVoronoiCellID(chunkPos.x, chunkPos.z);
		return isMobRegionEnabled(cellID, serverLevel);
	}

	public static boolean isMobRegionEnabled(long cellID, ServerLevel serverLevel) {
		MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);
		return !data.containsCellID(cellID);
	}

	public static CellCenter getMobRegionCell(ChunkPos chunkPos) {
		return Voronoi.getVoronoiCellCenter(chunkPos.x, chunkPos.z);
	}

	public static CellCenter getMobRegionCell(ChunkPos chunkPos, long worldSeed) {
		return Voronoi.getVoronoiCellCenter(chunkPos.x, chunkPos.z, worldSeed);
	}
}
