package prrandomthings.events;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prrandomthings.PRRandomThings;
import prrandomthings.constants.RTConstants;
import prrandomthings.config.RTConfig;
import prrandomthings.items.RTMetaItem;
import prrandomthings.jumpscare.JumpscareHandler;
import twelvefold.twelvefoldbooter.api.misc.NBTUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Mod.EventBusSubscriber(modid = RTConstants.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (RTConfig.logItems && event.getEntityPlayer() == null) {
            NBTTagCompound nbt = event.getItemStack().serializeNBT();
            StringBuilder builder = new StringBuilder(nbt.getString("id").replace(':', '/'));
            if (builder.toString().isEmpty()) {
                builder.append(String.format("{%x}", nbt.toString().hashCode()));
            }
            builder.append(String.format("_{%x}", System.nanoTime()));
            builder.append(".json");

            File file = new File(RTConstants.minecraftHome, builder.toString());
            file.getParentFile().mkdirs();
            try (FileOutputStream stream = new FileOutputStream(file, true)) {
                stream.write(NBTUtils.nbtToJson(nbt).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP playerMP && RTConfig.jumpscareInterval > 0) {
            JumpscareHandler.fromPlayer(playerMP).check();
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP playerMP && RTConfig.jumpscareInterval > 0) {
            JumpscareHandler.fromPlayer(playerMP).tick();
        }
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockEvent.HarvestDropsEvent event) {
        if (event.getWorld().isRemote) return;
        IBlockState state = event.getState();
        if (state.getBlock() instanceof BlockTallGrass || (state.getBlock() instanceof BlockDoublePlant && (state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.FERN || state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS))) {
            EntityPlayer player = event.getHarvester();
            ItemStack stack = player.getHeldItemMainhand();

            if (RTConstants.generalRandom.nextFloat() < 0.4) {
                int amount = 1;
                if (!stack.isEmpty() && stack.getItem().getToolClasses(stack).contains("knife")) {
                    amount = 2;
                }
                ItemStack drop = RTMetaItem.PLANT_FIBER.getStackForm(amount);
                event.getDrops().add(drop);

                stack.damageItem(1, player);
                player.setHeldItem(EnumHand.MAIN_HAND, stack); // Necessary because the stack came from IPlayerItem
            }
        }
    }
}
