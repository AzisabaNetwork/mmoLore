package net.azisaba.plugin;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Keys {

    @NotNull
    @Contract("_ -> new")
    public static NamespacedKey getLore(int repeat) {
        return new NamespacedKey("item_lore_edit", "fake_lore_" + repeat);
    }

    @Contract(" -> new")
    public static @NotNull NamespacedKey getSize() {
        return new NamespacedKey("item_lore_edit", "repeat");
    }
}
