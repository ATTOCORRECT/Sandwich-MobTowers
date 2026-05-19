package net.sandwich.mobtowers.particle.custom;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class TowerFlameParticle extends TextureSheetParticle {

	protected TowerFlameParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z);

		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;

		this.friction = 0.0f;
		this.lifetime = 25;
		this.setSpriteFromAge(spriteSet);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}



	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Provider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		@Nullable
		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double pX, double pY, double pZ,
				double pXSpeed, double pYSpeed, double pZSpeed) {
			return new TowerFlameParticle(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
		}
	}

}
