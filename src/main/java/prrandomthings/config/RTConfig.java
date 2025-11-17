package prrandomthings.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prrandomthings.PRConstants;

@Config(modid = PRConstants.MODID)
public class RTConfig {
    public static int startMetaTileEntityID=11000;
    public static int startMaterialID=24000;
	@Mod.EventBusSubscriber(modid = PRConstants.MODID)
	private static class EventHandler{
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(PRConstants.MODID)) {
				ConfigManager.sync(PRConstants.MODID, Config.Type.INSTANCE);
			}
		}
	}
}