package net.sandwich.mobtowers.particle.custom;

import java.awt.Color;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.sandwich.mobtowers.voronoi.CellCenter;
import net.sandwich.mobtowers.Utils;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.minecraft.util.Mth;

public class VanillaSpawnerFlameParticle extends TextureSheetParticle {

	private final SpriteSet allSprites;
	private final float sizeStart;

	protected VanillaSpawnerFlameParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z);

		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;

		this.quadSize *= (Mth.lerp(Math.random(), 0.6, 1.7));
		this.friction = 0.0f;
		this.lifetime = (int)(Mth.lerp(Math.random(), 15, 35));
		this.allSprites = spriteSet;
		this.setSpriteFromAge(this.allSprites);
		this.sizeStart = this.quadSize;

		this.tick();
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime)
			this.remove();

		float lifeAge = (float)this.age / (float)this.lifetime;
		lifeAge = (float)Math.pow(lifeAge, 2);
		this.quadSize = Mth.lerp(lifeAge, sizeStart, sizeStart * 0.1f);
		


		this.setSpriteFromAge(this.allSprites);

	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public int getLightColor(float partialTick) {
		return 255;
	}




	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Provider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		@Nullable
		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) 
		{
			return new VanillaSpawnerFlameParticle(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
		}
	}

}
