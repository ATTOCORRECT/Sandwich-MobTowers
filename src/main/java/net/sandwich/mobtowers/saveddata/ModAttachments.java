package net.sandwich.mobtowers.saveddata;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.sandwich.mobtowers.MobTowersMod;

public class ModAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
		DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MobTowersMod.MOD_ID);
	
	public static final Supplier<AttachmentType<Integer>> REGION_SIZE = ATTACHMENT_TYPES.register(
		"region_size",
		() -> AttachmentType.builder(() -> -1)
			.serialize(Codec.INT)
			.sync(ByteBufCodecs.VAR_INT)
			.build()
	);

	// Server
	public static void setRegionSize(MinecraftServer server, int newValue) {
		ServerLevel overworld = server.overworld();
		overworld.setData(REGION_SIZE, newValue);

		for (ServerLevel level : server.getAllLevels()) {
			if (level != overworld) {
				level.setData(REGION_SIZE, newValue);
			}
		}
	}

	// Server
	public static int getRegionSize(MinecraftServer server) {
		return server.overworld().getData(REGION_SIZE);
	}

	// Client
	public static int getClientRegionSize() {
		ClientLevel level = Minecraft.getInstance().level;
		return level.getData(REGION_SIZE);
	}
}
