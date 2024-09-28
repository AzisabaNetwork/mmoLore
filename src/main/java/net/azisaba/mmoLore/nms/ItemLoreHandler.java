package net.azisaba.mmoLore.nms;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemLoreHandler {

    public static ItemStack setTagInLore(ItemStack item, List<String> set) {
        net.minecraft.world.item.ItemStack nms = net.minecraft.world.item.ItemStack.fromBukkitCopy(item);
        if (nms.getComponents().isEmpty()) return item;
        if (!nms.getComponents().has(DataComponents.LORE)) return getNewLineItemStack(nms, set);

        ItemLore lore = nms.getComponents().get(DataComponents.LORE);
        if (lore == null) return getNewLineItemStack(nms, set);

        ItemLore data = lore;
        for (String s : set) {
            Component comp = PaperAdventure.asVanilla(LegacyComponentSerializer.legacyAmpersand().deserialize(s));
            data = data.withLineAdded(comp);
        }
        nms.set(DataComponents.LORE, data);
        return nms.getBukkitStack();
    }

    @Contract("_, _ -> param1")
    public static @NotNull ItemStack removeTagInLore(@NotNull ItemStack item, ItemStack serverItem) {
        ItemMeta meta = item.getItemMeta();
        if (serverItem == null || !serverItem.hasItemMeta() || !serverItem.getItemMeta().hasLore()) {
            meta.lore(null);
        } else {
            meta.lore(serverItem.lore());
        }
        item.setItemMeta(meta);
        return item;
    }

    private static @NotNull ItemStack getNewLineItemStack(@NotNull net.minecraft.world.item.ItemStack nms, @NotNull List<String> set) {
        List<Component> list = new ArrayList<>();
        set.forEach(s -> list.add(PaperAdventure.asVanilla(LegacyComponentSerializer.legacyAmpersand().deserialize(s))));
        nms.set(DataComponents.LORE, new ItemLore(list));
        return nms.getBukkitStack();
    }
}
