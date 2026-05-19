package net.sandwich.mobtowers.block.custom;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.sandwich.mobtowers.saveddata.MobRegionSavedData;

public class VoronoiBlock extends Block{

	public VoronoiBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		// if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
		// 	MobRegionSavedData data = MobRegionSavedData.getData(serverLevel);

		// 	Component message = Component.literal("hi" + data.getCellIDs());
		// 	serverLevel.players().forEach(p -> p.sendSystemMessage(message));

		// 	for (long cellID : data.getCellIDs()) {
		// 		Component message2 = Component.literal("" + cellID);
		// 		serverLevel.players().forEach(p -> p.sendSystemMessage(message));
		// 	}
		// }
	}
}
