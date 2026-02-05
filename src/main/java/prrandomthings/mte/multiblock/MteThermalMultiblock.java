package prrandomthings.mte.multiblock;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.util.ResourceLocation;
import prrandomthings.api.IHeater;
import prrandomthings.api.PRMultiblockAbility;
import prrandomthings.mte.MteCustomPrimitiveMultiblock;
import prrandomthings.recipes.logic.ThermalRecipeLogic;

import java.util.function.Function;

public class MteThermalMultiblock extends MteCustomPrimitiveMultiblock {

    protected IHeater heater;
    public MteThermalMultiblock(ResourceLocation metaTileEntityId,
                                RecipeMap<?> recipeMap,
                                ICubeRenderer baseTexture,
                                ICubeRenderer overlay,
                                Function<TraceabilityPredicate, BlockPattern> structurePatternFactory) {
        super(metaTileEntityId, recipeMap, baseTexture, overlay, structurePatternFactory);
        this.recipeMapWorkable=new ThermalRecipeLogic(this,recipeMap);
        this.heater=IHeater.DEAD_HEATER;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MteThermalMultiblock(this.metaTileEntityId,
                this.getRecipeMap(),
                this.baseTexture,
                this.overlay,
                this.structurePatternFactory);
    }


    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        var heaters=this.getAbilities(PRMultiblockAbility.HEATER);
        this.heater=heaters.isEmpty()?IHeater.DEAD_HEATER:heaters.get(0);
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.heater=IHeater.DEAD_HEATER;
    }

    public IHeater getHeater() {
        return heater;
    }
}
