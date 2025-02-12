/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 11.09.2024 um 19:40
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.utils
 */

package de.kallifabio.resetworld.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(final Material material, final short subID) {
        itemStack = new ItemStack(material, 1, subID);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(final Material material) {
        itemStack = new ItemStack(material, 1, (short) 0);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayName(final String displayName) {
        this.itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setSubID(final byte subID) {
        itemStack.getData().setData(subID);
        return this;
    }

    public ItemBuilder setLore(final String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder addLoreLine(final String line) {
        if (this.itemMeta.getLore() != null)
            this.itemMeta.getLore().add(line);
        else {
            final List<String> lore = new ArrayList<>();
            lore.add(line);
            this.itemMeta.setLore(lore);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemBuilder addLoreArray(final String[] array) {
        if (this.itemMeta.getLore() != null) {
            for (String current : array) {
                this.itemMeta.getLore().add(current);
            }
        } else {
            final List<String> lore = new ArrayList<>(Arrays.asList(array));
            this.itemMeta.setLore(lore);
        }
        return this;
    }

    public ItemBuilder setSkullOwner(final String owner) {
        ((SkullMeta) this.itemMeta).setOwner(owner);
        return this;
    }

    public ItemBuilder setType(final Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder setAmount(final Integer amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchantment(final Enchantment enchantment) {
        this.itemMeta.addEnchant(enchantment, 1, false);
        return this;
    }

    public ItemBuilder addEnchantment(final Enchantment enchantment, final Integer power) {
        this.itemMeta.addEnchant(enchantment, power, false);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }
}
