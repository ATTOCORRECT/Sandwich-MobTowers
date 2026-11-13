package net.sandwich.mobtowers.clientNetworking;

public class ClientSeedCache {
	private static long currentWorldSeed = 0L;

	public static void setSeed(long seed) {
		currentWorldSeed = seed;
	}

	public static long getSeed() {
		return currentWorldSeed;
	}
}
