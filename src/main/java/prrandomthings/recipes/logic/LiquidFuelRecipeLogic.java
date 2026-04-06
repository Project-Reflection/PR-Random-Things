package prrandomthings.recipes.logic;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.recipeproperties.IRecipePropertyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import prrandomthings.utils.PREnvironment;

public class LiquidFuelRecipeLogic extends NoEnergyRecipeLogic {

    private final FluidStack fuel;
    private final boolean allowPassiveRun;
    private final boolean randomize;
    public LiquidFuelRecipeLogic(MetaTileEntity tileEntity,
                                 RecipeMap<?> recipeMap,
                                 FluidStack fuel, boolean allowPassiveRun,
                                 boolean randomize) {
        super(tileEntity, recipeMap);
        this.fuel = fuel;
        this.allowPassiveRun = allowPassiveRun;
        this.randomize = randomize;
    }



    @Override
    protected void modifyOverclockPost(int[] overclockResults, @NotNull IRecipePropertyStorage storage) {
        super.modifyOverclockPost(overclockResults, storage);
        if(this.randomize) {
            overclockResults[1] = (int) Math.round(overclockResults[1] *
                    (1. + PREnvironment.getEnvironmentFactor(this.metaTileEntity.getWorld(),
                            this.metaTileEntity.getPos())));
        }
    }

    @Override
    protected void updateRecipeProgress() {
        if (this.canRecipeProgress && this.metaTileEntity.getOffsetTimer() % 4 == 0) {
            IFluidHandler provider = this.getInputTank();
            FluidStack availableSource = provider.drain(fuel, false);
            if (availableSource != null) {
                var drained = provider.drain(availableSource, true);
                assert drained != null;
                this.progressTime += drained.amount;
                if (this.hasNotEnoughEnergy) {
                    this.hasNotEnoughEnergy = false;
                }
            } else if (this.allowPassiveRun) {
                this.progressTime++;
            } else {
                this.hasNotEnoughEnergy = true;
                this.decreaseProgress();
            }
            if (!this.hasNotEnoughEnergy && this.progressTime > this.maxProgressTime) {
                this.completeRecipe();
            }
        }
    }
}
