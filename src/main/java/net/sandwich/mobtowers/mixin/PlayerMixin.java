package net.sandwich.mobtowers.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;



@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {


	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}



	private int healthCounter = 0;

	@Shadow 
	private int sleepCounter;



	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void tick(CallbackInfo ci) {
		if (this.isSleeping()) {
			healthCounter++;
			if (healthCounter > 50) {
				healthCounter = 0;
				this.heal(1.0f);
				System.out.println("health reached 25");
			}
		}

	}


}