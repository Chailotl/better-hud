package com.chailotl.better_hud;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ClientModInitializer
{
	public static final String MOD_ID = "better_hud";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final com.chailotl.better_hud.BetterHudConfig CONFIG = com.chailotl.better_hud.BetterHudConfig.createAndLoad();

	@Override
	public void onInitializeClient()
	{
		//LOGGER.info("Hello Fabric world!");
	}

	public static boolean shouldHide(ClientPlayerEntity player)
	{
		return player.isCreative() || player.isSpectator();
	}
}