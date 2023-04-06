package dev.hepno.devroomtrialproject.event;

import dev.hepno.devroomtrialproject.DevroomTrialProject;
import dev.hepno.devroomtrialproject.gui.HistoryGUI;
import org.bukkit.Bukkit;
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

            // Get target
            String targetString = event.getView().getTitle();
            targetString.replaceAll("Ban History of ", "");
            targetString.split("-");
            Player target = Bukkit.getPlayer(targetString);

            int page = Integer.parseInt(str);
            if (event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Previous Page")) {
                    new HistoryGUI((Player) event.getWhoClicked(), target, page - 1, plugin);
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Next Page")) {
                    new HistoryGUI((Player) event.getWhoClicked(), target, page + 1, plugin);
                }
            }
            event.setCancelled(true);
        }
    }
}
