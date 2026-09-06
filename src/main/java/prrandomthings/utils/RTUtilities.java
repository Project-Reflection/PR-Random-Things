package prrandomthings.utils;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Predicate;

public class RTUtilities {
    public static void clearRecipeMap(RecipeMap<?> recipeMap) {
        Collection<Recipe> recipes = recipeMap.getRecipeList();
        for (Recipe recipe : recipes) {
            recipeMap.removeRecipe(recipe);
        }
    }
    @Nullable
    public static Recipe findRecipeWithOutput(RecipeMap<?> recipeMap, Predicate<ItemStack> predicate) {
        for (var recipe : recipeMap.getRecipeList()) {
            for (var output : recipe.getAllItemOutputs()) {
                if (predicate.test(output)) {
                    return recipe;
                }
            }
        }
        return null;
    }
    public static void removeRecipeWithOutput(RecipeMap<?> recipeMap, Predicate<ItemStack> predicate)
    {
        Recipe oldRecipe = RTUtilities.findRecipeWithOutput(recipeMap,
                predicate);

        while (oldRecipe != null){
            recipeMap.removeRecipe(oldRecipe);
            oldRecipe = RTUtilities.findRecipeWithOutput(recipeMap,
                    predicate);
        }
    }
}