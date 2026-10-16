package net.sandwich.mobtowers.tags;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.sandwich.mobtowers.MobTowersMod;

public class ModTags {
	public static class Entities {

		private static TagKey<EntityType<?>> createTag(String name) {
			return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, name));
		}

		public static TagKey<EntityType<?>> SPAWN_BLOCKED = createTag("tower_blocked");
		public static TagKey<EntityType<?>> TOWER_SUMMONED = createTag("tower_summoned");


		public static TagKey<EntityType<?>> CAVERNS_AND_CHASMS_TOWER_SUMMONED = createTag("modded/caverns_and_chasms/tower_summoned");
		public static TagKey<EntityType<?>> CAVERNS_AND_CHASMS_TOWER_BLOCKED = createTag("modded/caverns_and_chasms/tower_blocked");
		public static TagKey<EntityType<?>> OPPOSING_FORCE_TOWER_SUMMONED = createTag("modded/opposing_force/tower_summoned");
		public static TagKey<EntityType<?>> OPPOSING_FORCE_TOWER_BLOCKED = createTag("modded/opposing_force/tower_blocked");
		public static TagKey<EntityType<?>> PEACELESS_TOWER_SUMMONED = createTag("modded/peaceless/tower_summoned");
		public static TagKey<EntityType<?>> PEACELESS_TOWER_BLOCKED = createTag("modded/peaceless/tower_blocked");
		public static TagKey<EntityType<?>> SPECIES_TOWER_SUMMONED = createTag("modded/species/tower_summoned");
		public static TagKey<EntityType<?>> SPECIES_TOWER_BLOCKED = createTag("modded/species/tower_blocked");

	}


	static boolean checked_mods = false;

	static boolean caverns_and_chasms_loaded = false;
	static boolean opposing_force_loaded = false;
	static boolean peaceless_loaded = false;
	static boolean species_loaded = false;

	public static void GetLoadedMods() {
		if (!checked_mods) {
			ModList list = ModList.get();

			caverns_and_chasms_loaded = list.isLoaded("caverns_and_chasms");
			System.out.println("Is Caverns And Chasms loaded: " + Boolean.toString(caverns_and_chasms_loaded).toUpperCase());
			opposing_force_loaded = list.isLoaded("opposing_force");
			System.out.println("Is Opposing Force loaded: " + Boolean.toString(opposing_force_loaded).toUpperCase());
			peaceless_loaded = list.isLoaded("peaceless");
			System.out.println("Is Peaceless loaded: " + Boolean.toString(peaceless_loaded).toUpperCase());
			species_loaded = list.isLoaded("species");
			System.out.println("Is Species loaded: " + Boolean.toString(species_loaded).toUpperCase());

			checked_mods = true;
			System.out.println(list);
		}
		

	}


	public static boolean IsTowerBlocked(EntityType<?> entity) {
		
		if (entity.is(ModTags.Entities.SPAWN_BLOCKED)) return true;
		GetLoadedMods();

		if (caverns_and_chasms_loaded) if (entity.is(ModTags.Entities.CAVERNS_AND_CHASMS_TOWER_BLOCKED)) return true;
		if (opposing_force_loaded) if (entity.is(ModTags.Entities.OPPOSING_FORCE_TOWER_BLOCKED)) return true;
		if (peaceless_loaded) if (entity.is(ModTags.Entities.PEACELESS_TOWER_BLOCKED)) return true;
		if (species_loaded) if (entity.is(ModTags.Entities.SPECIES_TOWER_BLOCKED)) return true;

		return false;
	}

	public static boolean IsTowerSummoned(EntityType<?> entity) {

		if (entity.is(ModTags.Entities.TOWER_SUMMONED)) return true;
		GetLoadedMods();

		if (caverns_and_chasms_loaded) if (entity.is(ModTags.Entities.CAVERNS_AND_CHASMS_TOWER_SUMMONED)) return true;
		if (opposing_force_loaded) if (entity.is(ModTags.Entities.OPPOSING_FORCE_TOWER_SUMMONED)) return true;
		if (peaceless_loaded) if (entity.is(ModTags.Entities.PEACELESS_TOWER_SUMMONED)) return true;
		if (species_loaded) if (entity.is(ModTags.Entities.SPECIES_TOWER_SUMMONED)) return true;
		

		return false;
	}
}
