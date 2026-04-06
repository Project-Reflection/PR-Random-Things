package prrandomthings.items;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;
import gregtech.api.util.GTUtility;
import net.minecraft.util.ResourceLocation;
import prrandomthings.constants.RTConstants;

public class RTMetaItem extends StandardMetaItem {
    public RTMetaItem(String registryName) {
        super();
        this.setRegistryName(RTConstants.RTID(registryName));
    }

    @Override
    public ResourceLocation createItemModelPath(MetaItem<?>.MetaValueItem metaValueItem, String postfix) {
        return RTConstants.RTID(formatModelPath(metaValueItem) + postfix);
    }
    public static MetaItem<?> META_ITEM_1=new RTMetaItem("meta_item_1");
    public static MetaItem<?>.MetaValueItem ERROR;
    public static MetaItem<?>.MetaValueItem PLANT_FIBER;
    public static MetaItem<?>.MetaValueItem PLANT_STRING;
    public static MetaItem<?>.MetaValueItem PLANT_MESH;
    public static MetaItem<?>.MetaValueItem STRING_MESH;

    @Override
    public void registerSubItems() {
        ERROR=addItem(0,"error_item");
        //Materials(1~5)
        PLANT_FIBER=addItem(1,"plant_fiber")
                .setCreativeTabs();
        PLANT_STRING=addItem(2,"plant_string");
        //Meshes(6~10)
        PLANT_MESH=addItem(6,"plant_mesh");
        STRING_MESH=addItem(7,"string_mesh");

    }
}
