package prrandomthings.recipes;

import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.category.RecipeCategories;
import gregtech.api.unification.ore.OrePrefix;
import prrandomthings.materials.RTMaterials;

public final class ArsNouveauRecipes {
    private ArsNouveauRecipes() {
    }


    public static void register() {
        //source gem
        RTRecipeMaps.INFUSER.recipeBuilder()
                .input("gemAmethyst")
                .output(OrePrefix.gem,RTMaterials.SOURCE)
                .duration(500)
                .buildAndRegister();
        RTRecipeMaps.INFUSER.recipeBuilder()
                .input("gemLapis")
                .output(OrePrefix.gem,RTMaterials.SOURCE)
                .duration(500)
                .buildAndRegister();

        RecipeMaps.MACERATOR_RECIPES.recipeBuilder()
                .input(OrePrefix.gem,RTMaterials.SOURCE)
                .output(OrePrefix.dust,RTMaterials.SOURCE)
                .EUt(2)
                .duration(100)
                .category(RecipeCategories.MACERATOR_RECYCLING)
                .buildAndRegister();
        RTRecipeMaps.CRUCIBLE.recipeBuilder()
                .input(OrePrefix.gem,RTMaterials.SOURCE)
                .fluidOutputs(RTMaterials.SOURCE.getFluid(500))
                .duration(200)
                .buildAndRegister();
        RTRecipeMaps.CRUCIBLE.recipeBuilder()
                .input(OrePrefix.dust,RTMaterials.SOURCE)
                .fluidOutputs(RTMaterials.SOURCE.getFluid(300))
                .duration(100)
                .buildAndRegister();
    }

}
