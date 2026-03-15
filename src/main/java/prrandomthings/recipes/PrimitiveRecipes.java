package prrandomthings.recipes;

import gregtech.api.GregTechAPI;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.GTRecipeOreInput;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.OreProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.items.MetaItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import prrandomthings.config.RTConfig;
import prrandomthings.constants.RTConstants;
import prrandomthings.items.RTMetaItem;
import prrandomthings.materials.RTMaterials;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class PrimitiveRecipes {
    private PrimitiveRecipes() {
    }

    private static final RecipeMap<PrimitiveRecipeBuilder> PBF_RECIPES = RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES;
    private static final RecipeMap<PrimitiveRecipeBuilder> COMPOSTING_RECIPES = RTRecipeMaps.COMPOSTING_BARREL;
    private static final RecipeMap<PrimitiveRecipeBuilder> SIEVE_RECIPES = RTRecipeMaps.SIEVE;
    private static final RecipeMap<PrimitiveRecipeBuilder> ROCK_RECIPES = RTRecipeMaps.STONE_BARREL;
    private static final RecipeMap<PrimitiveRecipeBuilder> DF_RECIPES = RTRecipeMaps.DIRT_FURNACE;

    public static void addStandardCompostingRecipe(GTRecipeInput input) {
        COMPOSTING_RECIPES.recipeBuilder()
                .chancedOutput(new ItemStack(Blocks.DIRT), 1250, 0)
                .fluidOutputs(Materials.Water.getFluid(250))
                .input(input)
                .duration(RTConfig.compostingSpeed / 8)
                .buildAndRegister();
    }

    public static void addRockMeltingRecipe(GTRecipeInput... inputs) {
        for (GTRecipeInput input : inputs) {
            RTRecipeMaps.CRUCIBLE.recipeBuilder()
                    .fluidOutputs(Materials.Lava.getFluid(250))
                    .input(input)
                    .duration(20 * 10)
                    .buildAndRegister();
        }
    }

    public static void register() {
        clear(PBF_RECIPES);
        PBF_RECIPES.recipeBuilder()
                .output(OrePrefix.ingot, Materials.Steel, 1)
                .output(OrePrefix.gem, RTMaterials.SLAG, 1)
                .input("ingotWroughtIron", 1)
                .input("dustCalcite", 1)
                .input("gemCoke", 1)
                .duration(20 * 60 * 6)
                .buildAndRegister();
        PBF_RECIPES.recipeBuilder()
                .output(OrePrefix.ingot, Materials.WroughtIron, 8)
                .input("ingotIron", 8)
                .input("gemCharcoal", 1)
                .duration(20 * 10 * 8)
                .buildAndRegister();
        //3Fe + 3SiO2 = FeSi + 2FeSiO3
        PBF_RECIPES.recipeBuilder()
                .output(OrePrefix.ingot, RTMaterials.ELECTRICAL_STEEL, 2)
                .output(OrePrefix.gem, RTMaterials.FERROUS_SLAG, 12)
                .input("ingotSteel", 3)
                .input("dustSiliconDioxide", 9)
                .input("gemCoke")
                .duration(20 * 10 * 3)
                .buildAndRegister();
        //5Cu + 3SiO2 = CuSi + 2Cu2SiO3
        PBF_RECIPES.recipeBuilder()
                .output(OrePrefix.ingot, RTMaterials.SILICON_BRONZE, 2)
                .output(OrePrefix.gem, RTMaterials.CUPROUS_SLAG, 14)
                .input("ingotCopper", 5)
                .input("dustSiliconDioxide", 9)
                .input("gemCoke")
                .duration(2 * 10 * 5)
                .buildAndRegister();
        //crop composting
        addStandardCompostingRecipe(new GTRecipeOreInput("treeSapling"));
        addStandardCompostingRecipe(new GTRecipeOreInput("treeLeaves"));
        addStandardCompostingRecipe(new GTRecipeItemInput(new ItemStack(Items.WHEAT)));
        addStandardCompostingRecipe(new GTRecipeItemInput(new ItemStack(Items.POTATO)));
        addStandardCompostingRecipe(new GTRecipeItemInput(new ItemStack(Items.CARROT)));
        addStandardCompostingRecipe(new GTRecipeItemInput(new ItemStack(Items.BEETROOT)));
        addStandardCompostingRecipe(new GTRecipeItemInput(new ItemStack(Items.REEDS)));
        addStandardCompostingRecipe(new GTRecipeItemInput(new ItemStack(Blocks.CACTUS)));
        addStandardCompostingRecipe(new GTRecipeItemInput(new ItemStack(Blocks.BROWN_MUSHROOM)));
        addStandardCompostingRecipe(new GTRecipeItemInput(new ItemStack(Blocks.RED_MUSHROOM)));
        //special composting
        COMPOSTING_RECIPES.recipeBuilder()
                .output(Blocks.DIRT)
                .fluidOutputs(Materials.Water.getFluid(2000))
                .input(MetaItems.PLANT_BALL)
                .duration(RTConfig.compostingSpeed / 2)
                .buildAndRegister();
        COMPOSTING_RECIPES.recipeBuilder()
                .chancedOutput(MetaItems.PLANT_BALL, 1000, 0)
                .fluidOutputs(Materials.Glue.getFluid(100))
                .input(MetaItems.STICKY_RESIN)
                .duration(20 * 20)
                .buildAndRegister();
        //compression
        COMPOSTING_RECIPES.recipeBuilder()
                .output(Blocks.GRAVEL)
                .input("dustStone", 9)
                .duration(20 * 10)
                .buildAndRegister();
        //clay making
        COMPOSTING_RECIPES.recipeBuilder()
                .output(Items.CLAY_BALL)
                .input("dustClay")
                .fluidInputs(Materials.Water.getFluid(250))
                .duration(20 * 10)
                .buildAndRegister();
        COMPOSTING_RECIPES.recipeBuilder()
                .output(Items.CLAY_BALL)
                .input("dustQuartzSand")
                .fluidInputs(Materials.Water.getFluid(250))
                .duration(20 * 10)
                .buildAndRegister();
        for (ItemStack mesh : new ItemStack[]{
                RTMetaItem.PLANT_MESH.getStackForm(),
                RTMetaItem.STRING_MESH.getStackForm()
        }) {
            SIEVE_RECIPES.recipeBuilder()
                    .output(OrePrefix.dust, Materials.Stone, 4)
                    .chancedOutput(OrePrefix.dust, Materials.Stone, 5000, 0)
                    .chancedOutput(OrePrefix.dust, Materials.Clay, 450, 0)
                    .chancedOutputs(Arrays.asList(
                            new ChancedItemOutput(new ItemStack(Blocks.SAPLING, 1, 0), 1000, 0),
                            new ChancedItemOutput(new ItemStack(Blocks.SAPLING, 1, 1), 1000, 0),
                            new ChancedItemOutput(new ItemStack(Blocks.SAPLING, 1, 2), 1000, 0),
                            new ChancedItemOutput(new ItemStack(Blocks.SAPLING, 1, 3), 1000, 0),
                            new ChancedItemOutput(new ItemStack(Blocks.SAPLING, 1, 4), 1000, 0),
                            new ChancedItemOutput(new ItemStack(Blocks.SAPLING, 1, 5), 1000, 0),
                            new ChancedItemOutput(new ItemStack(MetaBlocks.RUBBER_SAPLING), 1000, 0)
                    ))
                    .chancedOutputs(Arrays.asList(
                            new ChancedItemOutput(new ItemStack(Items.WHEAT_SEEDS), 1000, 0),
                            new ChancedItemOutput(new ItemStack(Items.BEETROOT_SEEDS), 1000, 0),
                            new ChancedItemOutput(new ItemStack(Items.MELON_SEEDS), 1000, 0),
                            new ChancedItemOutput(new ItemStack(Items.PUMPKIN_SEEDS), 1000, 0)
                    ))
                    .input(Blocks.DIRT, 1)
                    .input(new GTRecipeItemInput(mesh).setNonConsumable())
                    .circuitMeta(1)
                    .duration(20 * 10)
                    .buildAndRegister();
            SIEVE_RECIPES.recipeBuilder()
                    .chancedOutput(OrePrefix.dust, Materials.Clay, 900, 0)
                    .chancedOutput(MetaItems.PLANT_BALL, 1250, 0)
                    .input(Blocks.DIRT, 1)
                    .input(new GTRecipeItemInput(mesh).setNonConsumable())
                    .circuitMeta(2)
                    .duration(20 * 15)
                    .buildAndRegister();
            SIEVE_RECIPES.recipeBuilder()
                    .input(new GTRecipeItemInput(mesh).setNonConsumable())
                    .output(OrePrefix.gem, Materials.Flint, 3)
                    .chancedOutput(OrePrefix.gem, Materials.Flint, 8800, 0)
                    .chancedOutput(OrePrefix.gem, RTMaterials.UNINSPECTED, 1000, 0)
                    .input(Blocks.GRAVEL, 1)
                    .circuitMeta(1)
                    .duration(20 * 10)
                    .buildAndRegister();
            if (RTConstants.Environment.botaniaLoaded) {
                Item petal = Item.getByNameOrId("botania:petal");
                assert petal != null;
                SIEVE_RECIPES.recipeBuilder()
                        .input(new GTRecipeItemInput(mesh).setNonConsumable())
                        .chancedOutputs(
                                IntStream.range(0, 16).mapToObj(meta -> new ItemStack(petal, 1, meta))
                                        .map(itemStack -> new ChancedItemOutput(itemStack, 625, 0))
                                        .collect(Collectors.toList())
                        )
                        .input(Blocks.DIRT, 1)
                        .circuitMeta(3)
                        .duration(20 * 10)
                        .buildAndRegister();
            }
        }
        //rock melting
        addRockMeltingRecipe(new GTRecipeOreInput("gravel"),
                new GTRecipeOreInput("cobblestone"),
                new GTRecipeOreInput("stoneGranite"),
                new GTRecipeOreInput("stoneDiorite"),
                new GTRecipeOreInput("stoneAndesite"),
                new GTRecipeOreInput("stone")
        );
        RTRecipeMaps.CRUCIBLE.recipeBuilder()
                .input(OrePrefix.gem, RTMaterials.UNINSPECTED)
                .fluidOutputs(RTMaterials.UNINSPECTED.getFluid(144))
                .duration(20 * 10)
                .buildAndRegister();

        //region cobblegen
        ROCK_RECIPES.recipeBuilder()
                .notConsumable(Materials.Lava.getFluid(), 1000)
                .notConsumable(Materials.Water.getFluid(), 1000)
                .circuitMeta(1)
                .output(Blocks.COBBLESTONE)
                .duration(20)
                .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
                .notConsumable(Materials.Lava.getFluid(), 1000)
                .fluidInputs(Materials.Water.getFluid(1000))
                .circuitMeta(2)
                .output(Blocks.STONE)
                .duration(20)
                .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
                .fluidInputs(Materials.Lava.getFluid(1000))
                .fluidInputs(Materials.Water.getFluid(1000))
                .circuitMeta(3)
                .output(Blocks.OBSIDIAN)
                .duration(20)
                .buildAndRegister();
        //endregion

        for (Material mat : GregTechAPI.materialManager.getRegisteredMaterials()) {

            if (mat.hasFluid() && mat.getBlastTemperature() <= 0) {
                //crucible melting
                if (mat.hasProperty(PropertyKey.DUST)) {
                    RTRecipeMaps.CRUCIBLE.recipeBuilder()
                            .input(OrePrefix.dust, mat)
                            .fluidOutputs(mat.getFluid(144))
                            .duration(20 * 10)
                            .buildAndRegister();
                    ROCK_RECIPES.recipeBuilder()
                            .fluidInputs(mat.getFluid(144),Materials.Water.getFluid(1))
                            .fluidOutputs(Materials.Steam.getFluid(160))
                            .output(OrePrefix.dust,mat)
                            .duration(20*20)
                            .buildAndRegister();
                }
                if (mat.hasProperty(PropertyKey.ORE)) {
                    OreProperty property=mat.getProperty(PropertyKey.ORE);
                    RTRecipeMaps.CRUCIBLE.recipeBuilder()
                            .input(OrePrefix.ore, mat)
                            .fluidOutputs(mat.getFluid(288*property.getOreMultiplier()))
                            .duration(20 * 20)
                            .buildAndRegister();
                }
            }
            if (mat.hasProperty(PropertyKey.ORE)) {
                OreProperty property=mat.getProperty(PropertyKey.ORE);
                ROCK_RECIPES.recipeBuilder()
                        .output(OrePrefix.ore, mat)
                        .input(OrePrefix.dust, mat, property.getOreMultiplier())
                        .input(OrePrefix.dust, Materials.Flint,
                                8*Math.max(property.getOreMultiplier(),property.getByProductMultiplier()))
                        .fluidInputs(RTMaterials.UNINSPECTED.getFluid(1))
                        .circuitMeta(1)
                        .duration(20 * 10*Math.max(property.getOreMultiplier(),property.getByProductMultiplier()))
                        .buildAndRegister();
            }
        }
        ROCK_RECIPES.recipeBuilder()
                .fluidInputs(RTMaterials.UNINSPECTED.getFluid(1))
                .input(OrePrefix.dust, Materials.Iron, 1)
                .input(OrePrefix.dust, Materials.Flint, 8)
                .circuitMeta(2)
                .output(OrePrefix.ore, Materials.Chalcopyrite)
                .duration(20 * 10)
                .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
                .fluidInputs(RTMaterials.UNINSPECTED.getFluid(1))
                .input(OrePrefix.dust, Materials.Iron, 1)
                .input(OrePrefix.dust, Materials.Flint, 8)
                .circuitMeta(3)
                .output(OrePrefix.ore, Materials.Tin)
                .duration(20 * 10)
                .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
                .fluidInputs(RTMaterials.UNINSPECTED.getFluid(1))
                .input(OrePrefix.dust, Materials.Gold, 1)
                .input(OrePrefix.dust, Materials.Flint, 8)
                .circuitMeta(2)
                .output(OrePrefix.ore, Materials.Galena)
                .duration(20 * 10)
                .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
                .fluidInputs(RTMaterials.UNINSPECTED.getFluid(1))
                .input(OrePrefix.dust, Materials.Gold, 1)
                .input(OrePrefix.dust, Materials.Flint, 8)
                .circuitMeta(3)
                .output(OrePrefix.ore, Materials.Bauxite)
                .duration(20 * 10)
                .buildAndRegister();
        ROCK_RECIPES.recipeBuilder()
                .fluidInputs(RTMaterials.UNINSPECTED.getFluid(1))
                .input(OrePrefix.dust, Materials.Gold, 2)
                .input(OrePrefix.dust, Materials.Flint, 16)
                .circuitMeta(4)
                .output(OrePrefix.ore, Materials.Pentlandite)
                .duration(20 * 10)
                .buildAndRegister();

        ROCK_RECIPES.recipeBuilder()
                .fluidInputs(Materials.Lava.getFluid(1000))
                .input(OrePrefix.dust,Materials.Redstone)
                .output(Blocks.NETHERRACK)
                .duration(20)
                .buildAndRegister();

        if (!RTConstants.Environment.lycaniteLoaded) {
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
                    .notConsumable(OrePrefix.gem, Materials.Emerald, 1)
                    .output(OrePrefix.gem, Materials.NetherQuartz, 8)
                    .chancedOutput(OrePrefix.gem, Materials.Diamond, 1, 500, 0)
                    .chancedOutput(OrePrefix.gem, Materials.Emerald, 1, 500, 0)
                    .chancedOutput(OrePrefix.gem, Materials.Lapis, 6, 2500, 0)
                    .duration(20 * 16)
                    .buildAndRegister();
        }
        var clay = ConfigHolder.recipes.harderBrickRecipes ? MetaItems.COMPRESSED_CLAY.getStackForm(3)
                : new ItemStack(Items.CLAY_BALL, 1);
        DF_RECIPES.recipeBuilder()
                .input(new GTRecipeItemInput(clay))
                .output(Items.BRICK, 1)
                .duration(20 * 20)
                .buildAndRegister();
    }

    public static void clear(RecipeMap<?> recipeMap) {
        Collection<Recipe> recipes = recipeMap.getRecipeList();
        for (Recipe recipe : recipes) {
            recipeMap.removeRecipe(recipe);
        }
    }
}
