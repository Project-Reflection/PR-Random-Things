package prrandomthings.util;

import gregtech.core.sound.GTSoundEvents;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import prrandomthings.RTConstants;
import prrandomthings.config.RTConfig;

import java.util.*;

public class JumpscareUtils {
    private static final double A;
    static {
        A=Math.log(RTConfig.jumpscareInterval)-0.5;
    }
    private static Map<EntityPlayerMP,JumpscareUtils> map=new HashMap<>();
    private int jumpscareTimer;
    private EntityPlayerMP player;

    private JumpscareUtils(EntityPlayerMP player)
    {
        this.player=player;
        reload();
    }
    private void reload()
    {
        this.jumpscareTimer=(int)Math.round(Math.exp(A+RTConstants.generalRandom.nextGaussian()));
    }
    public static JumpscareUtils fromPlayer(EntityPlayerMP player)
    {
        if(map.containsKey(player))
        {
            return map.get(player);
        }
        JumpscareUtils utils=new JumpscareUtils(player);
        map.put(player,utils);
        return utils;
    }
    public void tick()
    {
        if(jumpscareTimer > 0){
            jumpscareTimer--;
        }
    }
    private static void playSoundTo(EntityPlayerMP player, SoundEvent sound,double pitch)
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
        switch (RTConstants.generalRandom.nextInt(3))
        {
            case 0:{
                boolean elderGuardian=false;
                ArrayList<Potion> availablePotions=new ArrayList<>(Arrays.asList(
                        MobEffects.BLINDNESS,
                        MobEffects.POISON,
                        MobEffects.NAUSEA,
                        MobEffects.WITHER,
                        MobEffects.HUNGER,
                        MobEffects.SLOWNESS,
                        MobEffects.WEAKNESS,
                        MobEffects.MINING_FATIGUE
                ));
                Collections.shuffle(availablePotions,RTConstants.generalRandom);
                int lvl=RTConstants.generalRandom.nextInt(3);
                int duration=lvl==2?600:RTConstants.generalRandom.nextInt(jumpscareTimer);
                for (int i = 0; i <=  lvl;i++) {
                    Potion potion=availablePotions.get(i);
                    if(potion == MobEffects.MINING_FATIGUE)
                    {
                        player.connection.sendPacket(new SPacketChangeGameState(10, 0.0F));
                        elderGuardian=true;
                    }
                    player.addPotionEffect(new PotionEffect(potion,
                            duration,lvl));
                }
                if(!elderGuardian)
                {
                    playSoundTo(player, SoundEvents.ENTITY_SPLASH_POTION_BREAK,1.0);
                }
                break;
            }
            case 1:{
                final SoundEvent[] soundEvents={GTSoundEvents.WRENCH_TOOL,
                    GTSoundEvents.BREAKDOWN_ELECTRICAL,
                    GTSoundEvents.DEFAULT_ALARM,
                    SoundEvents.ENTITY_CREEPER_PRIMED,
                    SoundEvents.ENTITY_WITHER_SPAWN
                };
                playSoundTo(player, soundEvents[RTConstants.generalRandom.nextInt(soundEvents.length)],1.0);
                break;
            }
            case 2:{
                playSoundTo(player, SoundEvents.ENTITY_BLAZE_SHOOT,1.0);
                player.setFire(1+RTConstants.generalRandom.nextInt(5));
            }
        }
    }
}
