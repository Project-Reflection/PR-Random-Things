package prrandomthings.utils;

import com.google.common.hash.Hashing;
import net.minecraft.util.math.Vec3d;

import java.nio.ByteBuffer;
import java.util.Random;

public class PerlinNoise {
    private final double accuracy;
    private final long seed;
    protected static final Random environRandom = new Random();

    public PerlinNoise(long seed, double accuracy) {
        this.seed = seed;
        this.accuracy=accuracy;
    }

    private static long commonHash(ByteBuffer buf) {
        return Hashing.farmHashFingerprint64().hashBytes(buf.array()).asLong();
    }
    protected double getDistributedRandomValue(){
        return environRandom.nextGaussian();
    }
    //计算晶格点梯度
    private Vec3d getGradient(Vec3d position) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        byteBuffer.putLong(seed);
        byteBuffer.putDouble(position.x);
        byteBuffer.putDouble(position.y);
        byteBuffer.putDouble(position.z);
        environRandom.setSeed(commonHash(byteBuffer));
        return new Vec3d(getDistributedRandomValue(),
                getDistributedRandomValue(),
                getDistributedRandomValue())
                .normalize();
    }
    //计算噪声分布
    private double getNoiseDistribution(Vec3d low, Vec3d high, Vec3d exact) {
        Vec3d[] points = new Vec3d[8];
        Vec3d[] gradients = new Vec3d[8];
        double[] values = new double[8];
        //计算噪声向量点积
        for (int i = 0; i < 8; i++) {
            double x = ((i & 1) == 0 ? low : high).x;
            double y = ((i & 2) == 0 ? low : high).y;
            double z = ((i & 4) == 0 ? low : high).z;
            points[i] = new Vec3d(x, y, z);
            gradients[i] = getGradient(points[i]);
            values[i] = gradients[i].dotProduct(points[i].subtract(exact))/accuracy;
        }
        double xPartial = (exact.x - low.x) / (high.x - low.x);
        double yPartial = (exact.y - low.y) / (high.y - low.y);
        double zPartial = (exact.z - low.z) / (high.z - low.z);

        //插值
        double f1 = interpolate(values[0], values[1], xPartial);
        double f2 = interpolate(values[2], values[3], xPartial);
        double f3 = interpolate(values[4], values[5], xPartial);
        double f4 = interpolate(values[6], values[7], xPartial);

        double f5 = interpolate(f1, f2, yPartial);
        double f6 = interpolate(f3, f4, yPartial);
        return interpolate(f5, f6, zPartial);
    }

    protected double interpolate(double l, double h, double t) {
        double fade = t * t * t * (t * (t * 6 - 15) + 10);
        return (l * (1. - fade) + h * fade);
    }

    public double getFinalDistribution(Vec3d pos) {
        double x =Math.floor(pos.x / accuracy) * accuracy;
        double y =Math.floor(pos.y / accuracy) * accuracy;
        double z = Math.floor(pos.z / accuracy) * accuracy;
        Vec3d low = new Vec3d(x, y, z);
        Vec3d high = new Vec3d(x + accuracy, y + accuracy, z + accuracy);
        return getNoiseDistribution(low, high, pos);
    }

    public static class CauchyPerlinNoise extends PerlinNoise{

        public CauchyPerlinNoise(long seed, double accuracy) {
            super(seed, accuracy);
        }

        @Override
        protected double getDistributedRandomValue() {
            double u = environRandom.nextDouble(); // [0,1)
            return Math.tan(Math.PI * (u - 0.5));
        }
    }
}
