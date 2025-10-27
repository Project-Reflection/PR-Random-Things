package prrandomthings.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        //registry.addRecipes(ExampleRecipeMaker.getExampleRecipe(), VanillaRecipeCategoryUid.CRAFTING);
    }
}