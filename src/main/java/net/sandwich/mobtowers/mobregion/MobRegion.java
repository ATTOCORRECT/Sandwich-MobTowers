package net.sandwich.mobtowers.mobregion;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.sandwich.mobtowers.Config;
import net.sandwich.mobtowers.saveddata.MobRegionSavedData;
import net.sandwich.mobtowers.saveddata.ModAttachments;
import net.sandwich.mobtowers.voronoi.Voronoi;
import net.sandwich.mobtowers.worldseed.ClientSeedCache;
import net.sandwich.mobtowers.voronoi.CellCenter;

public class MobRegion {

	private static int[] cellColors = {
	//  XXAARRGGBB
		0xFFA4D04F,
		0xFF83FDD9,
		0xFFBBFD3D,
		0xFFC7FDBE,
		0xFFFDF33D,
		0xFF01F3C6,
		0xFF3FE593,
		0xFFFDBE3D,
		0xFF73ED4D
	};

	// CLIENT ONLY

	public static int getMobRegionColor(BlockPos blockPos) {
		return getMobRegionColor(new ChunkPos(blockPos));
	}

	public static int getMobRegionColor(ChunkPos chunkPos) {
		CellCenter center = getMobRegionCell(chunkPos);

		//if (center.x == chunkPos.x && center.z == chunkPos.z) { // for marking centers
		//	return 0x000000;
		//}

		int colorIndex = Math.floorMod(center.gridX, 3) + Math.floorMod(center.gridZ, 3) * 3;
		colorIndex = Math.min(colorIndex, cellColors.length - 1); // make sure we dont go over
		return cellColors[colorIndex];
	}

	// ====

	// SERVER ONLY

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
		long cellID = getMobRegionCellID(chunkPos, serverLevel);
		disableMobRegion(cellID, serverLevel);
	}

	public static void disableMobRegion(long cellID, ServerLevel serverLevel) {
		MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);
		data.addCellID(cellID);
	}

	public static void enableMobRegion(ChunkPos chunkPos, ServerLevel serverLevel) {
		long cellID = getMobRegionCellID(chunkPos, serverLevel);
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
		long cellID = getMobRegionCellID(chunkPos, serverLevel);
		return isMobRegionEnabled(cellID, serverLevel);
	}

	public static boolean isMobRegionEnabled(long cellID, ServerLevel serverLevel) {
		if (serverLevel.dimension() != Level.OVERWORLD) return true;
		MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);
		return !data.containsCellID(cellID);
	}

	public static long getMobRegionCellID(ChunkPos chunkPos, ServerLevel serverLevel) {
		CellCenter center = getMobRegionCell(chunkPos, serverLevel);
		return Voronoi.getVoronoiCellID(center);
	}

	// ====



	public static CellCenter getMobRegionCell(BlockPos blockPos) { // Client Block Pos
		return getMobRegionCell(new ChunkPos(blockPos));
	}

	public static CellCenter getMobRegionCell(ChunkPos chunkPos) { // Client Chunk Pos
		long seed = ClientSeedCache.getSeed();
		int regionSize = ModAttachments.getClientRegionSize();
		return getMobRegionCell(chunkPos, seed, regionSize);
	}

	public static CellCenter getMobRegionCell(ChunkPos chunkPos, ServerLevel serverLevel) { // Server Chunk Pos
		long seed = serverLevel.getSeed();
		int regionSize = ModAttachments.getRegionSize(serverLevel.getServer());
		return getMobRegionCell(chunkPos, seed, regionSize);
	}

	public static CellCenter getMobRegionCell(ChunkPos chunkPos, long seed, int regionSize) { // Base
		if (regionSize == -1) regionSize = Config.REGION_SIZE.getAsInt();
		return Voronoi.getVoronoiCellCenter(chunkPos.x, chunkPos.z, seed, regionSize, true, 0.1f);
	}
}
