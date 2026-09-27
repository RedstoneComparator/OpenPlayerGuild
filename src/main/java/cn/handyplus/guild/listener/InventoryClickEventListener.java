package cn.handyplus.guild.listener;

import cn.handyplus.guild.api.PlayerGuildApi;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

@HandyListener
public class InventoryClickEventListener
implements Listener {
    @EventHandler
    public void onInventoryClickEvent(@NotNull InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        InventoryType inventoryType = event.getInventory().getType();
        if (!InventoryType.CRAFTING.equals((Object)inventoryType) && !InventoryType.WORKBENCH.equals((Object)inventoryType)) {
            return;
        }
        if (!InventoryType.SlotType.CRAFTING.equals((Object)event.getSlotType())) {
            return;
        }
        this.closeCraftingInventory(event);
    }

    private void closeCraftingInventory(@NotNull InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Optional<Player> optional = HandyInventoryUtil.getPlayer(event);
        if (!optional.isPresent()) {
            return;
        }
        Player player = optional.get();
        if (!PlayerGuildApi.isPvp(player)) {
            return;
        }
        event.setCancelled(true);
        Player targetPlayer = player;
        PlayerSchedulerUtil.closeInventory(targetPlayer);
        MessageUtil.sendMessage(targetPlayer, ConfigUtil.ACTIVITY_CONFIG.getString("playerCraftingMsg"));
    }

    @EventHandler
    public void onCraftItem(@NotNull CraftItemEvent event) {
        this.closeCraftingInventory((InventoryClickEvent)event);
    }
}
