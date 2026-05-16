package prrandomthings.mte.multiblock;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.impl.CommonFluidFilters;
import gregtech.api.capability.impl.FilteredFluidHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockSteamCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import prrandomthings.constants.RTConstants;
import prrandomthings.materials.RTMaterials;

import java.util.List;

public class MteSteamPump extends MultiblockWithDisplayBase {
    public static final MteSteamPump SAMPLE = new MteSteamPump();

    private IFluidTank waterTank;
    private IFluidTank steamTank;

    private int cooldown;
    private MteSteamPump() {
        super(RTConstants.RTID("steam_pump"));
        resetTileAbilities();
    }

    @Override
    protected void updateFormedValid() {

    }


    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        initializeAbilities();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        resetTileAbilities();
    }

    private void initializeAbilities() {
        //initializeInventory();
        List<IFluidTank> tanks = getAbilities(MultiblockAbility.PUMP_FLUID_HATCH);
        if (tanks == null || tanks.isEmpty()) {
            tanks = getAbilities(MultiblockAbility.EXPORT_FLUIDS);
        }
        this.waterTank = tanks.get(0);
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        this.steamTank = new FilteredFluidHandler(1000)
                .setFilter(fluidStack -> CommonFluidFilters.matchesFluid(fluidStack, Materials.Steam)
                        || CommonFluidFilters.matchesFluid(fluidStack, RTMaterials.LOW_QUALITY_STEAM));
        return new FluidTankList(false, steamTank);
    }

    private void resetTileAbilities() {
        this.waterTank = new FluidTank(0);
    }

    @NotNull
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXX", "**F*", "**F*")
                .aisle("XXHX", "F**F", "FFFF")
                .aisle("SXXX", "**F*", "**F*")
                .where('S', selfPredicate())
                .where('X', states(MetaBlocks.STEAM_CASING.getState(BlockSteamCasing.SteamCasingType.PUMP_DECK)))
                .where('F', frames(Materials.Bronze))
                .where('H',
                        abilities(MultiblockAbility.PUMP_FLUID_HATCH).or(metaTileEntities(
                                MetaTileEntities.FLUID_EXPORT_HATCH[0], MetaTileEntities.FLUID_EXPORT_HATCH[1])))
                .where('*', any())
                .build();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.PRIMITIVE_PUMP;
    }

    @SideOnly(Side.CLIENT)
    @NotNull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.PRIMITIVE_PUMP_OVERLAY;
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        this.getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(), true,
                true);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MteSteamPump();
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return false;
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (steamTank.getFluid() != null) {
            textList.add(new TextComponentString(steamTank.getFluid().getLocalizedName() + ":" + steamTank.getFluidAmount()));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data = super.writeToNBT(data);
        data.setInteger("Cooldown",cooldown);
        RTConstants.LOGGER.info("NBT written: {}", data);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        cooldown=data.getInteger("Cooldown");
        RTConstants.LOGGER.info("NBT read: {}", data);
    }

    @Override
    public void update() {
        super.update();
        if(cooldown >0){
            cooldown--;
            return;
        }
        FluidStack steam = steamTank.getFluid();
        if (steam == null) {
            return;
        }
        int volume = steamTank.getFluidAmount();
        boolean isHighPressure = steam.isFluidEqual(Materials.Steam.getFluid(1));
        int multiplier = isHighPressure ? 2 : 1;
        if (volume * multiplier >= 1000 && waterTank.fill(Materials.Water.getFluid(volume * multiplier), false) > 0) {
            steamTank.drain(volume, true);
            waterTank.fill(Materials.Water.getFluid(volume * multiplier), true);
            getWorld().playSound(null, this.getPos(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 0.8f);
            cooldown=volume>1000?40:20;
        }
    }
}
