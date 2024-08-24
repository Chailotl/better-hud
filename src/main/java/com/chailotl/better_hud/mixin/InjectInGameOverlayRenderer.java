package com.chailotl.better_hud.mixin;

import com.chailotl.better_hud.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InjectInGameOverlayRenderer
{
	@Inject(
		method = "renderFireOverlay",
		at = @At("HEAD")
	)
	private static void offsetFire(MinecraftClient client, MatrixStack matrices, CallbackInfo ci)
	{
		double offset = 0;

		if (Main.CONFIG.lowerFireOverlay())
		{
			offset = -0.2;
		}

		if (Main.CONFIG.removeFireOverlayWhenImmune() && (client.player.isCreative() ||
			client.player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)))
		{
			offset = -10;
		}
		matrices.translate(0, offset, 0);
	}
}