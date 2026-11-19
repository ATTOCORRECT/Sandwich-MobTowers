package net.sandwich.mobtowers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.sandwich.mobtowers.Config;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.sandwich.mobtowers.particle.ModParticles;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends FlyingMob implements Enemy {
	
	protected PhantomMixin(EntityType<? extends FlyingMob> type, Level level) {
		super(type, level);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void afterInit(EntityType<? extends FlyingMob> entityType, Level level, CallbackInfo ci) {
		this.noPhysics = true;
	}

	@Unique private int dryOutTick;

	@WrapOperation(
		method = "aiStep",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/Phantom;isSunBurnTick()Z"
		)
	)
	private boolean ignoreBurning(Phantom instance, Operation<Boolean> original) {
		return false;
	}

	@Inject(method = "aiStep", at = @At("TAIL"))
	public void afterAiStep(CallbackInfo ci) {
		if (!Config.PHANTOMS_DIE_WITHOUT_HEART.isTrue()) return;

		if (this.dryOutTick++ <= 100) return;
		this.dryOutTick = 0;

		if (this.level() instanceof ServerLevel level && !MobRegion.isMobRegionEnabled(this.chunkPosition(), level)) {
			this.dryOutTick = 80;
			this.hurt(damageSources().dryOut(), 2);
			level.sendParticles(ModParticles.TOWER_FLAME.get(),
				this.getX(), this.getY(), this.getZ(), 15,
				0.5F, 0.5F, 0.5F, 0
			);
		}
	}

}