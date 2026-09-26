package net.sandwich.mobtowers.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;




@Mixin(InBedChatScreen.class)
public abstract class InBedMixin extends ChatScreen {

	public InBedMixin(String initial) {
		super(initial);
	}

	@Shadow 
	private Button leaveBedButton;

	@Shadow 
	private void sendWakeUp() {}

	@Overwrite 
	protected void init() {
		super.init();
		this.leaveBedButton = Button.builder(Component.translatable("multiplayer.stopSleeping"), (p_96074_) -> this.sendWakeUp()).bounds(this.width / 2 - 50, this.height - 60, 100, 20).build();
		this.addRenderableWidget(this.leaveBedButton);
	}

}