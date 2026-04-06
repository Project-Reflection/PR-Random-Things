package prrandomthings.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PREnvironment {
    private static PerlinNoise perlinNoise1x;
    private static PerlinNoise perlinNoise3x;
    private static PerlinNoise perlinNoise5x;
    private static PerlinNoise perlinNoise7x;

    public static double globalMinEnv=0.;
    public static double globalMaxEnv=0.;
    public static final double MAX_THEORETICAL = Math.sqrt(3) * (1 + 1/3.0 + 1/5.0 + 1/7.0);
    public static void init(World world) {
        if (perlinNoise1x == null) {
            perlinNoise1x = new PerlinNoise.CauchyPerlinNoise(world.getSeed(), 16.);
            perlinNoise3x = new PerlinNoise.CauchyPerlinNoise(world.getSeed(), 16. / 3.);
            perlinNoise5x = new PerlinNoise.CauchyPerlinNoise(world.getSeed(), 16. / 5.);
            perlinNoise7x = new PerlinNoise.CauchyPerlinNoise(world.getSeed(), 16. / 7.);
        }
    }

    public static double getEnvironmentFactor(World world, BlockPos pos) {
        Vec3d timePos = new Vec3d(pos.getX() + 0.5, world.getTotalWorldTime() / 37.5, pos.getZ() + 0.5);
        double value=perlinNoise1x.getFinalDistribution(timePos)
                + perlinNoise3x.getFinalDistribution(timePos) / 3
                + perlinNoise5x.getFinalDistribution(timePos) / 5
                + perlinNoise7x.getFinalDistribution(timePos) / 7;
        value /= MAX_THEORETICAL;
        if(value > globalMaxEnv){
            globalMaxEnv=value;
        }
        if(value < globalMinEnv){
            globalMinEnv=value;
        }
        return value;
    }
}