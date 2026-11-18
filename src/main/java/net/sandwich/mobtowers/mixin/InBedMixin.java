package net.sandwich.mobtowers.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.network.chat.Component;

@Mixin(InBedChatScreen.class)
public abstract class InBedMixin extends ChatScreen {

	public InBedMixin(String initial) {
		super(initial);
	}

	@Shadow private Button leaveBedButton;

	@Shadow private void sendWakeUp() {}

	@Redirect(
        method = "init",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screens/InBedChatScreen;leaveBedButton:Lnet/minecraft/client/gui/components/Button;",
            opcode = Opcodes.PUTFIELD
        )
	)
	private void replaceLeaveBedButton(InBedChatScreen instance, Button value) {
		this.leaveBedButton = Button.builder(Component.translatable("multiplayer.stopSleeping"), (p_96074_) -> this.sendWakeUp()).bounds(this.width / 2 - 50, this.height - 60, 100, 20).build();
	}

}
