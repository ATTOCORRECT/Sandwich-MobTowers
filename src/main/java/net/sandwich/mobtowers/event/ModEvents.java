package net.sandwich.mobtowers.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.block.ModBlocks;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.sandwich.mobtowers.particle.ModParticles;
import net.sandwich.mobtowers.tags.ModTags;
import net.sandwich.mobtowers.worldseed.ClientSeedCache;
import net.sandwich.mobtowers.worldseed.WorldSeedPayload;

@EventBusSubscriber(modid = MobTowersMod.MOD_ID)
public class ModEvents {

	@SubscribeEvent
	public static void onCheckSpawn(MobSpawnEvent.PositionCheck event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {

			if (event.getSpawnType() != MobSpawnType.NATURAL) return; // exit early if its not a natural spawn
			
			if (!event.getEntity().getType().is(ModTags.Entities.SPAWN_BLOCKED)) return; // exit early if its not a tower spawn mob

			BlockPos blockPos = new BlockPos((int)event.getX(), (int)event.getY(), (int)event.getZ());
			boolean canSpawn = MobRegion.isMobRegionEnabled(blockPos, serverLevel);


			
			if (!canSpawn) {
				event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
			} else {
				if (event.getEntity().getType().is(ModTags.Entities.TOWER_SUMMONED)) {
					for (int p=0; p < serverLevel.players().size(); p++) 
					{
						serverLevel.sendParticles(serverLevel.players().get(p), ModParticles.TOWER_FLAME.get(), true, blockPos.getX(), blockPos.getY()+1, blockPos.getZ(), 15, 0.5f, 0.5f, 0.5f, 0.0f);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((state, level, pos, tintIndex) -> {
			if (pos != null) {
				// Calculate color based on position (X, Y, Z)
				return Voronoi.getVoronoiColor(pos);
			}
			// Default color (White) if pos is null
			return 0xFFFFFFFF;
		}, ModBlocks.SEEPING_GRIMSTONE.get());

		event.register(
			(state, level, pos, tintIndex) -> {
				if (pos != null) {
					return MobRegion.getMobRegionColor(pos);
				}
				return 0xFFFFFFFF;
			}, 
			ModBlocks.VORONOI_BLOCK.get()
		);
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
