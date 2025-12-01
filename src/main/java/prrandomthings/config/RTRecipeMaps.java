package prrandomthings.config;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.init.SoundEvents;

public class RTRecipeMaps {
    public static final RecipeMap<FuelRecipeBuilder> REACTANT_FUELS=new RecipeMap<>("reactant_fuels",
            1,0,1,0,new FuelRecipeBuilder(),false)
            .allowEmptyOutput();
    public static final RecipeMap<FuelRecipeBuilder> METALLURGIC_FUELS=new RecipeMap<>("metallurgic_fuels",
            0,0,1,0,new FuelRecipeBuilder(),false)
            .setSound(GTSoundEvents.FIRE)
            .allowEmptyOutput();
    public static final RecipeMap<SimpleRecipeBuilder> PRIMITIVE_CRACKER=new RecipeMap<>("primitive_cracker",
            4,1,2,1,new SimpleRecipeBuilder(),false)
            .setSound(SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE);
    public static final RecipeMap<PrimitiveRecipeBuilder> COMPOSTING_BARREL=new RecipeMap<>("composting_barrel",
            1,1,1,1,new PrimitiveRecipeBuilder(),false)
            .setSound(GTSoundEvents.BATH);
    public static final RecipeMap<PrimitiveRecipeBuilder> SIEVE =new RecipeMap<>("sieve",
            2,15,1,0,new PrimitiveRecipeBuilder(),false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_SIFT, ProgressWidget.MoveType.VERTICAL_DOWNWARDS)
            .setSound(SoundEvents.BLOCK_SAND_PLACE);
}
