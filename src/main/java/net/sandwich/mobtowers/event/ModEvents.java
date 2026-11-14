package net.sandwich.mobtowers.event;

import com.mojang.brigadier.Command;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.sandwich.mobtowers.Config;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.block.ModBlocks;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.sandwich.mobtowers.particle.ModParticles;
import net.sandwich.mobtowers.saveddata.ModAttachments;
import net.sandwich.mobtowers.sound.ModSounds;
import net.sandwich.mobtowers.tags.ModTags;
import net.sandwich.mobtowers.voronoi.CellCenter;
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

		// MobRegionSavedData data = MobRegionSavedData.getData(gameServer.overworld().getDataStorage());
		// data.trySetRegionSize();
	}


	@SubscribeEvent // on the game event bus
	public static void registerCommands(RegisterCommandsEvent event) { // Client?
		
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
				BlockPos blockPosition = new BlockPos((int)originPosition.x, 0, (int)originPosition.z);
				MinecraftServer server = context.getSource().getServer();
				int regionSize = ModAttachments.getRegionSize(server);
				CellCenter cellCenter = MobRegion.getMobRegionCell(new ChunkPos(blockPosition), level.getSeed(), regionSize);
				Vec3 cellCenterPosition = new Vec3(cellCenter.x * 16.0, 0.0, cellCenter.z * 16.0);
				

				Component teleportComponent = ComponentUtils.wrapInSquareBrackets(Component.translatable("chat.coordinates", new Object[]{(int)cellCenterPosition.x, "~", (int)cellCenterPosition.z})).withStyle((p_214489_) -> p_214489_.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/tp @s " + (int)cellCenterPosition.x + " ~ " + (int)cellCenterPosition.z)).withHoverEvent(new HoverEvent(net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip"))));
				
				context.getSource().sendSuccess(() -> Component.translatable("commands.mobtowers.located_tower", new Object[]{teleportComponent}), false);
				return Command.SINGLE_SUCCESS;
			})
		);

		event.getDispatcher().register(
			Commands.literal("regionsize")
				.executes(context -> {
					MinecraftServer server = context.getSource().getServer();
					int regionSize = ModAttachments.getRegionSize(server);

					context.getSource().sendSuccess(
						() -> Component.translatable("commands.mobtowers.say_region_size", new Object[]{regionSize}),
						false);
					return Command.SINGLE_SUCCESS;

				})
		);

		event.getDispatcher().register(
			Commands.literal("resetregionsize")
				.executes(context -> {
					MinecraftServer server = context.getSource().getServer();
					int regionSize = ModAttachments.getRegionSize(server);

					MutableComponent message = Component.translatable("commands.mobtowers.warn_reset_region_size_confirmation")
						.withStyle(ChatFormatting.RED);

					Component clickableMessage = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.mobtowers.confirm_reset_region_size"))
						.withStyle(style -> style
							.withColor(ChatFormatting.YELLOW)
							.withUnderlined(true)
							.withClickEvent(new ClickEvent(
								ClickEvent.Action.RUN_COMMAND,
								"/resetregionsize trigger"
							))
							.withHoverEvent(new HoverEvent(
								HoverEvent.Action.SHOW_TEXT,
								Component.translatable("commands.mobtowers.region_size_reset_complete")
							))
						);

					message.append(clickableMessage);

					context.getSource().sendSuccess(
						() -> message, 
						false);

					return Command.SINGLE_SUCCESS;

				})

				.then(Commands.literal("trigger")
					.executes(context -> {

						MinecraftServer server = context.getSource().getServer();
						int configRegionSize = Config.REGION_SIZE.getAsInt();
						ModAttachments.setRegionSize(server, configRegionSize);

						syncForPlayer(context.getSource().getPlayer());

						context.getSource().sendSuccess(
							() -> Component.translatable("commands.mobtowers.region_size_reset_value_complete", new Object[]{configRegionSize}), 
							false);

						return Command.SINGLE_SUCCESS;
					})
				)
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
			boolean is_below_natural_spawn_depth = blockPos.getY() < Config.NATURAL_SPAWN_DEPTH.getAsInt();
			boolean canSpawn = mobRegionEnabled || is_below_natural_spawn_depth;


			if (!canSpawn) {
				event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
			} else {

				
				if (ModTags.IsTowerSummoned(event.getEntity().getType()) && !is_below_natural_spawn_depth) {

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

	// @SubscribeEvent
	// public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
	// 	if (event.getEntity() instanceof ServerPlayer serverPlayer) {
	// 		// Get seed
	// 		long worldSeed = serverPlayer.getServer().getWorldData().worldGenOptions().seed();
		
	// 		// Send seed to client
	// 		PacketDistributor.sendToPlayer(serverPlayer, new WorldSeedPayload(worldSeed));
			
	// 		syncForPlayer(event.getEntity());
	// 	}
	// }

	private static void syncForPlayer(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			MinecraftServer server = serverPlayer.server;
			int overworldValue = ModAttachments.getRegionSize(server);
			serverPlayer.serverLevel().setData(ModAttachments.REGION_SIZE, overworldValue);
		}
	}

	@SubscribeEvent
	public static void onLevelLoad(LevelEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {
			if (serverLevel.dimension() == Level.OVERWORLD) {
				MinecraftServer server = serverLevel.getServer();

				if(ModAttachments.getRegionSize(server) == -1) {
					ModAttachments.setRegionSize(server, Config.REGION_SIZE.getAsInt());
				} 
			}
		}
	}
}
