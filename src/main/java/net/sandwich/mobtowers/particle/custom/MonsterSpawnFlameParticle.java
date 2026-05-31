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

public class MonsterSpawnFlameParticle extends TextureSheetParticle {

	private final SpriteSet allSprites;
	private final double xStart;
	private final double yStart;
	private final double zStart;
	private final CellCenter c;

	protected MonsterSpawnFlameParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z);

		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;

		this.xStart = this.x;
		this.yStart = this.y;
		this.zStart = this.z;
	
		BlockPos originPos = new BlockPos((int)xStart, (int)yStart, (int)zStart);
		c = MobRegion.getMobRegionCell(originPos);

		
		
		Color particleColor = Utils.intToColor(MobRegion.getMobRegionColor(originPos));

		this.rCol = (float)particleColor.getRed() / 255f;
		this.gCol = (float)particleColor.getGreen() / 255f;
		this.bCol = (float)particleColor.getBlue() / 255f;

		this.quadSize *= (Mth.lerp(Math.random(), 0.6, 1.7));
		this.friction = 0.0f;
		this.lifetime = (int)(Mth.lerp(Math.random(), 15, 35));
		this.allSprites = spriteSet;
		this.setSpriteFromAge(this.allSprites);
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
			return new MonsterSpawnFlameParticle(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
		}
	}

}
