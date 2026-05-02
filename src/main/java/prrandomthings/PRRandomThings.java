package prrandomthings;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import prrandomthings.constants.RTConstants;
import prrandomthings.events.RegistryEvents;
import prrandomthings.proxy.CommonProxy;
import prrandomthings.utils.PREnvironment;
import twelvefold.twelvefoldbooter.api.LateMixinLoader;

@Mod(modid = RTConstants.MODID, version = RTConstants.VERSION, name = RTConstants.NAME, dependencies = "required-after:twelvefoldbooter;required-after:gregtech;after:botania;")
@SuppressWarnings("unused")
public class PRRandomThings {

    @SidedProxy(clientSide = "prrandomthings.proxy.ClientProxy", serverSide = "prrandomthings.proxy.CommonProxy")
    public static CommonProxy PROXY;
	
	@Instance(RTConstants.MODID)
	public static PRRandomThings instance;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RegistryEvents.init();
        PRRandomThings.PROXY.preInit();
    }
}