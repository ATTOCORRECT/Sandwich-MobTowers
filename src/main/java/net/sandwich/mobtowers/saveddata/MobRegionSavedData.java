package net.sandwich.mobtowers.saveddata;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class MobRegionSavedData extends SavedData {
	public static final String ID = "mob_region_data";

	private final List<Long> disabledRegionCellIDs = new ArrayList<>();

	public static MobRegionSavedData getData(ServerLevel serverLevel) {
		return serverLevel.getDataStorage().computeIfAbsent(MobRegionSavedData.FACTORY, MobRegionSavedData.ID);
	}

	public List<Long> getCellIDs() {
		return this.disabledRegionCellIDs;
	}

	public void addCellID(long cellID) {
		if (!this.disabledRegionCellIDs.contains(cellID)) {
			this.disabledRegionCellIDs.add(cellID);
			this.setDirty();
		}
	}

	public void removeCellID(long cellID) {
		if (this.disabledRegionCellIDs.contains(cellID)) {
			this.disabledRegionCellIDs.remove(cellID);
			this.setDirty();
		}
	}

	public boolean containsCellID(long cellID) {
		return this.disabledRegionCellIDs.contains(cellID);
	}


	// Create new instance of saved data
	public static MobRegionSavedData create() {
		return new MobRegionSavedData();
	}

	public static final SavedData.Factory<MobRegionSavedData> FACTORY = new SavedData.Factory<>(
			MobRegionSavedData::new,
			MobRegionSavedData::load,
			null
	);

	// Load existing instance of saved data
	public static MobRegionSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
		MobRegionSavedData data = MobRegionSavedData.create();

		if (tag.contains("SavedCellIDs", 12)) { // 12 is the official NBT ID for Long Arrays
			long[] array = tag.getLongArray("SavedCellIDs");
			for (long cellID : array) {
				data.disabledRegionCellIDs.add(cellID);
			}
		}
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
		long[] array = new long[this.disabledRegionCellIDs.size()];
		for (int i = 0; i < this.disabledRegionCellIDs.size(); i++) {
			array[i] = this.disabledRegionCellIDs.get(i);
		}
		tag.putLongArray("SavedIds", array);
		return tag;
	}
}