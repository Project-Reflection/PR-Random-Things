package prrandomthings.recipes;

import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import net.minecraft.init.Items;
import prrandomthings.materials.RTMaterials;

public class SteamRecipes {
    public static void register(){
        RTRecipeMaps.PITIFUL_COMPRESSOR.recipeBuilder()
                .input(OrePrefix.dust, Materials.Fireclay)
                .input(new GTRecipeItemInput(MetaItems.WOODEN_FORM_BRICK.getStackForm()).setNonConsumable())
                .outputs(MetaItems.FIRECLAY_BRICK.getStackForm())
                .duration(100)
                .EUt(7)
                .buildAndRegister();
        RTRecipeMaps.ALCHEMIZER.recipeBuilder()
                .input(Items.WHEAT,4)
                .fluidInputs(Materials.Water.getFluid(1000))
                .fluidOutputs(RTMaterials.ALCHEMICAL_OIL.getFluid(1000))
                .duration(20)
                .EUt(7)
                .buildAndRegister();
    }
}
