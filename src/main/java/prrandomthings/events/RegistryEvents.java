package prrandomthings.events;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prrandomthings.PRConstants;
import prrandomthings.config.RTConfig;
import prrandomthings.config.recipes.MetallurgicRecipes;
import prrandomthings.config.recipes.RecipeTweaks;
import prrandomthings.materials.RTMaterials;
import prrandomthings.mte.MteCustomGenerator;

@Mod.EventBusSubscriber(modid = PRConstants.MODID)
public class RegistryEvents {

    public static void init() {
        registerMetaTileEntities();
    }

    @SubscribeEvent
    public static void registerItemEvent(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                //TODO register items here
        );
    }

    @SubscribeEvent
    public static void registerRecipeEvent(RegistryEvent.Register<IRecipe> event) {
        RecipeTweaks.register();
        MetallurgicRecipes.register();
    }

    @SubscribeEvent
    public static void registerPotionEvent(RegistryEvent.Register<Potion> event) {
    }

    @SubscribeEvent
    public static void registerPotionTypeEvent(RegistryEvent.Register<PotionType> event) {
    }


    private static void registerMetaTileEntities(){
        int id=RTConfig.startMetaTileEntityID;
        for(MetaTileEntity mte:MteCustomGenerator.METALLURGIC_GENERATORS)
        {
            MetaTileEntities.registerMetaTileEntity(id++,mte);
        }
        for(MetaTileEntity mte:MteCustomGenerator.REACTANT_GENERATORS)
        {
            MetaTileEntities.registerMetaTileEntity(id++,mte);
        }
    }

    @SubscribeEvent
    public static void registerMaterials(MaterialEvent event)
    {
        RTMaterials.register();
    }
}