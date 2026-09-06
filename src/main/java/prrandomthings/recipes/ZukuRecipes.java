package prrandomthings.recipes;

import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.store.FluidStorageKeys;
import gregtech.api.items.OreDictNames;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.BlastRecipeBuilder;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.BlastProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.loaders.recipe.CraftingComponent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidStack;
import prrandomthings.constants.RTConstants;
import prrandomthings.materials.RTOrePrefixes;
import prrandomthings.mte.RTMetaTileEntities;
import prrandomthings.utils.RTUtilities;

import java.util.Map;

public class ZukuRecipes {
    public static void generateZukuRecipes() {
        for (Material material : GregTechAPI.materialManager.getRegisteredMaterials()) {
            if (RTOrePrefixes.generateZuku(material)) {
                RTUtilities.removeRecipeWithOutput(RecipeMaps.ARC_FURNACE_RECIPES,
                        itemStack -> OreDictUnifier.hasOreDictionary(itemStack,
                                new UnificationEntry(OrePrefix.ingot, material).toString()));
                int EUt = 120;
                int duration = 300;
                if (material.hasProperty(PropertyKey.BLAST)) {

                    BlastProperty property = material.getProperty(PropertyKey.BLAST);
                    int blastTemp = property.getBlastTemperature();
                    duration = property.getDurationOverride();
                    if (duration <= 0) {
                        duration = Math.max(1, (int) (material.getMass() * (long) blastTemp / 50L));
                    }

                    EUt = property.getEUtOverride();
                    if (EUt <= 0) {
                        EUt = GTValues.VA[2];
                    }
                    if (OrePrefix.ingotHot.doGenerateItem(material)) {
                        RTUtilities.removeRecipeWithOutput(RecipeMaps.VACUUM_RECIPES,
                                itemStack -> OreDictUnifier.hasOreDictionary(itemStack,
                                        new UnificationEntry(OrePrefix.ingot, material).toString()));
                        int vacuumEUt = property.getVacuumEUtOverride() != -1 ? property.getVacuumEUtOverride() : GTValues.VA[2];
                        int vacuumDuration = property.getVacuumDurationOverride() != -1 ? property.getVacuumDurationOverride() : (int) material.getMass() * 3;
                        if (blastTemp < 5000) {
                            RecipeMaps.VACUUM_RECIPES.recipeBuilder()
                                    .input(OrePrefix.ingotHot, material)
                                    .output(RTOrePrefixes.ZUKU, material, 4)
                                    .duration(vacuumDuration)
                                    .EUt(vacuumEUt)
                                    .buildAndRegister();
                        } else {
                            RecipeMaps.VACUUM_RECIPES.recipeBuilder()
                                    .input(OrePrefix.ingotHot, material)
                                    .fluidInputs(new FluidStack[]{Materials.Helium.getFluid(FluidStorageKeys.LIQUID, 500)})
                                    .output(RTOrePrefixes.ZUKU, material, 4)
                                    .fluidOutputs(new FluidStack[]{Materials.Helium.getFluid(250)})
                                    .duration(vacuumDuration)
                                    .EUt(vacuumEUt)
                                    .buildAndRegister();
                        }

                        if(material == Materials.Silicon || material == Materials.Kanthal){
                            RTUtilities.removeRecipeWithOutput(RecipeMaps.CHEMICAL_BATH_RECIPES,
                                    itemStack -> OreDictUnifier.hasOreDictionary(itemStack,
                                            new UnificationEntry(OrePrefix.ingot, material).toString()));
                            RecipeMaps.CHEMICAL_BATH_RECIPES.recipeBuilder()
                                    .input(OrePrefix.ingotHot,material)
                                    .output(RTOrePrefixes.ZUKU,material,2)
                                    .EUt(120)
                                    .duration(400)
                                    .buildAndRegister();
                        }
                    } else {
                        BlastProperty.GasTier gasTier = property.getGasTier();
                        RTUtilities.removeRecipeWithOutput(RecipeMaps.BLAST_RECIPES,
                                itemStack -> OreDictUnifier.hasOreDictionary(itemStack,
                                        new UnificationEntry(OrePrefix.ingot, material).toString()));
                        RecipeMaps.BLAST_RECIPES.recipeBuilder()
                                .input(OrePrefix.dust, material)
                                .output(RTOrePrefixes.ZUKU, material, 1)
                                .blastFurnaceTemp(blastTemp).EUt(EUt)
                                .circuitMeta(1)
                                .duration(duration)
                                .buildAndRegister();
                        if (gasTier != null) {
                            FluidStack gas = CraftingComponent.EBF_GASES.get(gasTier).copy();

                            RecipeMaps.BLAST_RECIPES.recipeBuilder()
                                    .input(OrePrefix.dust, material)
                                    .output(RTOrePrefixes.ZUKU, material, 2)
                                    .blastFurnaceTemp(blastTemp).EUt(EUt)
                                    .circuitMeta(2)
                                    .fluidInputs(gas)
                                    .duration((int) ((double) duration * 1.33))
                                    .buildAndRegister();
                            RecipeMaps.BLAST_RECIPES.recipeBuilder()
                                    .input(OrePrefix.dust, material)
                                    .input(OrePrefix.dust, Materials.Carbon, 4)
                                    .output(RTOrePrefixes.ZUKU, material, 4)
                                    .blastFurnaceTemp(blastTemp).EUt(EUt)
                                    .circuitMeta(3)
                                    .fluidInputs(gas)
                                    .duration((int) ((double) duration * 1.78))
                                    .buildAndRegister();
                        } else {
                            RecipeMaps.BLAST_RECIPES.recipeBuilder()
                                    .input(OrePrefix.dust, material)
                                    .input(OrePrefix.dust, Materials.Carbon, 4)
                                    .output(RTOrePrefixes.ZUKU, material, 4)
                                    .blastFurnaceTemp(blastTemp).EUt(EUt)
                                    .circuitMeta(3)
                                    .duration((int) ((double) duration * 1.78))
                                    .buildAndRegister();
                        }
                    }
                } else {
                    replaceFurnaceRecipes(material);
                    RTUtilities.removeRecipeWithOutput(RecipeMaps.ALLOY_SMELTER_RECIPES,
                            itemStack -> OreDictUnifier.hasOreDictionary(itemStack,
                                    new UnificationEntry(OrePrefix.ingot, material).toString()));
                    addHandForgingRecipes(material);
                    addPBFRecipes(material);
                }
                addForgingRecipes(material, EUt / 4, Math.max(duration, 300));
            }
        }
    }

    private static void addForgingRecipes(Material material, int EUt, int duration) {
        RecipeMaps.FORGE_HAMMER_RECIPES.recipeBuilder()
                .output(RTOrePrefixes.INGOT_ZUKU, material)
                .input(RTOrePrefixes.ZUKU, material, 4)
                .EUt(EUt)
                .duration(duration)
                .buildAndRegister();
        RecipeMaps.FORGE_HAMMER_RECIPES.recipeBuilder()
                .output(RTOrePrefixes.INGOT_SAGEGANE, material)
                .input(RTOrePrefixes.INGOT_ZUKU, material, 4)
                .EUt(EUt)
                .duration(duration)
                .buildAndRegister();
        RecipeMaps.FORGE_HAMMER_RECIPES.recipeBuilder()
                .output(OrePrefix.ingot, material)
                .input(RTOrePrefixes.INGOT_SAGEGANE, material, 2)
                .EUt(EUt)
                .duration(duration)
                .buildAndRegister();
    }

    private static void addHandForgingRecipes(Material material) {
        ModHandler.addShapedRecipe("zuku_ingot_" + material.getName(),
                OreDictUnifier.get(RTOrePrefixes.INGOT_ZUKU, material),
                "hXX", " XX",
                'X', new UnificationEntry(RTOrePrefixes.ZUKU, material)
        );
        ModHandler.addShapedRecipe("sagegane_" + material.getName(),
                OreDictUnifier.get(RTOrePrefixes.INGOT_SAGEGANE, material),
                "hXX", " XX",
                'X', new UnificationEntry(RTOrePrefixes.INGOT_ZUKU, material)
        );
        ModHandler.addShapedRecipe("sagegane_to_ingot" + material.getName(),
                OreDictUnifier.get(OrePrefix.ingot, material),
                "h", "X", "X",
                'X', new UnificationEntry(RTOrePrefixes.INGOT_SAGEGANE, material)
        );
    }
    private static void addPBFRecipes(Material material){
        RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder()
                .input(OrePrefix.dust, material)
                .input(OrePrefix.dust, Materials.Coal,4)
                .output(RTOrePrefixes.ZUKU, material, 4)
                .duration(720)
                .buildAndRegister();
        RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder()
                .input(OrePrefix.dust, material)
                .input(OrePrefix.dust, Materials.Charcoal,4)
                .output(RTOrePrefixes.ZUKU, material, 4)
                .duration(720)
                .buildAndRegister();
        RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder()
                .input(OrePrefix.dust, material)
                .input(OrePrefix.dust, Materials.Coke,2)
                .output(RTOrePrefixes.ZUKU, material, 4)
                .duration(360)
                .buildAndRegister();
    }
    private static void replaceFurnaceRecipes(Material material) {
        ItemStack ingot = OreDictUnifier.get(OrePrefix.ingot, material);
        ItemStack zuku = OreDictUnifier.get(RTOrePrefixes.ZUKU, material);
        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            if (ingot.isItemEqual(entry.getValue())) {
                RTConstants.LOGGER.info(entry);
                entry.setValue(zuku);
            }
        }
    }
}