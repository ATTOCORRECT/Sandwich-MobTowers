package net.sandwich.mobtowers.block.entity.MonsterFlame;

import org.joml.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sandwich.mobtowers.block.custom.MonsterFlame;
import net.sandwich.mobtowers.block.entity.ModBlockEntities;
import net.sandwich.mobtowers.mobregion.MobRegion;
import net.sandwich.mobtowers.particle.ModParticles;
import net.sandwich.mobtowers.sound.ModSounds;
import net.sandwich.mobtowers.Utils;
import net.minecraft.world.entity.player.Player;

public class MonsterFlameEntity extends BlockEntity {

	public static final int openingTime = 20;
	public static final int shakingTime = 20;
	public static final int closingTime = 10;
	public static final int reverberateTime = 10;
	private static final float pitchAngle = (float)Math.PI / 2; // 90 deg
	private static final float openDistance = 0.5f;
	private static final float spinSpeed = 0.1f;
	private static final float spinDecay = 0.2f;
	private static final float shakeDistance = 0.15f;
	public int tintColor = 0;
	public int animationTime = 0;
	public MonsterFlameAnimationState animationState = MonsterFlameAnimationState.PLAYING;
	public float open = 1;
	public float openOld = 1;
	public float pitch = 1;
	public float pitchOld = 1;
	public float spin = 0;
	public float spinOld = 0;
	public float shakeAmountOld;
	public float shakeAmount;
	public float flameSize = 1;
	public float flameSizeOld = 1;
	private int timeChangedStates = 0;
	private boolean hasLoaded = false;

	public MonsterFlameEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MONSTER_FLAME_BE.get(), pos, blockState);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, MonsterFlameEntity blockEntity) {
		blockEntity.updateAnimation(level, pos, state);

		if (level.getGameTime() % 20L == 0L && (Boolean)state.getValue(MonsterFlame.LIT)) 
			blockEntity.renderAmbientFlameParticle(level, pos);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, MonsterFlameEntity blockEntity) {
		blockEntity.updateAnimation(level, pos, state);

		RandomSource randomsource = level.getRandom();
		if (level.getGameTime() % 80L == 0L && (Boolean)state.getValue(MonsterFlame.LIT)) 
			level.playSound((Player)null, (double)pos.getX()+.5, (double)pos.getY()+.5, (double)pos.getZ()+.5, ModSounds.MONSTER_FLAME_GROWL.get(), SoundSource.BLOCKS, 2f, (float)Mth.lerp(randomsource.nextFloat(), 0.8, 1.2));
	}

	private void updateAnimation(Level level, BlockPos pos, BlockState state) {
		if ((Boolean)state.getValue(MonsterFlame.LIT) && animationState == MonsterFlameAnimationState.STOPPED) {
			playStartAnimation();
		}

		if (!(Boolean)state.getValue(MonsterFlame.LIT) && animationState == MonsterFlameAnimationState.PLAYING) {
			playStopAnimation();
		}

		if (!hasLoaded) {
			tintColor = MobRegion.getMobRegionColor(pos);
			if (!(Boolean)state.getValue(MonsterFlame.LIT)) {
				setAnimationState(MonsterFlameAnimationState.STOPPED);
			}
			hasLoaded = true;
		}
		
		float t;
		switch (animationState) {
			case MonsterFlameAnimationState.STARTING:
				animationTime += 1;

				t = getT(openingTime);

				spinOld = spin;
				spin = Utils.lerpAngle(spin, getTargetAngle(), spinDecay);

				openOld = open;
				open = Utils.easing(2, 1, t) * openDistance;

				flameSizeOld = flameSize;
				flameSize = Utils.easing(1, 1, t);

				pitchOld = pitch;
				pitch = Utils.easing(0, 1, t) * pitchAngle;

				shakeAmountOld = shakeAmount;
				shakeAmount = 0;

				if (t >= 1) setAnimationState(MonsterFlameAnimationState.PLAYING);
				break;

			case MonsterFlameAnimationState.PLAYING:
				animationTime += 1;

				spinOld = spin;
				spin = Utils.lerpAngle(spin, getTargetAngle(), spinDecay);

				openOld = open;
				open = openDistance;

				flameSizeOld = flameSize;
				flameSize = 1;

				pitchOld = pitch;
				pitch = pitchAngle;

				shakeAmountOld = shakeAmount;
				shakeAmount = 0;

				break;

			case MonsterFlameAnimationState.SHAKING:
				animationTime += 1;

				t = getT(shakingTime);

				spinOld = spin;
				spin = Utils.lerpAngle(spin, 0, spinDecay);

				openOld = open;
				open = openDistance;

				flameSizeOld = flameSize;
				flameSize = 1;

				pitchOld = pitch;
				pitch = pitchAngle;

				shakeAmountOld = shakeAmount;
				shakeAmount = Utils.easing(0, 0, 1 - t) * shakeDistance;

				if (t >= 1) setAnimationState(MonsterFlameAnimationState.STOPPING);
				break;

			case MonsterFlameAnimationState.STOPPING:
				animationTime += 1;

				t = getT(closingTime);

				spinOld = spin;
				spin = Utils.lerpAngle(spin, 0, spinDecay);

				openOld = open;
				open = Utils.easing(3, 1, 1 - t) * openDistance;

				pitchOld = pitch;
				pitch = Utils.easing(1, 1, 1 - t) * pitchAngle;

				flameSizeOld = flameSize;
				flameSize = Utils.easing(1, 1, 1 - t);

				shakeAmountOld = shakeAmount;
				shakeAmount = 0;

				if (t >= 1) {
					setAnimationState(MonsterFlameAnimationState.REVERBERATING);
					renderSmokeParticle(level, pos);
				}
				break;

			case MonsterFlameAnimationState.REVERBERATING:
				animationTime += 1;

				t = getT(reverberateTime);

				spinOld = spin;
				spin = 0;

				openOld = open;
				open = 0;

				flameSizeOld = flameSize;
				flameSize = 0;

				pitchOld = pitch;
				pitch = 0;

				shakeAmountOld = shakeAmount;
				shakeAmount = Utils.easing(0, 0, 1 - t) * shakeDistance;

				if (t >= 1) setAnimationState(MonsterFlameAnimationState.STOPPED);
				break;

			case MonsterFlameAnimationState.STOPPED:
				spinOld = spin;
				spin = 0;

				openOld = open;
				open = 0;

				flameSizeOld = flameSize;
				flameSize = 0;

				pitchOld = pitch;
				pitch = 0;

				shakeAmountOld = shakeAmount;
				shakeAmount = 0;
				break;
		}
	}

	public void playStopAnimation() {
		setAnimationState(MonsterFlameAnimationState.SHAKING);
	}

	public void playStartAnimation() {
		animationTime = 0;
		setAnimationState(MonsterFlameAnimationState.STARTING);
	}

	private float getT(float transitionLength) {
		return Math.clamp((float)(animationTime - timeChangedStates) / transitionLength, 0, 1);
	}

	private float getTargetAngle() {
		return (animationTime * spinSpeed) % (2 * (float)Math.PI);
	}

	private void setAnimationState(MonsterFlameAnimationState animationState) {
		timeChangedStates = animationTime;
		this.animationState = animationState;
	}

	private void renderAmbientFlameParticle(Level level, BlockPos pos) {
		for (int i = 0; i < 5; i++) {
			RandomSource randomsource = level.getRandom();
			double d0 = (double)pos.getX() + randomsource.nextDouble();
			double d1 = (double)pos.getY() + randomsource.nextDouble();
			double d2 = (double)pos.getZ() + randomsource.nextDouble();
			level.addParticle(ModParticles.TOWER_FLAME.get(), true, d0, d1, d2, 0.0, 0.0, 0.0);
		}
	}

	private void renderSmokeParticle(Level level, BlockPos pos) {
		for (int i = 0; i < 20; i++) {
			RandomSource randomsource = level.getRandom();
			double d0 = (double)pos.getX() + randomsource.nextDouble();
			double d1 = (double)pos.getY() + 0.5;
			double d2 = (double)pos.getZ() + randomsource.nextDouble();

			double sX = (randomsource.nextDouble()-0.5) * 0.5;
			double sZ = (randomsource.nextDouble()-0.5) * 0.5;
			level.addParticle(ParticleTypes.LARGE_SMOKE, true, d0, d1, d2, sX, 0, sZ);
		}
	}

}
