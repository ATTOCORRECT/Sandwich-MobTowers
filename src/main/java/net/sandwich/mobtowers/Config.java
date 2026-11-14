package net.sandwich.mobtowers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
// import net.minecraft.world.item.Item;
// import net.neoforged.bus.api.SubscribeEvent;
// import net.neoforged.fml.common.EventBusSubscriber;
// import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

	private static int configMessageRegionSize = 12;

	public static void setConfigMessageRegionSize(int regionSize){
		configMessageRegionSize = regionSize;
	}

	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	public static final ModConfigSpec.BooleanValue APPLY_BED_CHANGES = BUILDER
			.comment("§l§nON by default§r\n\nON: You can enter a bed at any time for healing, but the night cannot be skipped.\n\nOFF: Default Minecraft bed behavior.\n\n\n§7§o(§lPlease reload your world for full effect.)")
			.define("applybedchanges", true);

	public static final ModConfigSpec.BooleanValue SPIRE_SPAWNS_PHANTOMS = BUILDER
			.comment("§l§nON by default§r\n\nON: The Spire Heart spawns phantoms around it when it's active.\n\nOFF: The Spire Heart does not spawn phantoms.")
			.define("spirespawnsphantoms", true);

	public static final ModConfigSpec.BooleanValue PHANTOMS_DIE_WITHOUT_HEART = BUILDER
			.comment("§l§nOFF by default§r\n\nOFF: When you disable a Spire Heart, the phantoms around it will continue to live, but they will not keep spawning.\n\nON: When you disable a Spire Heart, the phantoms around it will take damage until they die.")
			.define("phantomsdiewithoutheart", false);

	public static final ModConfigSpec.IntValue NATURAL_SPAWN_DEPTH = BUILDER
			.comment("§l§nDefault value: 0§r\n\nControls the depth in which monsters will spawn regardless of the state of the region's Spire.\n\nDefault is 0, which results in monsters spawning in the deepslate layer even if the Spire is inactive.")
			.defineInRange("naturalmonsterspawningdepth", 0, -64, 64);


	public static final ModConfigSpec.IntValue REGION_SIZE = BUILDER
			.comment("§l§nDefault value: 256§r\n\nMeasured in chunks. Determines the size of the voronoi cells that the Spire regions are created from.\n\nThis value is applied when creating a new world or loading a world with this mod for the first time. A world's region size can be changed with /setregionsize")
			.defineInRange("regionsize", 128, 16, Integer.MAX_VALUE);

	// public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
	// 		.comment("What you want the introduction message to be for the magic number")
	// 		.define("magicNumberIntroduction", "The magic number is... ");

	// // a list of strings that are treated as resource locations for items
	// public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
	// 		.comment("A list of items to log on common setup.")
	// 		.defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);

	static final ModConfigSpec SPEC = BUILDER.build();

	private static boolean validateItemName(final Object obj) {
		return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
	}
}
