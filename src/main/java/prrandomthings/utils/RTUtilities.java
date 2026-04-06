package prrandomthings.utils;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;

import java.util.Collection;

public class RTUtilities {
    public static void clearRecipeMap(RecipeMap<?> recipeMap) {
        Collection<Recipe> recipes = recipeMap.getRecipeList();
        for (Recipe recipe : recipes) {
            recipeMap.removeRecipe(recipe);
        }
    }
}