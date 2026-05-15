package net.sandwich.mobtowers.voronoi;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.sandwich.mobtowers.worldseed.ClientSeedCache;

public class Voronoi {

	public static boolean isVoronoiCellCenter(ChunkPos pos) {
		return isVoronoiCellCenter (pos.x, pos.z);
	}

	public static boolean isVoronoiCellCenter(int x, int z) {
		CellCenter center = getVoronoiCellCenter(x, z);
		return (center.x == x && center.z == z);
	}

	public static int getVoronoiColor(BlockPos pos) {
		return getVoronoiColor(new ChunkPos(pos));
	}

	public static int getVoronoiColor(ChunkPos pos) {
		return getVoronoiColor(pos.x, pos.z);
	}

	private static int getVoronoiColor(int x, int z) {
		CellCenter center = getVoronoiCellCenter(x, z);
		long seed = getVoronoiCellID(center);
		RandomSource rng = RandomSource.create(seed);

		if (center.x == x && center.z == z) { // for marking centers
			return 0x000000;
		}

		return rng.nextInt(0xFFFFFF);
	}

	public static long getVoronoiCellID(BlockPos pos) {
		return getVoronoiCellID(new ChunkPos(pos));
	}

	public static long getVoronoiCellID(ChunkPos pos) {
		return getVoronoiCellID(pos.x, pos.z);
	}

	private static long getVoronoiCellID(int x, int z) {
		CellCenter center = getVoronoiCellCenter(x, z);
		return getVoronoiCellID(center);
	}
	
	public static long getVoronoiCellID(CellCenter center) {
		return (long)center.x * 31234567L ^ (long)center.z * 11234567L;
	}

	public static CellCenter getVoronoiCellCenter(BlockPos pos) {
		return getVoronoiCellCenter(new ChunkPos(pos));
	}

	public static CellCenter getVoronoiCellCenter(ChunkPos pos) {
		return getVoronoiCellCenter(pos.x, pos.z);
	}

	private static CellCenter getVoronoiCellCenter(int x, int z) {
		int cellSize = 16; // Adjust to change size of cells
		int gridX = Math.floorDiv(x, cellSize);
		int gridZ = Math.floorDiv(z, cellSize);

		double minDistanceSq = Double.MAX_VALUE;
		CellCenter center = new CellCenter();

		// Check the 3x3 grid neighborhood
		for (int nx = -1; nx <= 1; nx++) {
			for (int nz = -1; nz <= 1; nz++) {
				int currentGridX = gridX + nx;
				int currentGridZ = gridZ + nz;

				// Generate a stable seed for this specific grid cell
				long seed = ((long)currentGridX * 31234567L ^ (long)currentGridZ * 11234567L) + ClientSeedCache.getSeed(); // use MC seed?
				RandomSource rng = RandomSource.create(seed);

				// Get the random feature point inside this grid cell
				double pointX = (currentGridX * cellSize) + rng.nextInt(cellSize);
				double pointZ = (currentGridZ * cellSize) + rng.nextInt(cellSize);

				// Calculate distance squared
				double dx = x - pointX;
				double dz = z - pointZ;
				double distSq = dx * dx + dz * dz;

				if (distSq < minDistanceSq) {
					minDistanceSq = distSq;
					// Generate a stable color for this specific point
					center.x = (long)pointX;
					center.z = (long)pointZ;
				}
			}
		}

		return center;
	}


}
