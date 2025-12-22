package prrandomthings.constants;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Random;

public class RTConstants {
    public static final String MODID = "prrandomthings";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "ProjectReflection Random Things";
    public static final Logger LOGGER = LogManager.getLogger();
    public static class Environment {
        public static final boolean botaniaLoaded = Loader.isModLoaded("botania");
        public static final boolean lycaniteLoaded = Loader.isModLoaded("lycanitesmobs");
    }

    public static File minecraftHome;
    public static final Random generalRandom=new Random();

    public static ResourceLocation RTID(String pathIn)
    {
        return new ResourceLocation(MODID,pathIn);
    }
}