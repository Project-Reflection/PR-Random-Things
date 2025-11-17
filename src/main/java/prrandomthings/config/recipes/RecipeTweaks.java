package prrandomthings.config.recipes;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import prrandomthings.materials.RTMaterials;

import java.util.Collection;

public final class RecipeTweaks {
    private RecipeTweaks(){}
    public static void register()
    {
        clear(RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES);
        RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder()
                .output(OrePrefix.ingot, Materials.Steel, 1)
                .output(OrePrefix.gem, RTMaterials.SLAG,1)
                .input("ingotWroughtIron",1)
                .input("dustCalcite",1)
                .input("gemCoke",1)
                .duration(20*60*6)
                .buildAndRegister();
        RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder()
                .output(OrePrefix.ingot, Materials.WroughtIron, 8)
                .input("ingotIron",8)
                .input("gemCharcoal",1)
                .duration(20*10*8)
                .buildAndRegister();
    }
    public static void clear(RecipeMap<?> recipeMap)
    {
        Collection<Recipe> recipes=recipeMap.getRecipeList();
        for(Recipe recipe:recipes)
        {
            recipeMap.removeRecipe(recipe);
        }
    }
}
