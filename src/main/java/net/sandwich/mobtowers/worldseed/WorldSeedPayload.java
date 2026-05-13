package net.sandwich.mobtowers.worldseed;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sandwich.mobtowers.MobTowersMod;

public record WorldSeedPayload(long seed) implements CustomPacketPayload {
	
	// Define a unique identifier for your packet channel
	public static final Type<WorldSeedPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "world_seed"));

	// Codec that handles translating the long into bytes
	public static final StreamCodec<RegistryFriendlyByteBuf, WorldSeedPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG, WorldSeedPayload::seed,
			WorldSeedPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}