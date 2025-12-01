package prrandomthings.events;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import prrandomthings.RTConstants;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = RTConstants.MODID, value = Side.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void modelRegisterEvent(ModelRegistryEvent event) {
        registerModels();//add items here
    }

    private static void registerModels(Item... values) {
        for(Item entry : values) {
            ModelLoader.setCustomModelResourceLocation(entry, 0, new ModelResourceLocation(Objects.requireNonNull(entry.getRegistryName()), "inventory"));
        }
    }
}