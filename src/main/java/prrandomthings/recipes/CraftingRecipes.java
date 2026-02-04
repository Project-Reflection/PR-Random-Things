package prrandomthings.recipes;

import com.google.common.collect.Collections2;
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
import prrandomthings.items.RTMetaItem;
import prrandomthings.materials.RTMaterials;
import prrandomthings.mte.RTMetaTileEntities;

import java.util.Collections;

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
        return RTConfig.sandstoneDirtFurnace?"sandstone":new ItemStack(Blocks.DIRT);
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
                'A', RTMetaItem.PLANT_MESH,
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
        ModHandler.addShapelessRecipe("plant_string", RTMetaItem.PLANT_STRING.getStackForm(),
                Collections.nCopies(3,RTMetaItem.PLANT_FIBER.getStackForm()).toArray());
        ModHandler.addShapedRecipe("plant_mesh", RTMetaItem.PLANT_MESH.getStackForm(),
                "XAX","AAA","XAX",
                'X',"stickWood",'A',RTMetaItem.PLANT_STRING.getStackForm());
        ModHandler.addShapedRecipe("string_mesh", RTMetaItem.STRING_MESH.getStackForm(),
                "XAX","AAA","XAX",
                'X',"stickWood",'A',Items.STRING.getDefaultInstance());

        if(RTConfig.enableExtraFlintTools)
        {
            registerExtraFlintRecipes();
        }
    }
}