package com.chailotl.better_hud.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class InjectGameRenderer
{
	@Inject(
		method = "getNightVisionStrength",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void removeEpilepsy(LivingEntity livingEntity, float tickDelta, CallbackInfoReturnable<Float> info)
	{
		float f = livingEntity.getStatusEffect(StatusEffects.NIGHT_VISION).getDuration() - tickDelta;
		info.setReturnValue(f > 200f ? 1f : f / 200f);
	}
}