package prrandomthings.mte.magic;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import prrandomthings.constants.RTConstants;
import prrandomthings.mte.MteCustomPrimitiveSingleblock;
import prrandomthings.recipes.logic.LiquidFuelRecipeLogic;
import prrandomthings.recipes.logic.NoEnergyRecipeLogic;

public class MteLiquidFuelSingleblock extends MteCustomPrimitiveSingleblock {


    protected final FluidStack fuel;
    protected final boolean allowPassiveRun;
    protected final boolean randomize;
    public MteLiquidFuelSingleblock(ResourceLocation metaTileEntityId,
                                    RecipeMap<?> recipeMap,
                                    ICubeRenderer baseTexture,
                                    ICubeRenderer overlay,
                                    FluidStack fuel,
                                    boolean allowPassiveRun,
                                    boolean randomize) {
        super(metaTileEntityId, recipeMap, baseTexture, overlay,false);
        this.fuel = fuel;
        this.allowPassiveRun = allowPassiveRun;
        this.randomize = randomize;
        this.recipeMapWorkable = this.initializeRecipeLogic();
        this.initializeAbilities();
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                && side != null ? null : super.getCapability(capability, side);
    }

    @Override
    protected NoEnergyRecipeLogic initializeRecipeLogic() {
        return new LiquidFuelRecipeLogic(this, this.recipeMap, this.fuel, this.allowPassiveRun, this.randomize);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        RTConstants.LOGGER.info("MTE in {} allowNoSourceRun= {}", tileEntity.pos(), this.allowPassiveRun);
        return new MteLiquidFuelSingleblock(this.metaTileEntityId,
                recipeMap,
                baseTexture,
                overlay,
                this.fuel,
                this.allowPassiveRun,
                randomize
        );
    }

}
