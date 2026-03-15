package prrandomthings.recipes.logic;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.FuelRecipeLogic;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.recipeproperties.IRecipePropertyStorage;
import org.jetbrains.annotations.NotNull;
import prrandomthings.mte.MteCustomGenerator;
import prrandomthings.utils.PREnvironment;

import java.util.function.Supplier;

public class MagicGeneratorRecipeLogic extends FuelRecipeLogic {
    private final MteCustomGenerator tileEntity;
    public MagicGeneratorRecipeLogic(MteCustomGenerator tileEntity,
                                     RecipeMap<?> recipeMap,
                                     Supplier<IEnergyContainer> energyContainer) {
        super(tileEntity, recipeMap, energyContainer);
        this.tileEntity=tileEntity;
    }

    @Override
    protected void updateRecipeProgress() {
        double env= PREnvironment.getEnvironmentFactor(tileEntity.getWorld(),tileEntity.getPos());
        double envVoltage=8.*Math.pow(4,env);
        if(envVoltage > recipeEUt) {
            super.updateRecipeProgress();
        }
    }

    @Override
    protected void modifyOverclockPre(@NotNull int[] values, @NotNull IRecipePropertyStorage storage) {
        super.modifyOverclockPre(values, storage);
    }

    @Override
    public long getMaxVoltage() {
        double env= PREnvironment.getEnvironmentFactor(tileEntity.getWorld(),tileEntity.getPos());
        double envVoltage=8.*Math.pow(4,env);
        return Math.min(super.getMaxVoltage(),(long) envVoltage);
    }
}
