package prrandomthings.recipes;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;

public final class MetallurgicRecipes {
    private MetallurgicRecipes(){

    }
    public static void register()
    {
        registerMetallurgicRecipes(128,625,
                Materials.Iron,
                Materials.Copper,
                Materials.Tin,
                Materials.Bronze);
        registerMetallurgicRecipes(96,625,
                Materials.Gold,
                Materials.Silver,
                Materials.Lead,
                Materials.Steel,
                Materials.Electrum,
                Materials.Invar,
                Materials.Aluminium);
        registerMetallurgicRecipes(256,1875,Materials.Cupronickel);
        registerMetallurgicRecipes(64,625,Materials.Nickel);
        registerMetallurgicRecipes(48,625,Materials.Platinum,Materials.Palladium);
        registerMetallurgicRecipes(192,3125,
                Materials.Iridium,
                Materials.Ruthenium,
                Materials.Rhodium,
                Materials.Osmium);
    }
    private static void registerMetallurgicRecipes(int volume, int duration, Material... materials)
    {
        for(Material material : materials){
            RTRecipeMaps.METALLURGIC_FUELS.recipeBuilder()
                    .fluidInputs(material.getFluid(volume))
                    .EUt(32)
                    .duration(duration)
                    .buildAndRegister();
        }
    }
}
