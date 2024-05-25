package com.chailotl.better_hud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Unique;

public class Main implements ClientModInitializer
{
	public static final String MOD_ID = "better_hud";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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