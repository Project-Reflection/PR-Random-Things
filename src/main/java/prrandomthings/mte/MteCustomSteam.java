package prrandomthings.mte;

import gregtech.api.GTValues;
import gregtech.api.capability.IGhostSlotConfigurable;
import gregtech.api.capability.impl.*;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.GhostCircuitSlotWidget;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SteamMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import prrandomthings.materials.RTMaterials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MteCustomSteam extends SteamMetaTileEntity implements IGhostSlotConfigurable {
    private static final int FONT_HEIGHT = 9;
    protected GhostCircuitItemStackHandler circuitInventory;
    private IItemHandlerModifiable actualImportItems;
    private FluidTankList actualImportFluids;
    public MteCustomSteam(ResourceLocation metaTileEntityId,
                          RecipeMap<?> recipeMap,
                          ICubeRenderer renderer,
                          boolean isHighPressure) {
        super(metaTileEntityId, recipeMap, renderer, isHighPressure);
        super.initializeInventory();
        this.workableHandler = new RecipeLogicSteam(this,
                recipeMap, isHighPressure, steamFluidTank, isHighPressure ? 1.0 : 0.5){
            @Override
            public long getMaxVoltage() {
                return GTValues.V[isHighPressure? GTValues.LV: GTValues.ULV];
            }
        };

    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MteCustomSteam(metaTileEntityId, getRecipeMap(), renderer, isHighPressure);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return createUITemplate(entityPlayer).build(this.getHolder(), entityPlayer);
    }

    @Override
    public ModularUI.Builder createUITemplate(EntityPlayer player) {
        RecipeMap<?> recipeMap = this.getRecipeMap();
        assert recipeMap != null;
        int totalInputs = recipeMap.getMaxInputs() + recipeMap.getMaxFluidInputs();
        int totalOutputs = recipeMap.getMaxOutputs() + recipeMap.getMaxFluidOutputs();
        int yOffset = FONT_HEIGHT * Math.max(0, (int) Math.ceil(Math.sqrt(Math.max(totalOutputs, totalInputs))) - 2);

        ModularUI.Builder builder = recipeMap.createUITemplate(workableHandler::getProgressPercent, importItems, exportItems,
                        actualImportFluids, exportFluids, yOffset)
                .widget(new LabelWidget(5, 5, getMetaFullName()))
                .bindPlayerInventory(player.inventory, GuiTextures.SLOT, 7 + yOffset / 2, 84 + 2 * yOffset);

        if (this.circuitInventory != null) {
            SlotWidget circuitSlot = new GhostCircuitSlotWidget(circuitInventory, 0, 150 + yOffset / 2, 64 + yOffset * 2)
                    .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.INT_CIRCUIT_OVERLAY);
            builder.widget(circuitSlot.setConsumer(this::getCircuitSlotTooltip));
        }
        return builder;
    }

    private List<FluidTank> makeFluidTanks(int length, boolean isExport) {
        List<FluidTank> fluidTankList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            fluidTankList.add(new NotifiableFluidTank(32000, this, isExport));
        }
        return fluidTankList;
    }

    @Override
    public FluidTankList createImportFluidHandler() {
        if (isHighPressure) {
            this.steamFluidTank = new FilteredFluidHandler(STEAM_CAPACITY).setFilter(CommonFluidFilters.STEAM);
        } else {
            this.steamFluidTank = new FilteredFluidHandler(STEAM_CAPACITY).setFilter(new SingleFluidFilter(
                    RTMaterials.LOW_QUALITY_STEAM.getFluid(1), false
            ));
        }
        var recipeMap = this.getRecipeMap();
        var fluidTankList = recipeMap == null
                ? new ArrayList<FluidTank>()
                : makeFluidTanks(recipeMap.getMaxFluidInputs(), false);
        this.actualImportFluids=new FluidTankList(true,fluidTankList);
        return new FluidTankList(true, actualImportFluids,this.steamFluidTank);
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

    private void getCircuitSlotTooltip(SlotWidget slotWidget) {
        String configString;
        if (circuitInventory == null || circuitInventory.getCircuitValue() == GhostCircuitItemStackHandler.NO_CONFIG) {
            configString = new TextComponentTranslation("gregtech.gui.configurator_slot.no_value").getFormattedText();
        } else {
            configString = String.valueOf(circuitInventory.getCircuitValue());
        }

        slotWidget.setTooltipText("gregtech.gui.configurator_slot.tooltip", configString);
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
    public FluidTankList getImportFluids() {
        return actualImportFluids;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data = super.writeToNBT(data);
        this.circuitInventory.write(data);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.circuitInventory.read(data);
        //throw new ChunkSupressionException(this.getPos());
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        var recipeMap = getRecipeMap();
        return recipeMap == null ?
                super.createImportItemHandler() :
                new NotifiableItemStackHandler(this, recipeMap.getMaxInputs(),
                        this, false);
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        var recipeMap = getRecipeMap();
        return recipeMap == null ?
                super.createImportItemHandler() :
                new NotifiableItemStackHandler(this, recipeMap.getMaxOutputs(), this,
                        true);
    }

    @Override
    protected FluidTankList createExportFluidHandler() {
        var recipeMap = getRecipeMap();
        return recipeMap == null ?
                super.createExportFluidHandler() :
                new FluidTankList(false,
                        makeFluidTanks(recipeMap.getMaxFluidOutputs(), true));
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
}
