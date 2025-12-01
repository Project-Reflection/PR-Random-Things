package prrandomthings;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Random;

public class RTConstants {
    public static final String MODID = "prrandomthings";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "ProjectReflection Random Things";
    public static final Logger LOGGER = LogManager.getLogger();

    public static File minecraftHome;
    public static final Random generalRandom=new Random();

    public static ResourceLocation RTID(String pathIn)
    {
        return new ResourceLocation(MODID,pathIn);
    }
}