package prrandomthings.recipes;

import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.fluids.store.FluidStorageKeys;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidStack;
import prrandomthings.constants.RTConstants;
import prrandomthings.materials.RTOrePrefixes;
import prrandomthings.utils.RTUtilities;

import java.util.Map;

public class ZukuRecipes {
    public static void generateZukuRecipes() {
        for (Material material : GregTechAPI.materialManager.getRegisteredMaterials()) {
            if (RTOrePrefixes.generateZuku(material)) {
                ItemStack ingot = OreDictUnifier.get(OrePrefix.ingot, material);
                ItemStack zuku = OreDictUnifier.get(RTOrePrefixes.ZUKU, material);
                for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
                    if (ingot.isItemEqual(entry.getValue())) {
                        RTConstants.LOGGER.info(entry);
                        entry.setValue(zuku);
                    }
                }

                if (material.hasProperty(PropertyKey.BLAST)) {
                    BlastProperty property = material.getProperty(PropertyKey.BLAST);
                    int blastTemp = property.getBlastTemperature();
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
                    } else {
                        BlastProperty.GasTier gasTier = property.getGasTier();
                        int duration = property.getDurationOverride();
                        if (duration <= 0) {
                            duration = Math.max(1, (int) (material.getMass() * (long) blastTemp / 50L));
                        }

                        int EUt = property.getEUtOverride();
                        if (EUt <= 0) {
                            EUt = GTValues.VA[2];
                        }
                        RTUtilities.removeRecipeWithOutput(RecipeMaps.BLAST_RECIPES,
                                itemStack -> OreDictUnifier.hasOreDictionary(itemStack,
                                        new UnificationEntry(OrePrefix.ingot, material).toString()));
                        BlastRecipeBuilder blastBuilder = RecipeMaps.BLAST_RECIPES.recipeBuilder()
                                .input(OrePrefix.dust, material)
                                .output(RTOrePrefixes.ZUKU, material, 4)
                                .blastFurnaceTemp(blastTemp).EUt(EUt);
                        if (gasTier != null) {
                            FluidStack gas = CraftingComponent.EBF_GASES.get(gasTier).copy();
                            blastBuilder.copy()
                                    .circuitMeta(1)
                                    .duration(duration)
                                    .buildAndRegister();
                            blastBuilder.copy()
                                    .circuitMeta(2)
                                    .fluidInputs(new FluidStack[]{gas})
                                    .duration((int) ((double) duration * 0.67))
                                    .buildAndRegister();
                        } else {
                            blastBuilder.duration(duration);
                            if (material == Materials.Silicon) {
                                blastBuilder.circuitMeta(1);
                            }

                            blastBuilder.buildAndRegister();
                        }
                    }
                }
                addForgingRecipes(material);
            }
        }
    }

    private static void addForgingRecipes(Material material) {
        RecipeMaps.FORGE_HAMMER_RECIPES.recipeBuilder()
                .output(RTOrePrefixes.INGOT_ZUKU, material)
                .input(RTOrePrefixes.ZUKU, material, 4)
                .EUt(30)
                .duration(300)
                .buildAndRegister();
        RecipeMaps.FORGE_HAMMER_RECIPES.recipeBuilder()
                .output(RTOrePrefixes.INGOT_SAGEGANE, material)
                .input(RTOrePrefixes.INGOT_ZUKU, material, 4)
                .EUt(30)
                .duration(300)
                .buildAndRegister();
        RecipeMaps.FORGE_HAMMER_RECIPES.recipeBuilder()
                .output(OrePrefix.ingot, material)
                .input(RTOrePrefixes.INGOT_SAGEGANE, material, 2)
                .EUt(30)
                .duration(300)
                .buildAndRegister();
    }
}