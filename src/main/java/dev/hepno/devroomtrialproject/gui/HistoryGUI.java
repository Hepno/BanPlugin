package dev.hepno.devroomtrialproject.gui;

import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HistoryGUI {

    public HistoryGUI(Player player, int page) {
        Inventory gui = Bukkit.createInventory(null, 54, "Ban History of " + player.getName() + " - Page " + page);
        DatabaseManager databaseManager = new DatabaseManager();
        List<ItemStack> items = new ArrayList<>();

        // Connect to database
        try {
            databaseManager.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 135; i++) {
            items.add(new ItemStack(Material.RED_WOOL));
        }
        ItemStack leftArrow;
        ItemMeta leftArrowMeta;
        if (PageUtil.isPageValid(items, page -1, 28)) {
            leftArrow = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            leftArrowMeta = leftArrow.getItemMeta();
            leftArrowMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
        } else {
            leftArrow = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            leftArrowMeta = leftArrow.getItemMeta();
            leftArrowMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
        }
        leftArrow.setItemMeta(leftArrowMeta);
        gui.setItem(48, leftArrow);

        ItemStack rightArrow;
        ItemMeta rightArrowMeta;
        if (PageUtil.isPageValid(items, page +1, 28)) {
            rightArrow = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            rightArrowMeta = rightArrow.getItemMeta();
            rightArrowMeta.setDisplayName(ChatColor.GREEN + "Next Page");
        } else {
            rightArrow = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            rightArrowMeta = rightArrow.getItemMeta();
            rightArrowMeta.setDisplayName(ChatColor.GREEN + "Next Page");
        }
        rightArrow.setItemMeta(rightArrowMeta);
        gui.setItem(50, rightArrow);

        // Create frame for decorations
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        for (int i = 0; i < 4; i++) {
            int slot = 9 + (i * 9);
            gui.setItem(slot, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        for (int i = 0; i < 4; i++) {
            int slot = 17 + (i * 9);
            gui.setItem(slot, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        for (int i = 45; i < 54; i++) {
            if (i != 48 && i != 50) {
                gui.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }

        // Add all the items
        for (ItemStack is : PageUtil.getPageItems(items, page, 28)) {
            gui.setItem(gui.firstEmpty(), is);
        }

        player.openInventory(gui);
    }

}
