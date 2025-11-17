package prrandomthings.materials;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.util.ResourceLocation;
import prrandomthings.PRConstants;
import prrandomthings.config.RTConfig;

public class RTMaterials {
    public static Material SLAG;
    public static void register() {
        int id= RTConfig.startMaterialID;
        SLAG=new Material.Builder(id++,new ResourceLocation(PRConstants.MODID,"slag"))
                .color(0x666666)
                .components(new MaterialStack(Materials.Ash,1),new MaterialStack(Materials.Grossular,1))
                .gem()
                .iconSet(MaterialIconSet.FLINT)
                .flags(MaterialFlags.DECOMPOSITION_BY_CENTRIFUGING)
                .build();
    }
}
