package prrandomthings.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import prrandomthings.constants.RTConstants;

public class EnchantmentManaRepair extends Enchantment {
    public static final Enchantment MANA_REPAIR=new EnchantmentManaRepair();
    protected EnchantmentManaRepair() {
        super(Rarity.COMMON, EnumEnchantmentType.BREAKABLE,
                EntityEquipmentSlot.values());
        this.name="mana_repair";
        setRegistryName(RTConstants.RTID("mana_repair"));
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
}
