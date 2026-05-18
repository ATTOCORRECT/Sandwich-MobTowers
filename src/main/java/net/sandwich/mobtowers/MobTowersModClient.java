package net.sandwich.mobtowers;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.sandwich.mobtowers.particle.ModParticles;
import net.sandwich.mobtowers.particle.custom.TowerFlameParticle;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = MobTowersMod.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = MobTowersMod.MOD_ID, value = Dist.CLIENT)
public class MobTowersModClient {
	public MobTowersModClient(ModContainer container) {
		// Allows NeoForge to create a config screen for this mod's configs.
		// The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
		// Do not forget to add translations for your config options to the en_us.json file.
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	@SubscribeEvent
	static void onClientSetup(FMLClientSetupEvent event) {
		// Some client setup code
		MobTowersMod.LOGGER.info("HELLO FROM CLIENT SETUP");
		MobTowersMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
	}
	
	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(ModParticles.TOWER_FLAME.get(), TowerFlameParticle.Provider::new);
	}
}
