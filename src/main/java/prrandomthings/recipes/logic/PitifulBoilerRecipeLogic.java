package prrandomthings.recipes.logic;

import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import prrandomthings.recipes.RTRecipeMaps;

public class PitifulBoilerRecipeLogic extends NoEnergyRecipeLogic {
    public PitifulBoilerRecipeLogic(MetaTileEntity tileEntity) {
        super(tileEntity, RTRecipeMaps.PITIFUL_BOILER);
    }

    public int heat;
    private static final int MAX_HEAT = 10000;
    private int burnTime;
    private int deltaHeat;

    @Override
    public void update() {
        super.update();

        if (this.burnTime <= 0) {
            this.deltaHeat = 0;
            IItemHandlerModifiable importInventory = getInputInventory();
            ItemStack stack = importInventory.getStackInSlot(0);
            int fuelBurnTime = TileEntityFurnace.getItemBurnTime(stack);
            if (fuelBurnTime > 0) {
                this.burnTime = fuelBurnTime;
                int eff = 1 + (stack.getCount() / 2);
                this.deltaHeat = 50* eff * (eff + 1) * stack.getCount() / 2;
                stack.setCount(0);//吃掉所有燃料
                this.setActive(true);
            }
        }

        if (burnTime > 0) {
            --burnTime;
            if (this.getMetaTileEntity().getOffsetTimer() % 20 == 0) {
                this.heat += deltaHeat*(getHeatPercentage() > 0.5f?2:1);
            }
            if (this.heat > MAX_HEAT) {
                this.metaTileEntity.doExplosion(4.0f);
            }
        } else if (this.getMetaTileEntity().getOffsetTimer() % 20 == 1) {
            this.setActive(false);
            if (this.heat > 0)
                this.heat -= 1;
        }
    }

    @Override
    protected void updateRecipeProgress() {
        if (this.canRecipeProgress) {
            if (this.heat >= 100) {
                int heatMultiplier= 1+ (int)(4*getHeatPercentage());
                if (this.getMetaTileEntity().getOffsetTimer() % 20 == 1){//错开1tick
                    this.heat -= 5*heatMultiplier;
                }
                this.progressTime+=heatMultiplier;
                if (this.hasNotEnoughEnergy) {
                    this.hasNotEnoughEnergy = false;
                }
            } else {
                this.hasNotEnoughEnergy = true;
                this.decreaseProgress();
            }
            if (!this.hasNotEnoughEnergy && this.progressTime > this.maxProgressTime) {
                this.completeRecipe();
            }
        }
    }

    @Override
    protected boolean hasEnoughPower(int[] resultOverclock) {
        return this.heat >= 100;
    }

    @Override
    public void deserializeNBT(@NotNull NBTTagCompound compound) {
        super.deserializeNBT(compound);
        this.heat = compound.getInteger("Heat");
        this.burnTime = compound.getInteger("BurnTime");
        this.deltaHeat = compound.getInteger("DeltaHeat");
    }

    @Override
    public @NotNull NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();
        compound.setInteger("Heat", heat);
        compound.setInteger("BurnTime", burnTime);
        compound.setInteger("DeltaHeat", deltaHeat);
        return compound;
    }

    @Override
    public boolean isWorking() {
        return super.isWorking();
    }
    public float getHeatPercentage(){
        return (float) heat / MAX_HEAT;
    }
}
