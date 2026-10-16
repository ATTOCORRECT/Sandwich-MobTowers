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

// TAG REGISTRIES GO HERE

	}


	static boolean checked_mods = false;
// MOD LOADED BOOLEANS GO HERE

	public static void GetLoadedMods() {
		if (!checked_mods) {
			ModList list = ModList.get();
// MOD LOADED LOGIC GOES HERE

			checked_mods = true;
			System.out.println(list);
		}
		

	}


	public static boolean IsTowerBlocked(EntityType<?> entity) {
		
		if (entity.is(ModTags.Entities.SPAWN_BLOCKED)) return true;
		GetLoadedMods();
// TAG BLOCKING LOGIC GOES HERE

		return false;
	}

	public static boolean IsTowerSummoned(EntityType<?> entity) {

		if (entity.is(ModTags.Entities.TOWER_SUMMONED)) return true;
		GetLoadedMods();
// TAG SUMMONING LOGIC GOES HERE
		

		return false;
	}
}
