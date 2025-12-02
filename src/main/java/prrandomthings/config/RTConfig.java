package prrandomthings.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prrandomthings.constants.RTConstants;

@Config(modid = RTConstants.MODID)
public class RTConfig {
    public static int startMetaTileEntityID=11000;
    public static int startMaterialID=24000;
    public static boolean logItems=false;
    @Config.Comment("Default: 5 min, set to negative to disable.")
    public static double jumpscareInterval=20*60*5;

    public static boolean enableExtraFlintTools=true;
	@Mod.EventBusSubscriber(modid = RTConstants.MODID)
	private static class EventHandler{
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(RTConstants.MODID)) {
				ConfigManager.sync(RTConstants.MODID, Config.Type.INSTANCE);
			}
		}
	}
}