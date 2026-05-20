package net.sandwich.mobtowers.particle.custom;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sandwich.mobtowers.voronoi.CellCenter;
import net.sandwich.mobtowers.voronoi.Voronoi;
import net.minecraft.util.Mth;

public class TowerFlameParticle extends TextureSheetParticle {


	private final double xStart;
	private final double yStart;
	private final double zStart;
	private final CellCenter c;

	protected TowerFlameParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z);

		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;

		this.xStart = this.x;
		this.yStart = this.y;
		this.zStart = this.z;
	

		c = Voronoi.getVoronoiCellCenter(new BlockPos((int)x, (int)y, (int)z));

		this.quadSize *= (Mth.lerp(Math.random(), 0.6, 1.7));
		this.friction = 0.0f;
		this.lifetime = (int)(Mth.lerp(Math.random(), 15, 35));
		this.setSpriteFromAge(spriteSet);
		this.y = this.age / 25;


	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			float f = (float)this.age / (float)this.lifetime;
			f = (float)Math.pow(f, 3);
			f = f * 0.05f;

			double cx = c.x * 16;
			double cy = this.yStart + 60;
			double cz = c.z * 16;
			this.x = Mth.lerp(f, this.xStart, cx);
			this.y = Mth.lerp(f, this.yStart, cy);
			this.z = Mth.lerp(f, this.zStart, cz);
		}

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
			return new TowerFlameParticle(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
		}
	}

}
