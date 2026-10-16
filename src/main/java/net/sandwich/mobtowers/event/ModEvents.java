package net.sandwich.mobtowers.event;

import com.ibm.icu.impl.units.MeasureUnitImpl.CompoundPart;
import com.mojang.brigadier.Command;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.sandwich.mobtowers.Config;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.block.ModBlocks;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.sandwich.mobtowers.particle.ModParticles;
import net.sandwich.mobtowers.sound.ModSounds;
import net.sandwich.mobtowers.tags.ModTags;
import net.sandwich.mobtowers.voronoi.CellCenter;
import net.sandwich.mobtowers.voronoi.Voronoi;
import net.sandwich.mobtowers.worldseed.ClientSeedCache;
import net.sandwich.mobtowers.worldseed.WorldSeedPayload;

@EventBusSubscriber(modid = MobTowersMod.MOD_ID)
public class ModEvents {

	@SubscribeEvent
	public static void serverLoad(ServerStartingEvent event) {
		MinecraftServer gameServer = event.getServer();
		GameRules rules = gameServer.getGameRules();
		GameRules.IntegerValue sleepingRule = (GameRules.IntegerValue)rules.getRule(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE);
		GameRules.BooleanValue insomniaRule = (GameRules.BooleanValue)rules.getRule(GameRules.RULE_DOINSOMNIA);
		insomniaRule.set(false, gameServer);

		if (Config.APPLY_BED_CHANGES.isTrue()) {
			sleepingRule.set(200, gameServer);
			
		} else {
			if (sleepingRule.get() > 100) sleepingRule.set(100, gameServer);
		}
		
		

		
	}


	@SubscribeEvent // on the game event bus
	public static void registerCommands(RegisterCommandsEvent event) {
		
		event.getDispatcher().register(
			Commands.literal("locatespire")
			.requires(commandSourceStack -> commandSourceStack.hasPermission(2))
			.executes(context -> {
				CommandSourceStack source = context.getSource();

				ServerLevel level = source.getLevel();

				if (level.dimension() != Level.OVERWORLD) {
					context.getSource().sendSuccess(() -> Component.translatable("commands.mobtowers.locate_failed_wrong_dimension"), false);
					return Command.SINGLE_SUCCESS;
				}

				Vec3 originPosition = source.getPosition();
				CellCenter cellCenter = Voronoi.getVoronoiCellCenter((int)(originPosition.x / 16.0), (int)(originPosition.z / 16.0));
				Vec3 cellCenterPosition = new Vec3(cellCenter.x * 16.0, 0.0, cellCenter.z * 16.0);
				

				Component teleportComponent = ComponentUtils.wrapInSquareBrackets(Component.translatable("chat.coordinates", new Object[]{(int)cellCenterPosition.x, "~", (int)cellCenterPosition.z})).withStyle((p_214489_) -> p_214489_.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/tp @s " + (int)cellCenterPosition.x + " ~ " + (int)cellCenterPosition.z)).withHoverEvent(new HoverEvent(net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip"))));
				
				context.getSource().sendSuccess(() -> Component.translatable("commands.mobtowers.located_tower", new Object[]{teleportComponent}), false);
				return Command.SINGLE_SUCCESS;
			})
		);
	}

	@SubscribeEvent
	public static void onCheckSpawn(MobSpawnEvent.PositionCheck event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {


			Level level = (Level)event.getLevel();
			BlockPos blockPos = new BlockPos((int)event.getX(), (int)event.getY(), (int)event.getZ());


			if (event.getSpawnType() != MobSpawnType.NATURAL) return; // exit early if its not a natural spawn
			if (level.dimension() != Level.OVERWORLD) return; // exit early if not in overworld
			if (!ModTags.IsTowerBlocked(event.getEntity().getType())) return; // exit early if it's not a tower spawn mob

			boolean mobRegionEnabled = MobRegion.isMobRegionEnabled(blockPos, serverLevel);
			boolean inDeepslateLayer = blockPos.getY() < 0;
			boolean canSpawn = mobRegionEnabled || inDeepslateLayer;


			if (!canSpawn) {
				event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
			} else {

				
				if (ModTags.IsTowerSummoned(event.getEntity().getType()) && !inDeepslateLayer) {

					int skyLight = level.getBrightness(LightLayer.SKY, blockPos);
					if (skyLight >= 12) 
						level.playSound((Player)null, blockPos, ModSounds.DISTANT_MOB_SPAWN.get(), SoundSource.HOSTILE, 10f, 1f);

					for (int p=0; p < serverLevel.players().size(); p++)
						serverLevel.sendParticles(serverLevel.players().get(p), ModParticles.MONSTER_SPAWN_FLAME.get(), true, blockPos.getX(), blockPos.getY()+1, blockPos.getZ(), 15, 0.5f, 0.5f, 0.5f, 0.0f);
					
				}
			}

		}
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register(
			(state, level, pos, tintIndex) -> {
				if (pos != null) {
					// Calculate color based on position (X, Y, Z)
					return MobRegion.getMobRegionColor(pos);
				}
				// Default color (White) if pos is null
				return 0xFFFFFFFF;
			}, 
			ModBlocks.SEEPING_GRIMSTONE.get()
		);

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
