package net.sandwich.mobtowers.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.sandwich.mobtowers.particle.ModParticles;

@Mixin(BaseSpawner.class)
public class SpawnerParticleMixin {

	@Shadow
	private double spin;
	@Shadow
	private double oSpin;
	@Shadow
	private boolean isNearPlayer(Level level, BlockPos pos) {
		return true;
	}
	@Shadow
	private Entity displayEntity;
	@Shadow
	private int spawnDelay;


	@SuppressWarnings("null")
	@Inject(method = "clientTick", at = @At("HEAD"), cancellable = true)
		
		private void clientTick (Level level, BlockPos pos, CallbackInfo ci) {
			if (!this.isNearPlayer(level, pos)) {
				this.oSpin = this.spin;
			} else if (this.displayEntity != null) {
				RandomSource randomsource = level.getRandom();
				double d0 = (double)pos.getX() + randomsource.nextDouble();
				double d1 = (double)pos.getY() + randomsource.nextDouble();
				double d2 = (double)pos.getZ() + randomsource.nextDouble();
				level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, (double)0.0F, (double)0.0F, (double)0.0F);
				level.addParticle(ModParticles.VANILLA_SPAWNER_FLAME.get(), d0, d1, d2, (double)0.0F, (double)0.0F, (double)0.0F);
				if (this.spawnDelay > 0) {
					--this.spawnDelay;
				}

				this.oSpin = this.spin;
				this.spin = (this.spin + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % (double)360.0F;
			}
			ci.cancel();
		}
}