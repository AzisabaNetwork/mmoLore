package net.azisaba.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemLoreHandler {

    @NotNull
    @Contract("_, _ -> param1")
    public static ItemStack setTagInLore(@NotNull ItemStack item, List<String> set) {
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            List<net.kyori.adventure.text.Component> list = meta.lore();
            List<net.kyori.adventure.text.Component> comp = list != null ? new ArrayList<>(list) : new ArrayList<>();
            meta.lore(put(comp, set, meta));
        } else {
            List<net.kyori.adventure.text.Component> comp = put(new ArrayList<>(), set, meta);
            meta.lore(comp);
        }
        item.setItemMeta(meta);
        return item;
    }

    private static List<net.kyori.adventure.text.Component> put(List<Component> comp, @NotNull List<String> set, ItemMeta meta) {
        int i = 0;
        for (String s : set) {
            comp.add(LegacyComponentSerializer.legacyAmpersand().deserialize(s));
            meta.getPersistentDataContainer().set(Keys.getLore(i), PersistentDataType.STRING, s);
            i++;
        }
        meta.getPersistentDataContainer().set(Keys.getSize(), PersistentDataType.STRING, String.valueOf(i));
        return comp;
    }

    @Contract("_ -> param1")
    public static @NotNull ItemStack removeTagInLore(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) return item;
        ItemMeta meta = item.getItemMeta();
        List<String> tags = new ArrayList<>();

        if (meta.getPersistentDataContainer().has(Keys.getSize())) {
            String s = meta.getPersistentDataContainer().get(Keys.getSize(), PersistentDataType.STRING);
            if (s == null) return item;
            int size = Integer.parseInt(s);
            for (int i = 0; i < size; i++) {
                tags.add(meta.getPersistentDataContainer().get(Keys.getLore(i), PersistentDataType.STRING));
            }
            meta.getPersistentDataContainer().remove(Keys.getSize());
            for (int i = 0; i < size; i++) {
                meta.getPersistentDataContainer().remove(Keys.getLore(i));
            }
        }
        List<Component> list;
        if (meta.hasLore()) {
            list = meta.lore();
            List<Component> newList = new ArrayList<>();
            for (Component c : Objects.requireNonNull(list)) {
                String s = LegacyComponentSerializer.legacyAmpersand().serialize(c);
                if (tags.contains(s)) {
                    newList.add(c);
                }
            }
            list.removeAll(newList);
        } else {
            list = new ArrayList<>();
        }
        meta.lore(list);
        item.setItemMeta(meta);
        return item;
    }
}
