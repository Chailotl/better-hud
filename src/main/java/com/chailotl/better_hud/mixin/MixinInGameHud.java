package com.chailotl.better_hud.mixin;

import com.chailotl.better_hud.ConfigModel;
import com.chailotl.better_hud.Main;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud
{
	@Final
	@Shadow
	private MinecraftClient client;
	@Shadow
	public abstract TextRenderer getTextRenderer();
	@Shadow
	public abstract void renderExperienceBar(DrawContext context, int x);
	@Shadow
	protected abstract int getHeartCount(LivingEntity entity);

	@Shadow @Final private LayeredDrawer layeredDrawer;

	@Inject(
		method = "<init>",
		at = @At(value = "TAIL")
	)
	private void addLayerDrawer(MinecraftClient client, CallbackInfo ci)
	{
		layeredDrawer.addSubDrawer(new LayeredDrawer().addLayer(this::renderArmorDurability), () -> !client.options.hudHidden);
	}

	@Unique
	private void renderArmorDurability(DrawContext context, RenderTickCounter tickCounter)
	{
		if (Main.CONFIG.miniDurabilityBars() == ConfigModel.MiniDurabilityBarsPosition.NONE) { return; }

		if (Main.shouldHide(client.player)) { return; }

		int x = client.getWindow().getScaledWidth() / 2 - 9;
		int y = client.getWindow().getScaledHeight() - 50;

		if (Main.CONFIG.miniDurabilityBars() == ConfigModel.MiniDurabilityBarsPosition.LEFT)
		{
			x = client.getWindow().getScaledWidth() / 2 - 108;
			y = client.getWindow().getScaledHeight() - 51;
		}

		for (ItemStack armor : client.player.getArmorItems())
		{
			y -= 3;
			if (armor == null || armor.isEmpty() || !armor.isDamageable())
			{
				continue;
			}

			int i = armor.getItemBarStep();
			int j = armor.getItemBarColor();
			int k = x + 2;
			int l = y + 13;

			context.fill(RenderLayer.getGuiOverlay(), k, l, k + 13, l + 2, -16777216);
			context.fill(RenderLayer.getGuiOverlay(), k, l, k + i, l + 1, j | 0xFF000000);
		}
	}

	@Unique
	private boolean shouldRenderXpBar()
	{
		return client.player.getMountJumpStrength() == 0 &&
			!client.player.isCreative() &&
			client.interactionManager.hasExperienceBar();
	}

	@Inject(
		method = "renderMountJumpBar",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	private void showJumpBarWhenJumping(JumpingMount mount, DrawContext context, int x, CallbackInfo ci)
	{
		if (!Main.CONFIG.showXpBarWhenMounted()) { return; }

		if (shouldRenderXpBar())
		{
			renderExperienceBar(context, x);
			ci.cancel();
		}
	}

	@Inject(
			method = "shouldRenderExperience",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	private void fixXpLevel(CallbackInfoReturnable<Boolean> cir)
	{
		if (!Main.CONFIG.showXpBarWhenMounted()) { return; }

		if (shouldRenderXpBar())
		{
			cir.setReturnValue(true);
		}
	}

	@Redirect(
		method = "renderStatusBars",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"
		)
	)
	private int showHungerBarWhenMounted(InGameHud hud, LivingEntity entity)
	{
		if (!Main.CONFIG.showHungerBarWhenMounted())
		{
			return getHeartCount(entity);
		}

		return 0;
	}

	@ModifyConstant(
		method = "renderMountHealth",
		constant = @Constant(intValue = 39)
	)
	private int raiseMountHealthHeight(int original)
	{
		if (!Main.CONFIG.showHungerBarWhenMounted()) { return original; }

		return original + (client.player != null && client.player.isCreative() ? 0 : 10);
	}

	@Unique
	private String ticksToTime(int ticks)
	{
		int seconds = ticks / 20;
		int minutes = seconds / 60;
		seconds = seconds % 60;
		if (minutes == 0)
		{
			return seconds + "";
		}
		return minutes + ":" + String.format("%2s", seconds).replace(' ', '0');
	}

	@Inject(
		method = "renderStatusEffectOverlay",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/texture/StatusEffectSpriteManager;getSprite(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/client/texture/Sprite;"
		)
	)
	private void drawText(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local StatusEffectInstance effect, @Local(ordinal = 2) int x, @Local(ordinal = 3) int y)
	{
		if (!Main.CONFIG.statusEffectIconTimers() || effect.isInfinite()) { return; }

		context.drawCenteredTextWithShadow(getTextRenderer(), ticksToTime(effect.getDuration()), x + 12, y + 25, Colors.GRAY);
	}
}