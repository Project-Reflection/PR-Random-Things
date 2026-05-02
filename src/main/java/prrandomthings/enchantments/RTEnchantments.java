package prrandomthings.enchantments;

import gregtech.api.unification.material.properties.PropertyKey;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.IForgeRegistry;
import prrandomthings.materials.RTMaterials;

import java.util.stream.Stream;

public class RTEnchantments {
    public static void register(IForgeRegistry<Enchantment> registry) {
        registry.registerAll(EnchantmentManaRepair.INSTANCE);
        Stream.of(
                        RTMaterials.MANASTEEEL,
                        RTMaterials.TERRASTEEL,
                        RTMaterials.ELVEN_ELEMENTIUM
                ).map(mat -> mat.getProperty(PropertyKey.TOOL))
                .forEach(prop -> prop.addEnchantmentForTools(EnchantmentManaRepair.INSTANCE, 1));
    }
}
