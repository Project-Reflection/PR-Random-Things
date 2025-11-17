package prrandomthings;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import prrandomthings.events.RegistryEvents;
import prrandomthings.proxy.CommonProxy;
import twelvefold.twelvefoldbooter.api.LateMixinLoader;

@Mod(modid = PRConstants.MODID, version = PRConstants.VERSION, name = PRConstants.NAME, dependencies = "required-after:twelvefoldbooter;required-after:gregtech;")
@SuppressWarnings("unused")
@LateMixinLoader(value = "mixins.prrandomthings.late.json",shouldMixinConfigQueue = "shouldMixinConfigQueue")
public class PRRandomThings {

    @SidedProxy(clientSide = "prrandomthings.proxy.ClientProxy", serverSide = "prrandomthings.proxy.CommonProxy")
    public static CommonProxy PROXY;
	
	@Instance(PRConstants.MODID)
	public static PRRandomThings instance;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RegistryEvents.init();
        PRRandomThings.PROXY.preInit();
    }
    public static boolean shouldMixinConfigQueue(String mixinConfig)
    {
        return true;
    }
}