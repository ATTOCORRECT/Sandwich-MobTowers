package net.sandwich.mobtowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;

import net.sandwich.mobtowers.Config;
import net.sandwich.mobtowers.mobregion.MobRegion;

@Mixin(Monster.class)
public abstract class MonsterSpawnMixin extends PathfinderMob {

	@Shadow
	private static boolean isDarkEnoughToSpawn(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
		return false;
	}

	protected MonsterSpawnMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
		super(entityType, level);
	}

	private static boolean isDarkEnoughForSpawner(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
		if (level.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
			return false;
		} else {
			DimensionType dimensiontype = level.dimensionType();
			int i = dimensiontype.monsterSpawnBlockLightLimit();
			if (i < 15 && level.getBrightness(LightLayer.BLOCK, pos) > i) {
				return false;
			} else {
				int j = level.getLevel().isThundering() ? level.getMaxLocalRawBrightness(pos, 10)
						: level.getMaxLocalRawBrightness(pos);
				return j <= dimensiontype.monsterSpawnLightTest().sample(random);
			}
		}
	}

	@Overwrite
	public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level,
			MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		boolean is_spawner = spawnType == MobSpawnType.SPAWNER;
		boolean is_dark_enough = false;
		if (is_spawner)
			is_dark_enough = isDarkEnoughForSpawner(level, pos, random);
		if (!is_spawner)
			is_dark_enough = isDarkEnoughToSpawn(level, pos, random);
		boolean is_not_peaceful = level.getDifficulty() != Difficulty.PEACEFUL;

		return is_not_peaceful && (MobSpawnType.ignoresLightRequirements(spawnType) || is_dark_enough)
				&& checkMobSpawnRules(type, level, spawnType, pos, random);
	}

	@Inject(
		method = "isDarkEnoughToSpawn", 
		at = @At("HEAD"), 
		cancellable = true
	)
	private static void stopDarkSpawning(ServerLevelAccessor level, BlockPos pos, RandomSource random,
			CallbackInfoReturnable<Boolean> ci) {

		boolean is_in_overworld = level.getLevel().dimension() == Level.OVERWORLD;
		boolean is_above_natural_spawn_depth = pos.getY() > Config.NATURAL_SPAWN_DEPTH.getAsInt();
		if (is_in_overworld && is_above_natural_spawn_depth && !MobRegion.isMobRegionEnabled(pos, level.getLevel())) {
			ci.setReturnValue(false);
			ci.cancel();
		}
	}

}