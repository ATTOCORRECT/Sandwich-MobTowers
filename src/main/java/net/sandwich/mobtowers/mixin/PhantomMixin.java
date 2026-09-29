package net.sandwich.mobtowers.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;

import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Phantom;

import net.minecraft.world.level.Level;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.sandwich.mobtowers.particle.ModParticles;
import net.sandwich.mobtowers.voronoi.Voronoi;




@Mixin(Phantom.class)
public abstract class PhantomMixin extends FlyingMob implements Enemy {


	
	protected PhantomMixin(EntityType<? extends FlyingMob> entityType, Level level) {
		super(entityType, level);
	}


	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void tick(CallbackInfo ci) {
		this.noPhysics = true;
	}

	private int checkTick;


	@Overwrite 
	public void aiStep() {
		if (this.isAlive() && this.isSunBurnTick()) {
			
		}

		checkTick++;
		if (checkTick > 100) {
			checkTick = 0;
			if (!this.level().isClientSide()) {
				ServerLevel serverLevel = (ServerLevel)this.level();
				if (!MobRegion.isMobRegionEnabled(this.chunkPosition(), serverLevel)) {
					this.hurt(damageSources().dryOut(), 2);

					checkTick = 80;

					for (int p=0; p < serverLevel.players().size(); p++)
						serverLevel.sendParticles(serverLevel.players().get(p), ModParticles.TOWER_FLAME.get(), true, this.getX(), this.getY(), this.getZ(), 15, 0.5f, 0.5f, 0.5f, 0.0f);
				}
			}

		}


		super.aiStep();
	}
}