package com.runicrealms.runicdoors.utility;

import com.runicrealms.runicdoors.RunicDoors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtil {

    public static final NamespacedKey key = new NamespacedKey(RunicDoors.getRunicDoors(), "Doors");

    public static ItemStack getRegionItem() {
        ItemStack regionItem = new ItemStack(Material.BLAZE_ROD);

        ItemMeta itemMeta = regionItem.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GRAY + "" + StringUtils.colorCode("&3Door Region &6Selector"));
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Doors");
        regionItem.setItemMeta(itemMeta);
        return regionItem;
    }

    public static boolean isRegionSelector(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (!itemStack.hasItemMeta()) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}
