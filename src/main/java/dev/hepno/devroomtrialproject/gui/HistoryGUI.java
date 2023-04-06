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

        for (ItemStack is : PageUtil.getPageItems(items, page, 28)) {
            gui.setItem(gui.firstEmpty(), is);
        }
        player.openInventory(gui);









        /*
        // Make edges of GUI be black stained-glass panes (aside from 48 and 50 as they are used for previous and next page)
        int[] edges = new int[] {
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 49, 51, 52, 53
        };
        for (int edge : edges) {
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            items.add(edge, item);
        }

        // Loop through all bans and add them to the items list
        for (int i = 0; i < databaseManager.getBanHistory(player.getUniqueId()).length; i++) {
            // Create the item to use
            ItemStack item = new ItemStack(Material.RED_WOOL);
            items.add(item);
        }
         */

    }

}
