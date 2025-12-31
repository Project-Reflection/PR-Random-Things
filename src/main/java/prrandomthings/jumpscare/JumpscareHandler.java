package prrandomthings.jumpscare;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import prrandomthings.constants.RTConstants;
import prrandomthings.config.RTConfig;

import java.util.*;

public class JumpscareHandler {
    private static final double A;
    static {
        A=Math.log(RTConfig.jumpscareInterval)-0.5;
    }
    private static Map<EntityPlayerMP, JumpscareHandler> map=new HashMap<>();
    private int jumpscareTimer;
    private EntityPlayerMP player;

    private JumpscareHandler(EntityPlayerMP player)
    {
        this.player=player;
        reload();
    }
    private void reload()
    {
        this.jumpscareTimer=(int)Math.round(Math.exp(A+RTConstants.generalRandom.nextGaussian()));
    }
    public static JumpscareHandler fromPlayer(EntityPlayerMP player)
    {
        if(map.containsKey(player))
        {
            return map.get(player);
        }
        JumpscareHandler utils=new JumpscareHandler(player);
        map.put(player,utils);
        return utils;
    }
    public void tick()
    {
        if(jumpscareTimer > 0){
            jumpscareTimer--;
        }
    }
    public static void playSoundTo(EntityPlayerMP player, SoundEvent sound,double pitch)
    {
        double a1=Math.log(pitch)-0.5;
        player.connection.sendPacket(new SPacketSoundEffect(sound,
                SoundCategory.MASTER, player.posX, player.posY, player.posZ, Float.MAX_VALUE, (float) Math.exp(a1+RTConstants.generalRandom.nextGaussian())));
    }
    public void check()
    {
        if(RTConfig.jumpscareInterval <0)
            return;
        if(jumpscareTimer == 0) {
            reload();
            doJumpscare();
        }
    }
    private void doJumpscare()
    {
        ArrayList<JumpscareEffect> availablePotions=new ArrayList<>(JumpscareEffect.effects);
        Collections.shuffle(availablePotions,RTConstants.generalRandom);
        int lvl=RTConstants.generalRandom.nextInt(3);
        int duration=lvl>=2?600:RTConstants.generalRandom.nextInt(jumpscareTimer)+1;
        for (int i = 0; i <=  lvl;i++) {
            JumpscareEffect potion=availablePotions.get(i);
            potion.applyOn(player,lvl,duration);
        }
    }
}
