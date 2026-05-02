package prrandomthings.mixin;

import gregtech.api.unification.material.Materials;
import gregtech.common.metatileentities.steam.boiler.SteamBoiler;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import prrandomthings.constants.RTConstants;
import prrandomthings.materials.RTMaterials;

@Mixin(value = SteamBoiler.class,remap = false)
public abstract class MixinSteamBoiler {
    @Final
    @Shadow
    protected boolean isHighPressure;
    @Redirect(method = "generateSteam",at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/fluids/FluidTank;fill(Lnet/minecraftforge/fluids/FluidStack;Z)I"))
    private int fillSteam(FluidTank steamFluidTank, FluidStack steam, boolean doFill){
        RTConstants.LOGGER.info("Generating steam");
        if(!isHighPressure && steam.getFluid() == Materials.Steam.getFluid()){
            return steamFluidTank.fill(RTMaterials.LOW_QUALITY_STEAM.getFluid(steam.amount),doFill);
        }
        return steamFluidTank.fill(steam,doFill);
    }

}
