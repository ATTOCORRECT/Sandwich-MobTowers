package net.sandwich.mobtowers.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.EventHooks;
import net.sandwich.mobtowers.Config;

@Mixin(EventHooks.class)
public abstract class EventHooksMixin {

	@WrapMethod(method = "canEntityContinueSleeping")
    private static boolean canEntityContinueSleeping(LivingEntity sleeper, Player.BedSleepingProblem problem, Operation<Boolean> original) {
		if (Config.APPLY_BED_CHANGES.isTrue()) return true;
        return original.call(sleeper, problem);
	}

}