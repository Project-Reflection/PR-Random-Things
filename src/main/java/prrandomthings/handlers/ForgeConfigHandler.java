package prrandomthings.handlers;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prrandomthings.PRRandomThings;

@Config(modid = PRRandomThings.MODID)
public class ForgeConfigHandler {
	@Mod.EventBusSubscriber(modid = PRRandomThings.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(PRRandomThings.MODID)) {
				ConfigManager.sync(PRRandomThings.MODID, Config.Type.INSTANCE);
			}
		}
	}
}