package prrandomthings.recipes;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.items.MetaItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import prrandomthings.constants.RTConstants;
import prrandomthings.materials.RTMaterials;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class PrimitiveRecipes {
    private PrimitiveRecipes(){}
    private static final RecipeMap<PrimitiveRecipeBuilder> PBF_RECIPES=RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES;
    private static final RecipeMap<PrimitiveRecipeBuilder> COMPOSTING_RECIPES= RTRecipeMaps.COMPOSTING_BARREL;
    private static final RecipeMap<PrimitiveRecipeBuilder> SIEVE_RECIPES=RTRecipeMaps.SIEVE;
    private static final RecipeMap<PrimitiveRecipeBuilder> ROCK_RECIPES=RTRecipeMaps.STONE_BARREL;
    private static final RecipeMap<PrimitiveRecipeBuilder> DF_RECIPES=RTRecipeMaps.DIRT_FURNACE;
    public static void register()
    {
        clear(PBF_RECIPES);
        PBF_RECIPES.recipeBuilder()
            .output(OrePrefix.ingot, Materials.Steel, 1)
            .output(OrePrefix.gem, RTMaterials.SLAG,1)
            .input("ingotWroughtIron",1)
            .input("dustCalcite",1)
            .input("gemCoke",1)
            .duration(20*60*6)
            .buildAndRegister();
        PBF_RECIPES.recipeBuilder()
            .output(OrePrefix.ingot, Materials.WroughtIron, 8)
            .input("ingotIron",8)
            .input("gemCharcoal",1)
            .duration(20*10*8)
            .buildAndRegister();
        //3Fe + 3SiO2 = FeSi + 2FeSiO3
        PBF_RECIPES.recipeBuilder()
            .output(OrePrefix.ingot,RTMaterials.ELECTRICAL_STEEL,2)
            .output(OrePrefix.gem,RTMaterials.FERROUS_SLAG,12)
            .input("ingotSteel",3)
            .input("dustSiliconDioxide",9)
            .input("gemCoke")
            .duration(20*10*3)
            .buildAndRegister();
        //5Cu + 3SiO2 = CuSi + 2Cu2SiO3
        PBF_RECIPES.recipeBuilder()
            .output(OrePrefix.ingot,RTMaterials.SILICON_BRONZE,2)
            .output(OrePrefix.gem,RTMaterials.CUPROUS_SLAG,14)
            .input("ingotCopper",5)
            .input("dustSiliconDioxide",9)
            .input("gemCoke")
            .duration(2*10*5)
            .buildAndRegister();

        COMPOSTING_RECIPES.recipeBuilder()
            .chancedOutput(new ItemStack(Blocks.DIRT),1250,0)
            .fluidOutputs(Materials.Water.getFluid(250))
            .input("treeSapling")
            .duration(20*10)
            .buildAndRegister();
        COMPOSTING_RECIPES.recipeBuilder()
            .chancedOutput(new ItemStack(Blocks.DIRT),1250,0)
            .fluidOutputs(Materials.Water.getFluid(250))
            .input("treeLeaves")
            .duration(20*10)
            .buildAndRegister();
        COMPOSTING_RECIPES.recipeBuilder()
            .output(Blocks.GRAVEL)
            .input("dustStone",9)
            .duration(20*10)
            .buildAndRegister();
        COMPOSTING_RECIPES.recipeBuilder()
            .output(Items.CLAY_BALL)
            .input("dustClay")
            .fluidInputs(Materials.Water.getFluid(250))
            .duration(20*10)
            .buildAndRegister();
        COMPOSTING_RECIPES.recipeBuilder()
            .output(Items.CLAY_BALL)
            .input("dustQuartzSand")
            .fluidInputs(Materials.Water.getFluid(250))
            .duration(20*10)
            .buildAndRegister();

        SIEVE_RECIPES.recipeBuilder()
            .output(OrePrefix.dust,Materials.Stone,2)
            .chancedOutput(OrePrefix.dust,Materials.Clay,450,0)
            .chancedOutputs(Arrays.asList(
                    new ChancedItemOutput(new ItemStack(Blocks.SAPLING,1,0),1000,0),
                    new ChancedItemOutput(new ItemStack(Blocks.SAPLING,1,1),1000,0),
                    new ChancedItemOutput(new ItemStack(Blocks.SAPLING,1,2),1000,0),
                    new ChancedItemOutput(new ItemStack(Blocks.SAPLING,1,3),1000,0),
                    new ChancedItemOutput(new ItemStack(Blocks.SAPLING,1,4),1000,0),
                    new ChancedItemOutput(new ItemStack(Blocks.SAPLING,1,5),1000,0),
                    new ChancedItemOutput(new ItemStack(MetaBlocks.RUBBER_SAPLING),1000,0)
            ))
            .chancedOutputs(Arrays.asList(
                    new ChancedItemOutput(new ItemStack(Items.WHEAT_SEEDS),1000,0),
                    new ChancedItemOutput(new ItemStack(Items.BEETROOT_SEEDS),1000,0),
                    new ChancedItemOutput(new ItemStack(Items.MELON_SEEDS),1000,0),
                    new ChancedItemOutput(new ItemStack(Items.PUMPKIN_SEEDS),1000,0)
            ))
            .input(Blocks.DIRT,1)
            .circuitMeta(1)
            .duration(20*10)
            .buildAndRegister();
        if(RTConstants.Environment.botaniaLoaded)
        {
            Item petal=Item.getByNameOrId("botania:petal");
            assert petal != null;
            SIEVE_RECIPES.recipeBuilder()
                    .chancedOutputs(
                            IntStream.range(0,16).mapToObj(meta->new ItemStack(petal,1,meta))
                                    .map(itemStack->new ChancedItemOutput(itemStack,625,0))
                                    .collect(Collectors.toList())
                    )
                    .input(Blocks.DIRT,1)
                    .circuitMeta(2)
                    .duration(20*10)
                    .buildAndRegister();
        }
        SIEVE_RECIPES.recipeBuilder()
            .output(OrePrefix.gem,Materials.Flint,3)
            .chancedOutput(OrePrefix.gem,Materials.Flint,8800,0)
            .chancedOutput(OrePrefix.gem,RTMaterials.UNINSPECTED,1000,0)
            .input(Blocks.GRAVEL,1)
            .circuitMeta(1)
            .duration(20*10)
            .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
            .fluidOutputs(Materials.Lava.getFluid(250))
            .input("gravel")
            .input(new GTRecipeItemInput(new ItemStack(Blocks.TORCH)).setNonConsumable())
            .duration(20*10)
            .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
            .notConsumable(Materials.Lava.getFluid(),1000)
            .input(OrePrefix.dust,RTMaterials.UNINSPECTED,8)
            .output(OrePrefix.ore,RTMaterials.UNINSPECTED)
            .duration(20*10)
            .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
            .notConsumable(Materials.Lava.getFluid(),1000)
            .input(OrePrefix.dust,Materials.Iron,8)
                .circuitMeta(1)
            .output(OrePrefix.ore,Materials.Chalcopyrite)
            .duration(20*10)
            .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
            .notConsumable(Materials.Lava.getFluid(),1000)
            .input(OrePrefix.dust,Materials.Iron,8)
                .circuitMeta(2)
            .output(OrePrefix.ore,Materials.Tin)
            .duration(20*10)
            .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
            .notConsumable(Materials.Lava.getFluid(),1000)
            .input(OrePrefix.dust,Materials.Gold,8)
                .circuitMeta(1)
            .output(OrePrefix.ore,Materials.Galena)
            .duration(20*10)
            .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
            .notConsumable(Materials.Lava.getFluid(),1000)
            .input(OrePrefix.dust,Materials.Gold,8)
                .circuitMeta(2)
            .output(OrePrefix.ore,Materials.Bauxite)
            .duration(20*10)
            .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
            .notConsumable(Materials.Lava.getFluid(),1000)
            .input(OrePrefix.dust,Materials.Gold,16)
            .circuitMeta(3)
            .output(OrePrefix.ore,Materials.Pentlandite)
            .duration(20*10)
            .buildAndRegister();
        if(!RTConstants.Environment.lycaniteLoaded) {
            ROCK_RECIPES.recipeBuilder()
                .notConsumable(Materials.Lava.getFluid(), 1000)
                .notConsumable(OrePrefix.ore, RTMaterials.UNINSPECTED, 16)
                .circuitMeta(1)
                .chancedOutput(OrePrefix.ore, Materials.Magnetite, 2, 7500, 0)
                .chancedOutput(OrePrefix.ore, Materials.Gold, 1, 1000, 0)
                .chancedOutput(OrePrefix.gem, Materials.NetherQuartz, 4, 7500, 0)
                .duration(20 * 16)
                .buildAndRegister();
            ROCK_RECIPES.recipeBuilder()
                .notConsumable(Materials.Lava.getFluid(), 1000)
                .notConsumable(OrePrefix.ore, RTMaterials.UNINSPECTED, 16)
                .notConsumable(OrePrefix.gem,Materials.Emerald,1)
                .output(OrePrefix.gem, Materials.NetherQuartz, 8)
                .chancedOutput(OrePrefix.gem, Materials.Diamond, 1, 500, 0)
                .chancedOutput(OrePrefix.gem, Materials.Emerald, 1, 500, 0)
                .chancedOutput(OrePrefix.gem, Materials.Lapis, 6, 2500, 0)
                .duration(20 * 16)
                .buildAndRegister();
        }
        var clay= ConfigHolder.recipes.harderBrickRecipes? MetaItems.COMPRESSED_CLAY.getStackForm(3)
                :new ItemStack(Items.CLAY_BALL,3);
        DF_RECIPES.recipeBuilder()
                .input(new GTRecipeItemInput(clay))
                .input("plankWood",2)
                .input("dustGunpowder")
                .output(Items.BRICK,3)
                .duration(60*20)
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
