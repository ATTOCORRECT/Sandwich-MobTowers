package net.sandwich.mobtowers.voronoi;

import net.minecraft.util.RandomSource;
import net.sandwich.mobtowers.worldseed.ClientSeedCache;


public class Voronoi {

	public static boolean isVoronoiCellCenter(int x, int z, long seed) {
		CellCenter center = getVoronoiCellCenter(x, z, seed);
		return (center.x == x && center.z == z);
	}

	public static boolean isVoronoiCellCenter(int x, int z) {
		CellCenter center = getVoronoiCellCenter(x, z);
		return (center.x == x && center.z == z);
	}

	public static long getVoronoiCellID(int x, int z) {
		CellCenter center = getVoronoiCellCenter(x, z);
		return getVoronoiCellID(center);
	}
	
	public static long getVoronoiCellID(CellCenter center) {
		return (long)center.x * 31234567L ^ (long)center.z * 11234567L;
	}

	public static CellCenter getVoronoiCellCenter(int x, int z) {
		return getVoronoiCellCenter(x, z, ClientSeedCache.getSeed());
	}

	public static CellCenter getVoronoiCellCenter(int x, int z, long levelSeed) {
		return getVoronoiCellCenter(x, z, ClientSeedCache.getSeed(), true);
	}

	public static CellCenter getVoronoiCellCenter(int x, int z, long levelSeed, boolean isManhattan) {
		int cellSize = 16; // Adjust to change size of cells
		int gridX = Math.floorDiv(x, cellSize);
		int gridZ = Math.floorDiv(z, cellSize);

		double minDistance = Double.MAX_VALUE;
		CellCenter center = new CellCenter();

		// Check the 3x3 grid neighborhood
		for (int nx = -1; nx <= 1; nx++) {
			for (int nz = -1; nz <= 1; nz++) {
				int currentGridX = gridX + nx;
				int currentGridZ = gridZ + nz;

				// Generate a stable seed for this specific grid cell
				long seed = ((long)currentGridX * 31234567L ^ (long)currentGridZ * 11234567L) + levelSeed; // use MC seed?
				RandomSource rng = RandomSource.create(seed);

				// Get the random feature point inside this grid cell
				double pointX = (currentGridX * cellSize) + rng.nextInt(cellSize);
				double pointZ = (currentGridZ * cellSize) + rng.nextInt(cellSize);

				// Calculate distance squared
				double dx = x - pointX;
				double dz = z - pointZ;
				double dist = isManhattan ? Math.abs(dx) + Math.abs(dz) : dx * dx + dz * dz;

				if (dist < minDistance) {
					minDistance = dist;
					// Generate a stable color for this specific point
					center.x = (long)pointX;
					center.z = (long)pointZ;

					center.gridX = currentGridX;
					center.gridZ = currentGridZ;
				}
			}
		}

		return center;
	}


}
