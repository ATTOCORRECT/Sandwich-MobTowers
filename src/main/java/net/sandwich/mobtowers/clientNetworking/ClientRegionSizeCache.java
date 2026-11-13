package net.sandwich.mobtowers.clientNetworking;

public class ClientRegionSizeCache {
	private static int regionSize = 0;

	public static void setRegionSize(int seed) {
		regionSize = seed;
	}

	public static int getRegionSize() {
		return regionSize;
	}
}
