package net.sandwich.mobtowers.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.sandwich.mobtowers.particle.ModParticles;
@Mixin(LevelRenderer.class)
public class SpawnerSpawnParticleMixin {

	@Shadow
	private ClientLevel level;

	@SuppressWarnings("null")
	@Inject(method = "levelEvent", at = @At("HEAD"), cancellable = true)
		
		public void levelEvent (int type, BlockPos pos, int data, CallbackInfo ci) {
			System.out.println("Event called of type " + type);
			RandomSource randomsource = this.level.random;
			if (type == 2004) {
				System.out.println("2004 called!");
				for(int l = 0; l < 20; ++l) {

				double d6 = (double)pos.getX() + (double)0.5F + (randomsource.nextDouble() - (double)0.5F) * (double)2.0F;
				double d8 = (double)pos.getY() + (double)0.5F + (randomsource.nextDouble() - (double)0.5F) * (double)2.0F;
				double d13 = (double)pos.getZ() + (double)0.5F + (randomsource.nextDouble() - (double)0.5F) * (double)2.0F;
				this.level.addParticle(ModParticles.VANILLA_SPAWNER_FLAME.get(), d6, d8, d13, (double)0.0F, (double)0.0F, (double)0.0F);
				
				}
				ci.cancel();
			}
		}
}