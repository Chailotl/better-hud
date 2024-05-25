package com.chailotl.better_hud.mixin;

import com.chailotl.better_hud.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ChatHud.class)
public class ModifyChatHud
{
	@Final
	@Shadow
	private MinecraftClient client;

	@ModifyConstant(
		method = "render",
		constant = @Constant(intValue = 40)
	)
	private int offsetChat(int original)
	{
		ClientPlayerEntity player = client.player;
		return original + (player.getArmor() == 0 || Main.shouldHide(player) ? 0 : 10);
	}
}