package prrandomthings;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import prrandomthings.handlers.ModRegistry;
import prrandomthings.proxy.CommonProxy;
import twelvefold.twelvefoldbooter.api.LateMixinLoader;

@Mod(modid = PRRandomThings.MODID, version = PRRandomThings.VERSION, name = PRRandomThings.NAME, dependencies = "required-after:twelvefoldbooter")
@SuppressWarnings("unused")
@LateMixinLoader(value = "mixins.prrandomthings.late.json",shouldMixinConfigQueue = "shouldMixinConfigQueue")
public class PRRandomThings {
    public static final String MODID = "prrandomthings";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "ProjectReflection Random Things";
    public static final Logger LOGGER = LogManager.getLogger();
	
    @SidedProxy(clientSide = "prrandomthings.proxy.ClientProxy", serverSide = "prrandomthings.proxy.CommonProxy")
    public static CommonProxy PROXY;
	
	@Instance(MODID)
	public static PRRandomThings instance;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModRegistry.init();
        PRRandomThings.PROXY.preInit();
    }
    public static boolean shouldMixinConfigQueue(String mixinConfig)
    {
        if (mixinConfig.equals("mixins.prrandomthings.late.json")) {
            return Loader.isModLoaded("jei");
        }
        return true;
    }
}