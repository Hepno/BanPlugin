package dev.hepno.devroomtrialproject.event;

import dev.hepno.devroomtrialproject.DevroomTrialProject;
import dev.hepno.devroomtrialproject.gui.HistoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    private final DevroomTrialProject plugin;

    public GUIListener(DevroomTrialProject plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.getInventory();
        if (event.getCurrentItem() != null && event.getView().getTitle().contains("Ban History of ")) {

            // Get Page
            String str = event.getView().getTitle();
            str = str.replaceAll("[^0-9]+", "");

            int page = Integer.parseInt(str);
            if (event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Previous Page")) {
                    new HistoryGUI((Player) event.getWhoClicked(), page - 1, plugin);
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Next Page")) {
                    new HistoryGUI((Player) event.getWhoClicked(), page + 1, plugin);
                }
            }
            event.setCancelled(true);
        }
    }
}
