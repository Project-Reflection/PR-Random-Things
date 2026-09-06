package prrandomthings.mte;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import gregtech.api.capability.IGhostSlotConfigurable;
import gregtech.api.capability.impl.*;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.resources.IGuiTexture;
import gregtech.api.gui.widgets.GhostCircuitSlotWidget;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.MTETrait;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.cube.SimpleOrientedCubeRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.ArrayUtils;
import prrandomthings.recipes.logic.NoEnergyRecipeLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MteCustomPrimitiveSingleblock extends MetaTileEntity implements IGhostSlotConfigurable {
    private static final int FONT_HEIGHT = 9;

    protected final ICubeRenderer baseTexture;
    protected final ICubeRenderer overlay;
    protected GhostCircuitItemStackHandler circuitInventory;
    private IItemHandlerModifiable actualImportItems;
    protected RecipeMap<?> recipeMap;

    protected NoEnergyRecipeLogic recipeMapWorkable;

    protected MteCustomPrimitiveSingleblock(ResourceLocation metaTileEntityId,
                                         RecipeMap<?> recipeMap,
                                         ICubeRenderer baseTexture,
                                         ICubeRenderer overlay,
                                         boolean doInitializeAbilities)
    {
        super(metaTileEntityId);
        this.baseTexture=baseTexture;
        this.overlay=overlay;
        this.recipeMap=recipeMap;
        if(doInitializeAbilities) {
            this.recipeMapWorkable = this.initializeRecipeLogic();
            this.initializeAbilities();
        }
    }
    public MteCustomPrimitiveSingleblock(ResourceLocation metaTileEntityId,
                                         RecipeMap<?> recipeMap,
                                         ICubeRenderer baseTexture,
                                         ICubeRenderer overlay){
        this(metaTileEntityId,recipeMap,baseTexture,overlay,true);

    }
    protected NoEnergyRecipeLogic initializeRecipeLogic(){
        return new NoEnergyRecipeLogic(this,recipeMap);
    }

    protected void initializeAbilities() {
        this.importItems = new NotifiableItemStackHandler(this, this.recipeMap.getMaxInputs(), this,
                false);
        this.importFluids = new FluidTankList(true,
                makeFluidTanks(this.recipeMap.getMaxFluidInputs(), false));
        this.exportItems = new NotifiableItemStackHandler(this, this.recipeMap.getMaxOutputs(), this,
                true);
        this.exportFluids = new FluidTankList(false,
                makeFluidTanks(this.recipeMap.getMaxFluidOutputs(), true));

        this.itemInventory = new ItemHandlerProxy(this.importItems, this.exportItems);
        this.fluidInventory = new FluidHandlerProxy(this.importFluids, this.exportFluids);
    }
    private List<FluidTank> makeFluidTanks(int length, boolean isExport) {
        List<FluidTank> fluidTankList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            fluidTankList.add(new NotifiableFluidTank(32000, this, isExport));
        }
        return fluidTankList;
    }

    @Override
    public boolean isActive() {
        return this.recipeMapWorkable.isWorkingEnabled() && this.recipeMapWorkable.isActive();
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
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MteCustomPrimitiveSingleblock(this.metaTileEntityId,this.recipeMap,this.baseTexture,this.overlay);
    }

    protected ModularUI.Builder createUIBuilder(EntityPlayer entityPlayer) {
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
    protected ModularUI createUI(EntityPlayer entityPlayer){
        return createUIBuilder(entityPlayer).build(this.getHolder(),entityPlayer);
    }
    private void getCircuitSlotTooltip(SlotWidget slotWidget) {
        String configString;
        if (circuitInventory == null || circuitInventory.getCircuitValue() == GhostCircuitItemStackHandler.NO_CONFIG) {
            configString = new TextComponentTranslation("gregtech.gui.configurator_slot.no_value").getFormattedText();
        } else {
            configString = String.valueOf(circuitInventory.getCircuitValue());
        }

        slotWidget.setTooltipText("gregtech.gui.configurator_slot.tooltip", configString);
    }

    private IGuiTexture getCircuitSlotOverlay() {
        return GuiTextures.INT_CIRCUIT_OVERLAY;
    }
    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        pipeline = ArrayUtils.add(pipeline,
                new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering())));
        if (baseTexture instanceof SimpleOrientedCubeRenderer) {
            baseTexture.renderOriented(renderState, translation, pipeline, getFrontFacing());
        } else {
            baseTexture.render(renderState, translation, pipeline);
        }
        getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(),
                recipeMapWorkable.isActive(), recipeMapWorkable.isWorkingEnabled());
    }

    private ICubeRenderer getFrontOverlay() {
        return overlay;
    }
    @Override
    protected boolean shouldUpdate(MTETrait trait) {
        return !(trait instanceof PrimitiveRecipeLogic);
    }

    @Override
    public SoundEvent getSound() {
        return recipeMap.getSound();
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
