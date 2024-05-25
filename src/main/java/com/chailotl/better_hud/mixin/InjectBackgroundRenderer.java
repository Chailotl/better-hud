package com.chailotl.better_hud.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class InjectBackgroundRenderer
{
	@Inject(
		method = "applyFog",
		at = @At("TAIL")
	)
	private static void clearLavaFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci)
	{
		Entity entity = camera.getFocusedEntity();

		if (camera.getSubmersionType() == CameraSubmersionType.LAVA &&
			entity instanceof PlayerEntity &&
			((PlayerEntity) entity).isCreative())
		{
			RenderSystem.setShaderFogStart(-8.0f);
			RenderSystem.setShaderFogEnd(viewDistance * 0.5f);
		}
	}
}