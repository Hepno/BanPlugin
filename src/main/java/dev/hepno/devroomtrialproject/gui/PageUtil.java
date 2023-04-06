package dev.hepno.devroomtrialproject.gui;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {

    public static List<ItemStack> getPageItems(List<ItemStack> items, int page, int itemsPerPage) {
        int upperBound = page * itemsPerPage;
        int lowerBound = upperBound - itemsPerPage;

        List<ItemStack> newItems = new ArrayList<>();
        for (int i = lowerBound; i < upperBound; i++) {
            try {
                newItems.add(items.get(i));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        return newItems;
    }

    public static boolean isPageValid(List<ItemStack> items, int page, int itemsPerPage) {
        if (page <= 0) { return false; }
        int upperBound = page * itemsPerPage;
        int lowerBound = upperBound - itemsPerPage;
        return items.size() > lowerBound;

    }

}
