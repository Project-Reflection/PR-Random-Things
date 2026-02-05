package prrandomthings.recipes.logic;

import gregtech.api.capability.impl.PrimitiveRecipeLogic;
import gregtech.api.recipes.RecipeMap;
import org.jetbrains.annotations.NotNull;
import prrandomthings.mte.multiblock.MteThermalMultiblock;

public class ThermalRecipeLogic extends PrimitiveRecipeLogic {
    private final MteThermalMultiblock tileEntity;
    public ThermalRecipeLogic(MteThermalMultiblock tileEntity, RecipeMap<?> recipeMap) {
        super(tileEntity, recipeMap);
        this.tileEntity=tileEntity;
    }

    @Override
    protected boolean drawEnergy(int recipeEUt, boolean simulate) {
        return tileEntity.getHeater().isActive();
    }

    @Override
    protected boolean hasEnoughPower(int @NotNull [] resultOverclock) {
        return tileEntity.getHeater().isActive();
    }

    @Override
    protected long getEnergyInputPerSecond() {
        return tileEntity.getHeater().isActive()?super.getEnergyInputPerSecond():0;
    }

    @Override
    protected long getEnergyStored() {
        return tileEntity.getHeater().isActive()?super.getEnergyStored():0;
    }
}
