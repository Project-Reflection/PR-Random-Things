package prrandomthings.mte;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.AdvancedTextWidget;
import gregtech.api.gui.widgets.DynamicLabelWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import prrandomthings.constants.RTConstants;
import prrandomthings.recipes.RTRecipeMaps;
import prrandomthings.recipes.logic.NoEnergyRecipeLogic;
import prrandomthings.recipes.logic.PitifulBoilerRecipeLogic;

import java.util.Collections;

public class MtePitifulBoiler extends MteCustomPrimitiveSingleblock {
    public static final MtePitifulBoiler SAMPLE = new MtePitifulBoiler(RTConstants.RTID("pitiful_boiler"),
            Textures.VOLTAGE_CASINGS[0], Textures.COAL_BOILER_OVERLAY);

    private MtePitifulBoiler(ResourceLocation metaTileEntityId, ICubeRenderer baseTexture, ICubeRenderer overlay) {
        super(metaTileEntityId, RTRecipeMaps.PITIFUL_BOILER, baseTexture, overlay);
    }

    @Override
    protected NoEnergyRecipeLogic initializeRecipeLogic() {
        return new PitifulBoilerRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MtePitifulBoiler(this.metaTileEntityId,this.baseTexture,this.overlay);
    }

    @Override
    protected ModularUI.Builder createUIBuilder(EntityPlayer entityPlayer) {
        ModularUI.Builder builder = super.createUIBuilder(entityPlayer);
        builder.widget(new AdvancedTextWidget(5, 40, list -> list.add(new TextComponentTranslation("Temperature: %s",
                ((PitifulBoilerRecipeLogic) this.recipeMapWorkable).heat)), 0xffffff));
        return builder;
    }
}
