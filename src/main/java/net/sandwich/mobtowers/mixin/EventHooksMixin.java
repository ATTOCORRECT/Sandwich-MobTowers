package net.sandwich.mobtowers.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.sandwich.mobtowers.Config;




@Mixin(EventHooks.class)
public abstract class EventHooksMixin {

	@Overwrite 
	public static boolean canEntityContinueSleeping(LivingEntity sleeper, Player.@Nullable BedSleepingProblem problem) {

		if (Config.APPLY_BED_CHANGES.isTrue()) {
			return true;
		} else {
			return ((CanContinueSleepingEvent)NeoForge.EVENT_BUS.post(new CanContinueSleepingEvent(sleeper, problem))).mayContinueSleeping();
		}
		
	}

}