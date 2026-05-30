package net.sandwich.mobtowers.block.entity.MonsterFlame;

import org.joml.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sandwich.mobtowers.block.custom.MonsterFlame;
import net.sandwich.mobtowers.block.entity.ModBlockEntities;
import net.sandwich.mobtowers.Utils;

public class MonsterFlameEntity extends BlockEntity {

	public static final int openingTime = 20;
	public static final int shakingTime = 20;
	public static final int closingTime = 10;
	public static final int reverberateTime = 5;
	private static final float pitchAngle = (float)Math.PI / 2; // 90 deg
	private static final float openDistance = 0.5f;
	private static final float spinSpeed = 0.1f;
	private static final float spinDecay = 0.2f;
	private static final float shakeDistance = 0.5f;
	public int animationTime = 0;
	public MonsterFlameAnimationState animationState = MonsterFlameAnimationState.PLAYING;
	public float open = 1;
	public float openOld = 1;
	public float pitch = 1;
	public float pitchOld = 1;
	public float spin = 0;
	public float spinOld = 0;
	public Vector3f shake = new Vector3f(0,0,0);
	public Vector3f shakeOld = new Vector3f(0,0,0);
	private int timeChangedStates = 0;
	private boolean hasLoaded = false;

	public MonsterFlameEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MONSTER_FLAME_BE.get(), pos, blockState);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, MonsterFlameEntity blockEntity) {
		blockEntity.updateAnimation(level, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, MonsterFlameEntity blockEntity) {
		blockEntity.updateAnimation(level, state);
	}

	private void updateAnimation(Level level, BlockState state) {
		if (!hasLoaded) {
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

				pitchOld = pitch;
				pitch = Utils.easing(0, 1, t) * pitchAngle;
				
				shakeOld = shake;
				shake = new Vector3f(0,0,0);

				if (t >= 1) setAnimationState(MonsterFlameAnimationState.PLAYING);
				break;

			case MonsterFlameAnimationState.PLAYING:
				animationTime += 1;

				spinOld = spin;
				spin = Utils.lerpAngle(spin, getTargetAngle(), spinDecay);

				openOld = open;
				open = openDistance;

				pitchOld = pitch;
				pitch = pitchAngle;
				
				shakeOld = shake;
				shake = new Vector3f(0,0,0);
				break;

			case MonsterFlameAnimationState.SHAKING:
				animationTime += 1;

				t = getT(shakingTime);

				spinOld = spin;
				spin = Utils.lerpAngle(spin, 0, spinDecay);

				openOld = open;
				open = openDistance;

				pitchOld = pitch;
				pitch = pitchAngle;
				
				shakeOld = shake;
				shake = getShake(level.getRandom()).mul(Utils.easing(0, 0, 1 - t));

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
				pitch = Utils.easing(0, 0, 1 - t) * pitchAngle;
				
				shakeOld = shake;
				shake = new Vector3f(0,0,0);

				if (t >= 1) setAnimationState(MonsterFlameAnimationState.REVERBERATING);
				break;

			case MonsterFlameAnimationState.REVERBERATING:
				animationTime += 1;

				t = getT(reverberateTime);

				spinOld = spin;
				spin = 0;

				openOld = open;
				open = 0;

				pitchOld = pitch;
				pitch = 0;
				
				shakeOld = shake;
				shake = getShake(level.getRandom()).mul(Utils.easing(0, 0, 1 - t));

				if (t >= 1) setAnimationState(MonsterFlameAnimationState.STOPPED);
				break;

			case MonsterFlameAnimationState.STOPPED:
				spinOld = spin;
				spin = 0;

				openOld = open;
				open = 0;

				pitchOld = pitch;
				pitch = 0;
				
				shakeOld = shake;
				shake = new Vector3f(0,0,0);
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

	private Vector3f getShake(RandomSource randomSource) {
		return new Vector3f(
			(randomSource.nextFloat() * 2 - 1) * shakeDistance,
			(randomSource.nextFloat() * 2 - 1) * shakeDistance,
			(randomSource.nextFloat() * 2 - 1) * shakeDistance
		);
	}

	private void setAnimationState(MonsterFlameAnimationState animationState) {
		timeChangedStates = animationTime;
		this.animationState = animationState;
	}

}
