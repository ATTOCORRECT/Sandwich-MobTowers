package net.sandwich.mobtowers.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.sandwich.mobtowers.Config;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

	@ModifyVariable(
		method = "startSleepInBed", 
		at = @At("STORE")
	)
	private Either<Player.BedSleepingProblem, Unit> ignoreNormalSleepingConditions(Either<Player.BedSleepingProblem, Unit> original) {
		if (!Config.APPLY_BED_CHANGES.isTrue()) return original;
		if (original.left().isEmpty()) return original;

		return switch (original.left().get()) {
			case NOT_POSSIBLE_NOW, NOT_SAFE -> Either.right(Unit.INSTANCE);
			default -> original;
		};
	}

	@ModifyExpressionValue(
		method = "startSleepInBed",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;canSleepThroughNights()Z"
		)
	)
	private boolean onlyMessageAtNight(boolean originalCanSleep) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		boolean isNight = player.level().isNight();

		// Vanilla checks for !originalCanSleep IE Cant sleep through night,
		// I want Cant sleep Throught Night && Is Night
		// !(!originalCanSleep && isNight) == (originalCanSleep || !isNight).
		return originalCanSleep || !isNight;
	}

}
