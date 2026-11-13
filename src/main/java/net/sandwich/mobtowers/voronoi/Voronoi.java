package net.sandwich.mobtowers.voronoi;

import net.minecraft.util.RandomSource;
import net.sandwich.mobtowers.clientNetworking.ClientSeedCache;



public class Voronoi {

	public static double worleyNoise(int x, int z, int scale) {
		return worleyNoise(x, z, scale, ClientSeedCache.getSeed());
	}

	public static double worleyNoise(int x, int z, int scale, long seed) {
		CellCenter center = getVoronoiCellCenter(x, z, seed, scale, false, 0);

		double normalizedDist = Math.clamp(Math.sqrt(center.distance) / scale, 0, 1);

		return normalizedDist;
	}
	
	public static long getVoronoiCellID(CellCenter center) {
		return (long)center.x * 31234567L ^ (long)center.z * 11234567L;
	}

	public static CellCenter getVoronoiCellCenter(int x, int z, long levelSeed, int cellSize, boolean isManhattan, float padding) {
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
				int min = (int)(cellSize * padding * 0.5f);
				int max = cellSize - min;

				double pointX = (currentGridX * cellSize) + rng.nextIntBetweenInclusive(min, max);
				double pointZ = (currentGridZ * cellSize) + rng.nextIntBetweenInclusive(min, max);

				// Calculate distance squared
				double dx = x - pointX;
				double dz = z - pointZ;
				double dist = isManhattan ? Math.abs(dx) + Math.abs(dz) : dx * dx + dz * dz;

				if (dist < minDistance) {
					minDistance = dist;

					center.distance = minDistance;

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
