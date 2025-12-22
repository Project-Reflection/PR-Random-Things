package prrandomthings.recipes;

import gregtech.api.items.OreDictNames;
import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import prrandomthings.mte.RTMetaTileEntities;

public class CraftingRecipes {
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
                'X', new ItemStack(Blocks.DIRT),
                'B', new UnificationEntry(OrePrefix.gem, Materials.Flint));
    }
}