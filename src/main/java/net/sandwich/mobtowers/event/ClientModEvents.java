package net.sandwich.mobtowers.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.client.block.noiseVarying.NoiseVaryingModelLoader;

@EventBusSubscriber(modid = MobTowersMod.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

	@SubscribeEvent
	public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
		System.out.print("Registering Geometry Loader for Noise Varying");
		event.register(
			ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "noise_varying"), 
			NoiseVaryingModelLoader.INSTANCE
		);
	}
}