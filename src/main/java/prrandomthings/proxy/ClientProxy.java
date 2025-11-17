package prrandomthings.proxy;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;
@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

//    private static final ModelExampleArmor exampleArmor = new ModelExampleArmor(1.0F);
//    private static final ModelExampleArmor exampleArmorLegs = new ModelExampleArmor(0.5F);

    private static final Map<Item, ModelBiped> exampleArmorModels = new HashMap<>();

    @Override
    public void preInit() {
//        exampleArmorModels.put(RegistryEvents.exampleHelmet, exampleArmor);
//        exampleArmorModels.put(RegistryEvents.exampleChestplate, exampleArmor);
//        exampleArmorModels.put(RegistryEvents.exampleLeggings, exampleArmorLegs);
//        exampleArmorModels.put(RegistryEvents.exampleBoots, exampleArmor);
    }

    @Override
    public Map<Item, ModelBiped> getExampleArmor() {
        return exampleArmorModels;
    }
}