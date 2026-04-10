package prrandomthings.events;

import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.material.event.PostMaterialEvent;
import gregtech.api.unification.stack.ItemMaterialInfo;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prrandomthings.constants.RTConstants;
import prrandomthings.config.RTConfig;
import prrandomthings.enchantments.RTEnchantments;
import prrandomthings.items.RTMetaItem;
import prrandomthings.mte.multiblock.MteHeater;
import prrandomthings.recipes.CraftingRecipes;
import prrandomthings.recipes.ArsNouveauRecipes;
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
                RTMetaItem.META_ITEM_1
        );
        RTMetaItem.META_ITEM_1.registerSubItems();
    }

    @SubscribeEvent
    public static void registerRecipeEvent(RegistryEvent.Register<IRecipe> event) {
        PrimitiveRecipes.register();
        MetallurgicRecipes.register();
        ArsNouveauRecipes.register();
        CraftingRecipes.registerCraftingRecipes();

        RecipeMaps.STEAM_TURBINE_FUELS.getRecipeList().forEach(RecipeMaps.STEAM_TURBINE_FUELS::removeRecipe);
        RecipeMaps.STEAM_TURBINE_FUELS.recipeBuilder()
                .fluidInputs(Materials.Steam.getFluid(64))
                .fluidOutputs(RTMaterials.LOW_QUALITY_STEAM.getFluid(128))
                .EUt(32)
                .duration(1)
                .buildAndRegister();
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
        MetaTileEntities.registerMetaTileEntity(id++, RTMetaTileEntities.BRICK_BARREL);
        MetaTileEntities.registerMetaTileEntity(id++, RTMetaTileEntities.DIRT_FURNACE);
        MetaTileEntities.registerMetaTileEntity(id++, MteHeater.SAMPLE);
        MetaTileEntities.registerMetaTileEntity(id++,RTMetaTileEntities.CRUCIBLE);
        MetaTileEntities.registerMetaTileEntity(id++,RTMetaTileEntities.INFUSER);
        MetaTileEntities.registerMetaTileEntity(id++,RTMetaTileEntities.PITIFUL_BOILER);
    }

    @SubscribeEvent
    public static void registerMaterials(MaterialEvent event) {
        RTConstants.LOGGER.info("Registering materials");
        RTMaterials.register();
    }
    @SubscribeEvent
    public static void postRegisterMaterials(PostMaterialEvent event)
    {
        RTConstants.LOGGER.info("Modifying existing materials");
        RTMaterials.postRegister();
    }

    @SubscribeEvent
    public static void onMaterialInfo(GregTechAPI.RegisterEvent<ItemMaterialInfo> event)
    {
        RTConstants.LOGGER.info("Adding material information");
        RTMaterials.onMaterialInfo();
    }
    @SubscribeEvent(priority=EventPriority.LOW)
    public static void onEnchantment(RegistryEvent.Register<Enchantment> event){
        RTConstants.LOGGER.info("Registering enchantments");
        RTEnchantments.register(event.getRegistry());
    }
}