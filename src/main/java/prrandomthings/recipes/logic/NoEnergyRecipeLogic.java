package prrandomthings.recipes.logic;

import gregtech.api.GTValues;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.recipeproperties.IRecipePropertyStorage;

import static gregtech.api.recipes.logic.OverclockingLogic.standardOverclockingLogic;

public class NoEnergyRecipeLogic extends AbstractRecipeLogic {
    public NoEnergyRecipeLogic(MetaTileEntity tileEntity, RecipeMap<?> recipeMap) {
        super(tileEntity, recipeMap);
    }

    @Override
    protected long getEnergyInputPerSecond() {
        return Long.MAX_VALUE;
    }

    @Override
    protected long getEnergyStored() {
        return Long.MAX_VALUE;
    }

    @Override
    protected long getEnergyCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    protected boolean drawEnergy(int recipeEUt, boolean simulate) {
        return true;
    }

    @Override
    protected boolean hasEnoughPower(int[] resultOverclock) {
        return true;
    }

    @Override
    public long getMaxVoltage() {
        return GTValues.LV;
    }

    @Override
    protected int[] runOverclockingLogic(IRecipePropertyStorage propertyStorage, int recipeEUt,
                                                   long maxVoltage, int recipeDuration, int amountOC) {
        return standardOverclockingLogic(
                1,
                getMaxVoltage(),
                recipeDuration,
                amountOC,
                getOverclockingDurationDivisor(),
                getOverclockingVoltageMultiplier()

        );
    }

    @Override
    public long getMaximumOverclockVoltage() {
        return GTValues.V[GTValues.LV];
    }

}
