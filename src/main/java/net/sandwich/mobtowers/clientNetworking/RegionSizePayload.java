package net.sandwich.mobtowers.clientNetworking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sandwich.mobtowers.MobTowersMod;

public record RegionSizePayload(int regionSize) implements CustomPacketPayload {
	
	public static final Type<RegionSizePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "region_size"));

	public static final StreamCodec<RegistryFriendlyByteBuf, RegionSizePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, RegionSizePayload::regionSize,
			RegionSizePayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}