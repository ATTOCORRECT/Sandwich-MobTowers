package net.sandwich.mobtowers.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;



@Mixin(BedBlock.class)
public abstract class BedMixin extends HorizontalDirectionalBlock {

	protected BedMixin(Properties properties) {
		super(properties);
	}

	@Shadow 
	protected abstract MapCodec<? extends HorizontalDirectionalBlock> codec();
	
	@Shadow
	public static EnumProperty<BedPart> PART;
	@Shadow 
	public static BooleanProperty OCCUPIED;

	@Shadow 
	public static boolean canSetSpawn(Level level) {
		return true;
	}

	@Shadow
	private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
		return true;
	}


	@SuppressWarnings("null")
	@Overwrite	
		protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
			if (level.isClientSide) {
				return InteractionResult.CONSUME;
			} else {
				if (state.getValue(PART) != BedPart.HEAD) {
					pos = pos.relative((Direction)state.getValue(FACING));
					state = level.getBlockState(pos);
					if (!state.is((BedBlock)(Object)this)) {
					return InteractionResult.CONSUME;
					}
				}

				if (!canSetSpawn(level)) {
					level.removeBlock(pos, false);
					BlockPos blockpos = pos.relative(((Direction)state.getValue(FACING)).getOpposite());
					if (level.getBlockState(blockpos).is((BedBlock)(Object)this)) {
					level.removeBlock(blockpos, false);
					}

					Vec3 vec3 = pos.getCenter();
					level.explode((Entity)null, level.damageSources().badRespawnPointExplosion(vec3), (ExplosionDamageCalculator)null, vec3, 5.0F, true, ExplosionInteraction.BLOCK);
					return InteractionResult.SUCCESS;
				} else if ((Boolean)state.getValue(OCCUPIED)) {
					if (!this.kickVillagerOutOfBed(level, pos)) {
					player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
					}

					return InteractionResult.SUCCESS;
				} else {
					ServerPlayer serverplayer = (ServerPlayer)player;

					// Either<BedSleepingProblem, Unit> result = player.startSleepInBed(pos);
					// if (result.left().get() != null) {
					// 	BedSleepingProblem problem = result.left().get();
					// 	if (problem == BedSleepingProblem.NOT_SAFE) {
					// 		player.displayClientMessage(Component.translatable("block.minecraft.bed.not_safe"), true);
					// 		return InteractionResult.FAIL;
					// 	} else {
					// 		player.startSleeping(pos);
					// 	}
					// }
					player.startSleeping(pos);
					serverplayer.setRespawnPosition(level.dimension(), pos, 0.0F, false, true);

					return InteractionResult.SUCCESS;
				}
			}
		}

}