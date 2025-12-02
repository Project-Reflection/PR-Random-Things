package prrandomthings.events;

import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.material.event.PostMaterialEvent;
import gregtech.api.unification.stack.ItemMaterialInfo;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prrandomthings.constants.RTConstants;
import prrandomthings.config.RTConfig;
import prrandomthings.recipes.MetallurgicRecipes;
import prrandomthings.recipes.PrimitiveRecipes;
import prrandomthings.materials.RTMaterials;
import prrandomthings.mte.RTMetaTileEntities;

@Mod.EventBusSubscriber(modid = RTConstants.MODID)
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
        PrimitiveRecipes.register();
        MetallurgicRecipes.register();
    }

    @SubscribeEvent
    public static void registerPotionEvent(RegistryEvent.Register<Potion> event) {
    }

    @SubscribeEvent
    public static void registerPotionTypeEvent(RegistryEvent.Register<PotionType> event) {
    }


    private static void registerMetaTileEntities() {
        int id = RTConfig.startMetaTileEntityID;
        for (MetaTileEntity mte : RTMetaTileEntities.METALLURGIC_GENERATORS) {
            MetaTileEntities.registerMetaTileEntity(id++, mte);
        }
        for (MetaTileEntity mte : RTMetaTileEntities.REACTANT_GENERATORS) {
            MetaTileEntities.registerMetaTileEntity(id++, mte);
        }
        MetaTileEntities.registerMetaTileEntity(id++, RTMetaTileEntities.COMPOSTING_BARREL);
        MetaTileEntities.registerMetaTileEntity(id++, RTMetaTileEntities.SIEVE);
    }

    @SubscribeEvent
    public static void registerMaterials(MaterialEvent event) {
        RTMaterials.register();
    }
    @SubscribeEvent
    public static void postRegisterMaterials(PostMaterialEvent event)
    {
        RTMaterials.postRegister();
    }

    @SubscribeEvent
    public static void onMaterialInfo(GregTechAPI.RegisterEvent<ItemMaterialInfo> event)
    {
        RTMaterials.onMaterialInfo();
    }
}