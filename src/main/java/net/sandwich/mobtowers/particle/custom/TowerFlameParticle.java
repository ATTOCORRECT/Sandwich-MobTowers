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

public class TowerFlameParticle extends TextureSheetParticle {

	private final SpriteSet allSprites;
	private final double xStart;
	private final double yStart;
	private final double zStart;
	private final double xEnd;
	private final double yEnd;
	private final double zEnd;

	protected TowerFlameParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z);

		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;

		this.xStart = this.x;
		this.yStart = this.y;
		this.zStart = this.z;
		this.xEnd = this.xStart + Mth.lerp(Math.random(), -1f, 1f);
		this.yEnd = this.yStart + Mth.lerp(Math.random(), 0.5f, 2f);
		this.zEnd = this.zStart + Mth.lerp(Math.random(), -1f, 1f);
	
		BlockPos originPos = new BlockPos((int)xStart, (int)yStart, (int)zStart);

		
		
		Color particleColor = Utils.intToColor(MobRegion.getMobRegionColor(originPos));

		this.rCol = (float)particleColor.getRed() / 255f;
		this.gCol = (float)particleColor.getGreen() / 255f;
		this.bCol = (float)particleColor.getBlue() / 255f;

		this.quadSize *= (Mth.lerp(Math.random(), 0.6, 1.7));
		this.friction = 0.0f;
		this.lifetime = (int)(Mth.lerp(Math.random(), 15, 35));
		this.allSprites = spriteSet;
		this.setSpriteFromAge(this.allSprites);
		this.tick();
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			float f = (float)this.age / (float)this.lifetime;
			f = (float)Math.pow(f, 0.6f);

			this.x = Mth.lerp(f, this.xStart, this.xEnd);
			this.y = Mth.lerp(f, this.yStart, this.yEnd);
			this.z = Mth.lerp(f, this.zStart, this.zEnd);
		}
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
			return new TowerFlameParticle(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
		}
	}

}
