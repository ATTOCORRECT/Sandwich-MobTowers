package net.sandwich.mobtowers.clientNetworking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sandwich.mobtowers.MobTowersMod;

public record WorldSeedPayload(long seed) implements CustomPacketPayload {
	
	public static final Type<WorldSeedPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "world_seed"));

	public static final StreamCodec<RegistryFriendlyByteBuf, WorldSeedPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG, WorldSeedPayload::seed,
			WorldSeedPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}