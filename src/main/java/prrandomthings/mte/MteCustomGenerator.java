package prrandomthings.mte;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleGeneratorMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.client.particle.VanillaParticleEffects;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.PipelineUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

public class MteCustomGenerator extends SimpleGeneratorMetaTileEntity {

    protected MteCustomGenerator(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, ICubeRenderer texture,int tier) {
        super(metaTileEntityId, recipeMap,texture,
                tier, GTUtility.genericGeneratorTankSizeFunction);
    }
    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MteCustomGenerator(this.metaTileEntityId,this.recipeMap,this.renderer,getTier());
    }

    @Override
    public boolean isValidFrontFacing(EnumFacing facing) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick() {
        if (isActive()) {
            VanillaParticleEffects.RANDOM_SPARKS.runEffect(this);
        }
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        IVertexOperation[] colouredPipeline = ArrayUtils.add(pipeline,
                new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering())));
        getBaseRenderer().render(renderState, translation, colouredPipeline);
        try {
            this.renderer.renderOrientedState(renderState, translation, pipeline,
                    getFrontFacing().rotateY(),
                    workable.isActive(),
                    workable.isWorkingEnabled());
        }catch (IllegalStateException e)
        {
            this.renderer.renderOrientedState(renderState, translation, pipeline,
                    EnumFacing.NORTH,
                    workable.isActive(),
                    workable.isWorkingEnabled());
        }
        Textures.ENERGY_OUT.renderSided(getFrontFacing(), renderState, translation,
                PipelineUtil.color(pipeline, GTValues.VC[getTier()]));
    }
}
