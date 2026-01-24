package prrandomthings.recipes;

import gregtech.api.items.OreDictNames;
import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.items.ToolItems;
import gregtech.loaders.recipe.handlers.ToolRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import prrandomthings.config.RTConfig;
import prrandomthings.mte.RTMetaTileEntities;

public class CraftingRecipes {
    private static void registerExtraFlintRecipes() {
        final UnificationEntry flint = new UnificationEntry(OrePrefix.gem, Materials.Flint);
        final UnificationEntry stick = new UnificationEntry(OrePrefix.stick, Materials.Wood);

        ToolRecipeHandler.addToolRecipe(Materials.Flint, ToolItems.HARD_HAMMER, true,
                " II", "SII", " II",
                'I', flint,
                'S', stick);
    }
    private static Object getPrimitiveFurnaceMaterial()
    {
        return RTConfig.enableExtraFlintTools?"sandstone":new ItemStack(Blocks.DIRT);
    }
    public static void registerCraftingRecipes() {
        ModHandler.addShapedRecipe("wooden_barrel", RTMetaTileEntities.COMPOSTING_BARREL.getStackForm(),
                "XAX", "XBX", "XCX",
                'X', new UnificationEntry(OrePrefix.plank, Materials.Wood),
                'A', new ItemStack(Items.BOWL),
                'B', new UnificationEntry(OrePrefix.slab, Materials.Wood),
                'C', OreDictNames.chestWood);
        ModHandler.addShapedRecipe("sieve", RTMetaTileEntities.SIEVE.getStackForm(),
                "XAX", "XBX", "XCX",
                'X', new UnificationEntry(OrePrefix.plank, Materials.Wood),
                'A', new ItemStack(Items.STRING),
                'B', new UnificationEntry(OrePrefix.slab, Materials.Wood),
                'C', OreDictNames.chestWood);
        ModHandler.addShapedRecipe("bricks_barrel", RTMetaTileEntities.BRICK_BARREL.getStackForm(),
                "XAX", "XBX", "XCX",
                'X', new ItemStack(Blocks.BRICK_BLOCK),
                'A', new ItemStack(Items.BOWL),
                'B', new ItemStack(Blocks.STONE_SLAB, 1, 4),
                'C', OreDictNames.chestWood);
        ModHandler.addShapedRecipe("dirt_furnace", RTMetaTileEntities.DIRT_FURNACE.getStackForm(),
                "XXX", "BBB", "XXX",
                'X', getPrimitiveFurnaceMaterial(),
                'B', new UnificationEntry(OrePrefix.gem, Materials.Flint));

        if(RTConfig.enableExtraFlintTools)
        {
            registerExtraFlintRecipes();
        }
    }
}