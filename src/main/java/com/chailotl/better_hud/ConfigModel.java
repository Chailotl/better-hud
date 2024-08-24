package com.chailotl.better_hud;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = Main.MOD_ID)
@Config(name = Main.MOD_ID, wrapperName = "BetterHudConfig")
public class ConfigModel
{
	public MiniDurabilityBarsPosition miniDurabilityBars = MiniDurabilityBarsPosition.CENTER;
	public boolean statusEffectIconTimers = true;
	public boolean showXpBarWhenMounted = true;
	public boolean showHungerBarWhenMounted = true;
	public boolean lowerFireOverlay = true;
	public boolean removeFireOverlayWhenImmune = true;
	public boolean raiseChatWhenWearingArmor = true;
	public boolean creativeModeLavaVision = true;
	public boolean removeNightVisionFlickering = true;

	public enum MiniDurabilityBarsPosition
	{
		NONE,
		CENTER,
		LEFT
	}
}