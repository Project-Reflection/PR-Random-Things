package prrandomthings.mte;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.IGhostSlotConfigurable;
import gregtech.api.capability.impl.GhostCircuitItemStackHandler;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.GhostCircuitSlotWidget;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapPrimitiveMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Arrays;
import java.util.function.Function;

public class MteCustomPrimitiveMultiblock extends RecipeMapPrimitiveMultiblockController implements IGhostSlotConfigurable {
    private static final int FONT_HEIGHT = 9;
    //endregion
    protected final ICubeRenderer baseTexture;
    protected final ICubeRenderer overlay;
    protected final Function<TraceabilityPredicate,BlockPattern> structurePatternFactory;
    protected GhostCircuitItemStackHandler circuitInventory;
    private IItemHandlerModifiable actualImportItems;

    protected MteCustomPrimitiveMultiblock(ResourceLocation metaTileEntityId,
                                           RecipeMap<?> recipeMap,
                                           ICubeRenderer baseTexture,
                                           ICubeRenderer overlay,
                                           Function<TraceabilityPredicate,BlockPattern> structurePatternFactory) {
        super(metaTileEntityId, recipeMap);
        this.baseTexture = baseTexture;
        this.overlay = overlay;
        this.structurePatternFactory = structurePatternFactory;
    }
    protected TextureArea getCircuitSlotOverlay() {
        return GuiTextures.INT_CIRCUIT_OVERLAY;
    }

    // Method provided to override
    protected void getCircuitSlotTooltip(SlotWidget widget) {
        String configString;
        if (circuitInventory == null || circuitInventory.getCircuitValue() == GhostCircuitItemStackHandler.NO_CONFIG) {
            configString = new TextComponentTranslation("gregtech.gui.configurator_slot.no_value").getFormattedText();
        } else {
            configString = String.valueOf(circuitInventory.getCircuitValue());
        }

        widget.setTooltipText("gregtech.gui.configurator_slot.tooltip", configString);
    }
    @Override
    protected ModularUI.Builder createUITemplate(EntityPlayer entityPlayer) {
        //return super.createUITemplate(entityPlayer);
        RecipeMap<?> workableRecipeMap=this.recipeMapWorkable.getRecipeMap();
        assert workableRecipeMap != null;
        int totalInputs=workableRecipeMap.getMaxInputs() + workableRecipeMap.getMaxFluidInputs();
        int totalOutputs=workableRecipeMap.getMaxOutputs() + workableRecipeMap.getMaxFluidOutputs();
        int yOffset = FONT_HEIGHT * Math.max(0,(int)Math.ceil(Math.sqrt(Math.max(totalOutputs,totalInputs)))-2);

        ModularUI.Builder builder= this.recipeMapWorkable.getRecipeMap()
                .createUITemplate(recipeMapWorkable::getProgressPercent,importItems,exportItems,
                        importFluids,exportFluids,yOffset)
                .widget(new LabelWidget(5, 5, getMetaFullName()))
                .bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT,7+yOffset/2,84+2*yOffset);

        if (this.circuitInventory != null) {
                SlotWidget circuitSlot = new GhostCircuitSlotWidget(circuitInventory, 0, 150+yOffset/2,  64 + yOffset*2)
                        .setBackgroundTexture(GuiTextures.SLOT, getCircuitSlotOverlay());
                builder.widget(circuitSlot.setConsumer(this::getCircuitSlotTooltip));
        }
        return builder;
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return structurePatternFactory.apply(selfPredicate());
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
        return new MteCustomPrimitiveMultiblock(this.metaTileEntityId,this.getRecipeMap(),baseTexture,overlay, structurePatternFactory);
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

    @Override
    public boolean hasGhostCircuitInventory() {
        return true;
    }

    @Override
    public void setGhostCircuitConfig(int config) {
        if (this.circuitInventory == null || this.circuitInventory.getCircuitValue() == config) {
            return;
        }
        this.circuitInventory.setCircuitValue(config);
        if (!getWorld().isRemote) {
            markDirty();
        }
    }

    @Override
    protected void initializeInventory() {
        super.initializeInventory();
        if (this.hasGhostCircuitInventory()) {
            this.circuitInventory = new GhostCircuitItemStackHandler(this);
            this.circuitInventory.addNotifiableMetaTileEntity(this);
        }

        this.actualImportItems = null;
    }

    @Override
    public IItemHandlerModifiable getImportItems() {
        if (this.actualImportItems == null) {
            this.actualImportItems = this.circuitInventory == null ?
                    super.getImportItems() :
                    new ItemHandlerList(Arrays.asList(super.getImportItems(), this.circuitInventory));
        }
        return this.actualImportItems;

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data= super.writeToNBT(data);
        this.circuitInventory.write(data);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.circuitInventory.read(data);
    }
}
