package net.sandwich.mobtowers.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.block.ModBlocks;
import net.sandwich.mobtowers.voronoi.Voronoi;
import net.sandwich.mobtowers.worldseed.ClientSeedCache;
import net.sandwich.mobtowers.worldseed.WorldSeedPayload;

@EventBusSubscriber(modid = MobTowersMod.MOD_ID)
public class ModEvents {

	@SubscribeEvent
	public static void onCheckSpawn(MobSpawnEvent.PositionCheck event) {
		double x = event.getX();
		double z = event.getZ();

		boolean canSpawn = Math.abs(x) < 32 && Math.abs(z) < 32; // change out condition
		if (canSpawn) { 
			if (event.getLevel() instanceof ServerLevel serverLevel) {
				Component message = Component.literal("the spawn blockerr of: " + event.getEntity().getType().getDescriptionId());
				serverLevel.players().forEach(p -> p.sendSystemMessage(message));
			}

			event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
		}
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		// Register the handler for your specific block
		event.register((state, level, pos, tintIndex) -> {
			if (pos != null) {
				// Calculate color based on position (X, Y, Z)
				return Voronoi.getVoronoiColor(pos);
			}
			// Default color (White) if pos is null
			return 0xFFFFFFFF;
		}, ModBlocks.VORONOI_BLOCK.get());
	}

	@SubscribeEvent
	public static void registerPayloads(RegisterPayloadHandlersEvent event) {
	
		final PayloadRegistrar registrar = event.registrar(MobTowersMod.MOD_ID);

		registrar.playToClient(
			WorldSeedPayload.TYPE,
			WorldSeedPayload.CODEC,
			(payload, context) -> {
				context.enqueueWork(() -> {
					ClientSeedCache.setSeed(payload.seed());
				});
			}
		);
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			// Get seed
			long worldSeed = serverPlayer.getServer().getWorldData().worldGenOptions().seed();
			
			// Send seed to client
			PacketDistributor.sendToPlayer(serverPlayer, new WorldSeedPayload(worldSeed));
		}
	}
}
