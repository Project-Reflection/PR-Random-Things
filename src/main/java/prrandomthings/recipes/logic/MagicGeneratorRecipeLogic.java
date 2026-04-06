package prrandomthings.recipes.logic;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.FuelRecipeLogic;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.recipeproperties.IRecipePropertyStorage;
import org.jetbrains.annotations.NotNull;
import prrandomthings.mte.MteCustomGenerator;

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
    protected void modifyOverclockPost(int[] overclockResults, @NotNull IRecipePropertyStorage storage) {
        super.modifyOverclockPost(overclockResults, storage);
        //0：能量 1：时间

    }
}
