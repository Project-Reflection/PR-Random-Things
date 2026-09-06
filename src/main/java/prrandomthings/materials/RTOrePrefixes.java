package prrandomthings.materials;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialFlags;
import gregtech.api.unification.material.info.MaterialIconType;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;

public class RTOrePrefixes {
    public static boolean generateZuku(Material material) {
        if(!material.hasProperty(PropertyKey.INGOT) || material.hasProperty(PropertyKey.POLYMER)){
            return false;
        }
        return !material.hasAnyOfFlags(MaterialFlags.DECOMPOSITION_BY_ELECTROLYZING,MaterialFlags.IS_MAGNETIC);
    }
    public static OrePrefix ZUKU = new OrePrefix("zuku", GTValues.M / 32,
            null, new MaterialIconType("zuku"), OrePrefix.Flags.ENABLE_UNIFICATION, RTOrePrefixes::generateZuku);
    public static OrePrefix INGOT_ZUKU = new OrePrefix("ingotZuku", GTValues.M / 8,
            null, new MaterialIconType("ingotZuku"), OrePrefix.Flags.ENABLE_UNIFICATION, RTOrePrefixes::generateZuku);
    public static OrePrefix INGOT_SAGEGANE = new OrePrefix("ingotSagegane", GTValues.M / 2,
            null, new MaterialIconType("ingotSagegane"), OrePrefix.Flags.ENABLE_UNIFICATION, RTOrePrefixes::generateZuku);

    public static void addToMetaItem() {
        MetaItems.addOrePrefix(ZUKU);
        MetaItems.addOrePrefix(INGOT_ZUKU);
        MetaItems.addOrePrefix(INGOT_SAGEGANE);
    }
}
