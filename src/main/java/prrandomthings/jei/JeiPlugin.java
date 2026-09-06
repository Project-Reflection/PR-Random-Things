package prrandomthings.jei;

import gregtech.api.GTValues;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.common.items.ToolItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        //registry.addRecipes(ExampleRecipeMaker.getExampleRecipe(), VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeCatalyst(ToolItems.HARD_HAMMER.get(Materials.Diamond),
                GTValues.MODID + ":" + RecipeMaps.FORGE_HAMMER_RECIPES.getUnlocalizedName());
    }
}