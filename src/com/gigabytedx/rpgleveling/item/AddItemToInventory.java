package com.gigabytedx.rpgleveling.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.modifiers.Modifier;
import com.questcraft.itemapi.ItemAPI;

public class AddItemToInventory {

	public static Inventory addItem(Inventory inv, Item item, Main plugin, boolean obtainable) {
		ItemStack itemStack = new ItemStack(item.getType());
		try {
			if (item.isEnchanted()) {
				ItemAPI.addGlow(itemStack);
			}
		} catch (IllegalArgumentException e) {

		}
		ItemMeta meta = itemStack.getItemMeta();

		meta.setDisplayName(ChatColor.BLUE + item.getName());
		String loreText = item.getLore();

		List<String> lore = new ArrayList<>();

		lore.add(ChatColor.GOLD + "Cost: " + ChatColor.DARK_PURPLE + item.getCost());
		try {
			String[] words = loreText.split("\\s+");

			int count = 0;
			String sentence = "";
			for (String word : words) {
				sentence = sentence.concat(word + " ");
				if (++count > plugin.loreLength) {
					lore.add(sentence);
					sentence = "";
					count = 0;
				}
			}
			lore.add(sentence);
		} catch (NullPointerException e) {
			System.out.println("No lore");
		}
		if (item.getBuffs().size() > 0) {
			lore.add(" ");
			lore.add(ChatColor.GOLD + "Buffs");

			for (Modifier buff : item.getBuffs()) {
				try {
					lore.add(ChatColor.DARK_PURPLE + " - " + buff.getName());
				} catch (NullPointerException e) {
					try {
						throw new Exception("Buff for item " + item.getName()
								+ " was not found. Please ensure buffs listed in item is spelt correctly (case-sensitive)");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		if (item.getDebuffs().size() > 0) {
			lore.add(" ");
			lore.add(ChatColor.RED + "Debuffs");
			for (Modifier buff : item.getDebuffs()) {
				lore.add(ChatColor.DARK_PURPLE + " - " + buff.getName());
			}
		}
		
		if(item.getDamage() > 0){
			lore.add("");
			lore.add(ChatColor.GREEN + "Attack Damage: " + ChatColor.RED + item.getDamage());
		}else if (item.getProtection() > 0){
			lore.add("");
			lore.add(ChatColor.GREEN + "Armor Protection: " + ChatColor.RED + item.getProtection());
		}
		if (!obtainable) {
			lore.add(" ");
			lore.add(ChatColor.DARK_RED + "- Locked -");
		}
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		inv.addItem(itemStack);
		return inv;
	}
}
