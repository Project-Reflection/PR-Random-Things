package prrandomthings.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prrandomthings.RTConstants;
import prrandomthings.config.RTConfig;
import prrandomthings.util.JumpscareUtils;
//import prrandomthings.util.NBTUtils;
import twelvefold.twelvefoldbooter.api.misc.NBTUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Mod.EventBusSubscriber(modid = RTConstants.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event)
    {
        if(RTConfig.logItems && event.getEntityPlayer() == null)
        {
            NBTTagCompound nbt=event.getItemStack().serializeNBT();
            StringBuilder builder=new StringBuilder(nbt.getString("id").replace(':','/'));
            if(builder.toString().isEmpty()){
                builder.append(String.format("{%x}",nbt.toString().hashCode()));
            }
            builder.append(String.format("_{%x}",System.nanoTime()));
            builder.append(".json");

            File file=new File(RTConstants.minecraftHome,builder.toString());
            file.getParentFile().mkdirs();
            try(FileOutputStream stream=new FileOutputStream(file,true))
            {
                stream.write(NBTUtils.nbtToJson(nbt).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event)
    {
        if(event.getEntityPlayer() instanceof EntityPlayerMP playerMP)
        {
            JumpscareUtils.fromPlayer(playerMP).check();
        }
    }
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
    {
        if(event.getEntityLiving() instanceof EntityPlayerMP playerMP)
        {
            JumpscareUtils.fromPlayer(playerMP).tick();
        }
    }
}
