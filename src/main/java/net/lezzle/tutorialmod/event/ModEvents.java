package net.lezzle.tutorialmod.event;

import net.lezzle.tutorialmod.TutorialMod;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.SoundDefinition.Sound;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

@EventBusSubscriber(modid = TutorialMod.MODID)
public class ModEvents {
	@SubscribeEvent
	public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity instanceof Player player && !entity.level().isClientSide()) {
			player.sendSystemMessage(Component.literal("Yyyyyuppppp"));
			//if (entity.level() instanceof ServerLevel serverLevel) {
			//	serverLevel.playSound(entity, 0, 0, 0,  "minecraft:entity.allay.hurt", SoundSource.BLOCKS, 1, 1);
			//}
		}
	}
}
