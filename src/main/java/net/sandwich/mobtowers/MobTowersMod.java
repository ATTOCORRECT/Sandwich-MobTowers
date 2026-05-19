package net.sandwich.mobtowers;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sandwich.mobtowers.block.ModBlocks;
import net.sandwich.mobtowers.item.ModItems;
import net.sandwich.mobtowers.particle.ModParticles;
import net.sandwich.mobtowers.particle.custom.TowerFlameParticle;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MobTowersMod.MOD_ID)
public class MobTowersMod {
	// Define mod id in a common place for everything to reference
	public static final String MOD_ID = "mobtowers";
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();
	
	
	// The constructor for the mod class is the first code that is run when your mod is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
	public MobTowersMod(IEventBus modEventBus, ModContainer modContainer) {
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);

		// Register ourselves for server and other game events we are interested in.
		// Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
		// Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
		NeoForge.EVENT_BUS.register(this);

		ModItems.register(modEventBus);
		ModBlocks.register(modEventBus);
		ModParticles.register(modEventBus);

		// Register the item to a creative tab
		modEventBus.addListener(this::addCreative);

		// Register our mod's ModConfigSpec so that FML can create and load the config file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
		MobTowerStructurePlacement.DEFERRED_REGISTRY_STRUCTURE_PLACEMENT_TYPE.register(modEventBus);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		// Some common setup code
		LOGGER.info("HELLO FROM COMMON SETUP");

		if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
			LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
		}

		LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

		Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
	}

	// Add the example block item to the building blocks tab
	private void addCreative(BuildCreativeModeTabContentsEvent event) {

		if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			event.accept(ModBlocks.VORONOI_BLOCK);
		}
		if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			event.accept(ModBlocks.MONSTER_FLAME);
		}
		if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			event.accept(ModBlocks.GRIMSTONE);
			event.accept(ModBlocks.COBBLED_GRIMSTONE);
			event.accept(ModBlocks.PALE_GRIMSTONE);
			event.accept(ModBlocks.COBBLED_PALE_GRIMSTONE);
			event.accept(ModBlocks.TEMPERED_GRIMSTONE);
			event.accept(ModBlocks.COBBLED_TEMPERED_GRIMSTONE);
			event.accept(ModBlocks.SEEPING_GRIMSTONE);
		}
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {

	}
}
