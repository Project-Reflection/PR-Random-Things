package prrandomthings.jumpscare;

import gregtech.core.sound.GTSoundEvents;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;

import javax.annotation.concurrent.Immutable;
import java.util.HashSet;
import java.util.Set;

@Immutable
public class JumpscareEffect {
    public static final Set<JumpscareEffect> effects=new HashSet<>();

    public static final JumpscareEffect BLINDNESS=new JumpscareEffect(MobEffects.BLINDNESS);
    public static final JumpscareEffect POISON=new JumpscareEffect(MobEffects.POISON);
    public static final JumpscareEffect NAUSEA=new JumpscareEffect(MobEffects.NAUSEA);
    public static final JumpscareEffect WITHER=new JumpscareEffect(MobEffects.WITHER,SoundEvents.ENTITY_WITHER_SPAWN)
    {
        @Override
        protected void applyPotion(EntityPlayerMP playerMP, int lvl, int duration) {
            int maxDuration=switch (lvl){
                case 0->40*20;
                case 1->20*20;
                default -> 10*20;
            };
            super.applyPotion(playerMP, lvl, Math.min(duration,maxDuration));
        }
    };
    public static final JumpscareEffect HUNGER=new JumpscareEffect(MobEffects.HUNGER);
    public static final JumpscareEffect SLOWNESS=new JumpscareEffect(MobEffects.SLOWNESS);
    public static final JumpscareEffect WEAKNESS=new JumpscareEffect(MobEffects.WEAKNESS);
    public static final JumpscareEffect GUARDIAN=new JumpscareEffect(MobEffects.MINING_FATIGUE,
            SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE){
        protected void playSound(EntityPlayerMP playerMP)
        {
            playerMP.connection.sendPacket(new SPacketChangeGameState(10, 0.0F));
        }
    };
    public static final JumpscareEffect WRENCH=new JumpscareEffect(null, GTSoundEvents.WRENCH_TOOL);
    public static final JumpscareEffect BREAKDOWN=new JumpscareEffect(null, GTSoundEvents.BREAKDOWN_ELECTRICAL);
    public static final JumpscareEffect ALARM=new JumpscareEffect(null, GTSoundEvents.DEFAULT_ALARM);
    public static final JumpscareEffect HISS=new JumpscareEffect(null, SoundEvents.ENTITY_CREEPER_PRIMED);
    public static final JumpscareEffect BURN=new JumpscareEffect(null, SoundEvents.ENTITY_BLAZE_SHOOT){
        protected void applyPotion(EntityPlayerMP playerMP,int lvl,int duration){
            playerMP.setFire(duration / 20);
        }
    };

    private final Potion potion;
    private final SoundEvent sound;
    protected JumpscareEffect(Potion potion, SoundEvent sound){
        this.potion=potion;
        this.sound=sound;
        effects.add(this);
    }
    private JumpscareEffect(Potion potion)
    {
        this(potion,SoundEvents.ENTITY_SPLASH_POTION_BREAK);
    }
    protected void applyPotion(EntityPlayerMP playerMP,int lvl,int duration)
    {
        if(this.potion != null)
        {
            playerMP.addPotionEffect(new PotionEffect(potion,
                    duration,lvl));
        }
    }
    protected void playSound(EntityPlayerMP playerMP)
    {
        if(sound != null) {
            JumpscareHandler.playSoundTo(playerMP, this.sound, 1.0);
        }
    }
    public void applyOn(EntityPlayerMP playerMP,int lvl,int duration)
    {
        if(playerMP.world.isRemote) return;
        playSound(playerMP);
        applyPotion(playerMP,lvl,Math.max(duration,1));
    }
}
