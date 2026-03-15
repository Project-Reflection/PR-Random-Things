package prrandomthings.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.IForgeRegistry;

public class RTEnchantments {
    public static Enchantment MANA_REPAIR;
    public static void register(IForgeRegistry<Enchantment> registry){
        MANA_REPAIR=new EnchantmentManaRepair();
        registry.registerAll(MANA_REPAIR);
    }
}
