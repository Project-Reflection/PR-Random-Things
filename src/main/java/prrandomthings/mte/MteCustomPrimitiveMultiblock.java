package prrandomthings.mte;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapPrimitiveMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.SimpleCubeRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prrandomthings.RTConstants;
import prrandomthings.config.RTRecipeMaps;

import java.util.function.Function;

public class MteCustomPrimitiveMultiblock extends RecipeMapPrimitiveMultiblockController {
    private static final ICubeRenderer WOODEN_PLANKS =new SimpleCubeRenderer("minecraft:blocks/planks_oak");
    //region declarations
    public static final MteCustomPrimitiveMultiblock COMPOSTING_BARREL =new MteCustomPrimitiveMultiblock(RTConstants.RTID("composting_barrel"),
            RTRecipeMaps.COMPOSTING_BARREL,WOODEN_PLANKS,Textures.FERMENTER_OVERLAY,
            selfPredicate->FactoryBlockPattern.start()
            .aisle("CCC","CCC","CCC","CCC")
            .aisle("CCC","C#C","C#C","C#C")
            .aisle("CCC","C@C","CCC","CCC")
            .where('C',states(Blocks.PLANKS.getDefaultState()))
            .where('#',air())
            .where('@',selfPredicate)
            .build());
    public static final MteCustomPrimitiveMultiblock SIEVE= new MteCustomPrimitiveMultiblock(RTConstants.RTID("sieve"
    ), RTRecipeMaps.SIEVE, WOODEN_PLANKS, Textures.SIFTER_OVERLAY, selfPredicate->FactoryBlockPattern.start()
            .aisle("C   C","C   C","CCCCC","CCCCC","CCCCC")
            .aisle("     ","     ","CSSSC","C###C","C###C")
            .aisle("     ","     ","CSSSC","C###C","C###C")
            .aisle("     ","     ","CSSSC","C###C","C###C")
            .aisle("C   C","C   C","CC@CC","CCCCC","CCCCC")
            .where('C',states(Blocks.PLANKS.getDefaultState()))
            .where('S',states(Blocks.WOODEN_SLAB.getDefaultState()))
            .where(' ',any())
            .where('#',air())
            .where('@',selfPredicate)
            .build());
    private static final int FONT_HEIGHT = 9;
    //endregion
    private final ICubeRenderer baseTexture;
    private final ICubeRenderer overlay;
    private final Function<TraceabilityPredicate,BlockPattern> structurePattern;

    protected MteCustomPrimitiveMultiblock(ResourceLocation metaTileEntityId,
                                           RecipeMap<?> recipeMap,
                                           ICubeRenderer baseTexture,
                                           ICubeRenderer overlay,
                                           Function<TraceabilityPredicate,BlockPattern> structurePattern) {
        super(metaTileEntityId, recipeMap);
        this.baseTexture = baseTexture;
        this.overlay = overlay;
        this.structurePattern = structurePattern;
    }

    @Override
    protected ModularUI.Builder createUITemplate(EntityPlayer entityPlayer) {
        //return super.createUITemplate(entityPlayer);
        RecipeMap<?> workableRecipeMap=this.recipeMapWorkable.getRecipeMap();
        assert workableRecipeMap != null;
        int yOffset = 0;
        if (workableRecipeMap.getMaxInputs() >= 6 || workableRecipeMap.getMaxFluidInputs() >= 6 ||
                workableRecipeMap.getMaxOutputs() >= 6 || workableRecipeMap.getMaxFluidOutputs() >= 6) {
            yOffset = FONT_HEIGHT;
        }
        return this.recipeMapWorkable.getRecipeMap()
                .createUITemplate(recipeMapWorkable::getProgressPercent,importItems,exportItems,
                        importFluids,exportFluids,yOffset)
                .widget(new LabelWidget(5, 5, getMetaFullName()))
                .bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT,yOffset);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return structurePattern.apply(selfPredicate());
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return baseTexture;
    }
    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(),
                recipeMapWorkable.isActive(), recipeMapWorkable.isWorkingEnabled());
    }
    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MteCustomPrimitiveMultiblock(this.metaTileEntityId,this.getRecipeMap(),baseTexture,overlay,structurePattern);
    }
    @SideOnly(Side.CLIENT)
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return overlay;
    }
    @Override
    public boolean hasMaintenanceMechanics() {
        return false;
    }
}
