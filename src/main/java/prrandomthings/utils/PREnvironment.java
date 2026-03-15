package prrandomthings.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Random;

public class PREnvironment {
    private static final int accuracy=16;
    private static final Random environRandom=new Random();

    private static final MessageDigest digest=DigestUtils.getSha256Digest();

    private static long commonHash(ByteBuffer buf){
        byte[] shaBytes= digest.digest(buf.array());
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (shaBytes[i] & 0xFF);
        }
        return result;
    }

    private static double getNoise(long seed,int[] parameters){
        ByteBuffer byteBuffer=ByteBuffer.allocate(parameters.length*4+8);
        byteBuffer.putLong(seed);
        for(int param:parameters){
            byteBuffer.putInt(param);
        }
        environRandom.setSeed(commonHash(byteBuffer));
        return Math.exp(environRandom.nextGaussian());
    }

    /**
     * @author BigData GLM
     */
    private static double getNoiseDistribution(long seed, int[] low, int[] high, int[] exact) {
        assert low.length == high.length && high.length == exact.length;
        int dim = low.length; // 维度 N
        int totalNoises = 1 << dim; // 2^N 个顶点
        double[] noises = new double[totalNoises];

        // 1. 遍历生成所有顶点的噪声值
        for (int i = 0; i < totalNoises; i++) {
            int[] parameters = new int[dim];
            for (int j = 0; j < dim; j++) {
                // 如果第 j 位是 1，取 high，否则取 low
                parameters[j] = ((i & (1 << j)) != 0) ? high[j] : low[j];
            }
            noises[i] = getNoise(seed, parameters);
        }

        // 2. 计算每个维度的插值权重
        double[] parts = new double[dim];
        for (int j = 0; j < dim; j++) {
            double range = high[j] - low[j];
            // 防止除以0（如果 high 和 low 相同，权重设为0）
            if (range == 0) {
                parts[j] = 0;
            } else {
                parts[j] = (exact[j] - low[j]) / range;
            }
        }

        // 3. 循环降维插值
        // 这是一个“分治”的过程：每一轮循环处理一个维度，将数据量减半
        double[] buffer = noises;
        for (int d = 0; d < dim; d++) {
            int nextLength = buffer.length >> 1; // 这一轮插值后剩下的点数
            double[] nextBuffer = new double[nextLength];

            // 这里的逻辑是：总是配对相邻的两个点 (2*i) 和 (2*i+1)
            // 在二进制表示中，这正好对应了当前维度 d 的 0 和 1 的状态
            for (int i = 0; i < nextLength; i++) {
                double v0 = buffer[2 * i];      // 当前维度 d 为 "low" 的值
                double v1 = buffer[2 * i + 1];  // 当前维度 d 为 "high" 的值
                nextBuffer[i] = cosInterpolate(v0, v1, parts[d]);
            }
            buffer = nextBuffer; // 更新缓冲区，准备处理下一个维度
        }

        // 最终 buffer 里只剩下一个值
        return buffer[0];
    }

    // 记得把插值函数改成 double 版本，精度更高
    private static double cosInterpolate(double l, double h, double partial) {
        double t2 = (1. - Math.cos(partial * Math.PI)) / 2F;
        return (l * (1. - t2) + h * t2);
    }

    public static double getFinalDistribution(long seed, int... params){
        int dim= params.length;
        int[] low=new int[dim];
        int[] high =new int[dim];
        for(int i=0;i<dim;i++){
            low[i]=(int) Math.floor((float) params[i] / accuracy) * accuracy;
            high[i]=low[i]+accuracy;
        }
        return getNoiseDistribution(seed,low,high,params);
    }
    public static double getEnvironmentFactor(World world, BlockPos pos){
        long worldTime=world.getTotalWorldTime();
        int worldTimeMid=(int)((worldTime >> 16L) & 0x7FFFFFFFL);
        return getFinalDistribution(world.getSeed(), pos.getX(), pos.getY(), pos.getZ(),worldTimeMid);
    }
}
