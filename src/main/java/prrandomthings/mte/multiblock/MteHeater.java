package prrandomthings.mte.multiblock;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IControllable;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandlerModifiable;
import prrandomthings.api.IHeater;
import prrandomthings.api.PRMultiblockAbility;
import prrandomthings.constants.RTConstants;
import prrandomthings.constants.RTTextures;

import java.util.List;

public class MteHeater extends MetaTileEntityMultiblockPart implements IControllable,
        IMultiblockAbilityPart<IHeater>,IHeater {
    public static final MteHeater SAMPLE = new MteHeater();

    private int fuelBurnTimeLeft;
    private int fuelMaxBurnTime;

    private boolean isBurning;
    private boolean wasBurningAndNeedsUpdate;
    private boolean workingEnabled;

    private MteHeater() {
        super(RTConstants.RTID("heater"), 0);
        this.workingEnabled = true;
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        return new NotifiableItemStackHandler(this, 1,this,false);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MteHeater();
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return ModularUI.builder(GuiTextures.BACKGROUND, 176, 130)
                .label(10, 5, getMetaFullName())
                .widget(new SlotWidget(this.importItems, 0,
                        79, 18, true, true)
                        .setBackgroundTexture(GuiTextures.SLOT))
                .bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 7, 48)
                .build(getHolder(), entityPlayer);
    }

    @Override
    public boolean isValidFrontFacing(EnumFacing facing) {
        return facing != EnumFacing.UP && facing != EnumFacing.DOWN;
    }

    @Override
    public boolean isActive() {
        return isBurning;
    }

    @Override
    public void update() {
        super.update();
        if (!getWorld().isRemote) {
            if(fuelMaxBurnTime >0){
                fuelBurnTimeLeft--;
                if(fuelBurnTimeLeft <= 0){
                    this.fuelMaxBurnTime = 0;
                    this.wasBurningAndNeedsUpdate = true;
                }
            }
            if (fuelMaxBurnTime <= 0 && this.workingEnabled) {
                tryConsumeNewFuel();
                if (fuelBurnTimeLeft > 0) {
                    if (wasBurningAndNeedsUpdate) {
                        this.wasBurningAndNeedsUpdate = false;
                    } else setBurning(true);
                }
            }
            if (wasBurningAndNeedsUpdate) {
                this.wasBurningAndNeedsUpdate = false;
                setBurning(false);
            }
        }
    }

    protected void setBurning(boolean burning) {
        this.isBurning = burning;
        if (!getWorld().isRemote) {
            markDirty();
            writeCustomData(GregtechDataCodes.IS_WORKING, buf -> buf.writeBoolean(burning));
        }
    }

    @Override
    public ICubeRenderer getBaseTexture() {
        MultiblockControllerBase controller = this.getController();
        if (controller != null) {
            return this.hatchTexture = controller.getBaseTexture(this);
        } else if (this.hatchTexture != null) {
            return this.hatchTexture != Textures.getInactiveTexture(this.hatchTexture) ? (this.hatchTexture = Textures.getInactiveTexture(this.hatchTexture)) : this.hatchTexture;
        } else {
            return RTTextures.SMOOTH_SANDSTONE;
        }
    }
    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (shouldRenderOverlay())
            Textures.FURNACE_OVERLAY.renderOrientedState(renderState, translation, pipeline,
                    getFrontFacing(), isActive(), isWorkingEnabled());
    }


    @Override
    public boolean isWorkingEnabled() {
        return this.workingEnabled;
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        this.workingEnabled = workingEnabled;
        World world = getWorld();
        if (world != null && !world.isRemote) {
            writeCustomData(GregtechDataCodes.WORKING_ENABLED, buf -> buf.writeBoolean(workingEnabled));
        }
    }
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        if (capability == GregtechTileCapabilities.CAPABILITY_CONTROLLABLE) {
            return GregtechTileCapabilities.CAPABILITY_CONTROLLABLE.cast(this);
        }
        return super.getCapability(capability, side);
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("FuelBurnTimeLeft", fuelBurnTimeLeft);
        data.setInteger("FuelMaxBurnTime", fuelMaxBurnTime);
        data.setBoolean("WorkingEnabled",workingEnabled);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.fuelBurnTimeLeft = data.getInteger("FuelBurnTimeLeft");
        this.fuelMaxBurnTime = data.getInteger("FuelMaxBurnTime");
        this.workingEnabled=data.getBoolean("WorkingEnabled");
        this.isBurning = fuelBurnTimeLeft > 0;
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeBoolean(isBurning);
        buf.writeBoolean(workingEnabled);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.isBurning = buf.readBoolean();
        this.workingEnabled=buf.readBoolean();
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == GregtechDataCodes.IS_WORKING) {
            this.isBurning = buf.readBoolean();

        }
        if(dataId == GregtechDataCodes.WORKING_ENABLED){
            this.workingEnabled = buf.readBoolean();
        }
        scheduleRenderUpdate();
    }

    protected void setFuelMaxBurnTime(int fuelMaxBurnTime) {
        this.fuelMaxBurnTime = fuelMaxBurnTime;
        this.fuelBurnTimeLeft = fuelMaxBurnTime;
        if (!getWorld().isRemote) {
            markDirty();
        }
    }
    protected void tryConsumeNewFuel() {
        ItemStack fuelInSlot = importItems.extractItem(0, 1, true);
        if (fuelInSlot.isEmpty()) return;
        // Prevent consuming buckets with burn time
        if (FluidUtil.getFluidHandler(fuelInSlot) != null) {
            return;
        }
        int burnTime = TileEntityFurnace.getItemBurnTime(fuelInSlot);
        if (burnTime <= 0) return;
        importItems.extractItem(0, 1, false);
        setFuelMaxBurnTime(burnTime);
    }

    @Override
    public MultiblockAbility<IHeater> getAbility() {
        return PRMultiblockAbility.HEATER;
    }

    @Override
    public void registerAbilities(List<IHeater> abilityList) {
        abilityList.add(this);
    }
}
