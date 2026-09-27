package cn.handyplus.guild.listener;

import cn.handyplus.guild.command.player.CreateCommand;
import cn.handyplus.guild.command.player.EditDescriptionCommand;
import cn.handyplus.guild.command.player.EditGuildNameCommand;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.constants.ShopBuyLimitTypeEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildShop;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.NumberUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildShopService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import cn.handyplus.guild.util.NoticeUtil;
import java.util.List;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@HandyListener
public class AsyncPlayerChatEventListener
implements Listener {
    private void depositMoney(Player player, String amountText) {
        Integer amount = NumberUtil.isNumericToInt(amountText);
        if (amount == null || amount <= 0) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("amountFailureMsg"));
            return;
        }
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        if (guildPlayer.getMoney() < amount) {
            MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("noButton"));
            return;
        }
        GuildPlayerService.getInstance().subtractMoneyById(guildPlayer.getId(), amount);
        GuildPlayerService.getInstance().addGuildMoneyById(guildPlayer.getId(), amount);
        GuildInfoService.getInstance().addMoneyById(guildPlayer.getGuildInfoId(), amount);
        MessageUtil.sendMessage(player, ConfigUtil.BANK_CONFIG.getString("succeedMsg"));
        GuildCacheUtil.refresh(guildPlayer.getGuildInfoId());
    }

    private void sellItem(Player player, String message) {
        String[] parts = message.split("#");
        if (parts.length != 2) {
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellErrorOneMsg"));
            return;
        }
        Integer number = NumberUtil.isNumericToInt(parts[0]);
        Integer price = NumberUtil.isNumericToInt(parts[1]);
        if (number == null) {
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellErrorOneMsg"));
            return;
        }
        if (number < 1) {
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellErrorNumberMsg"));
            return;
        }
        if (price == null) {
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellErrorOneMsg"));
            return;
        }
        if (price < 1) {
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellErrorPriceMsg"));
            return;
        }
        ItemStack itemStack = ItemStackUtil.getItemInMainHand(player.getInventory());
        if (itemStack == null || Material.AIR.equals((Object)itemStack.getType())) {
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellErrorItemMsg"));
            return;
        }
        List<String> loreBlackList = ConfigUtil.SHOP_CONFIG.getStringList("loreBlackList");
        ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemStack);
        if (CollUtil.isNotEmpty(itemMeta.getLore()) && CollUtil.isNotEmpty(loreBlackList)) {
            for (String blacklisted : loreBlackList) {
                for (String loreLine : itemMeta.getLore()) {
                    if (!loreLine.contains(blacklisted)) continue;
                    MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellLoreErrorNumberMsg"));
                    return;
                }
            }
        }
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        Boolean removed = ItemStackUtil.removeItem(player, itemStack, number);
        if (!removed.booleanValue()) {
            MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellErrorNumberMsg"));
            return;
        }
        GuildShop guildShop = new GuildShop();
        guildShop.setPrice(price);
        guildShop.setLevel(1);
        guildShop.setNumber(number);
        ItemStack soldItem = itemStack.clone();
        soldItem.setAmount(1);
        guildShop.setItemStack(ItemStackUtil.itemStackSerialize(soldItem));
        guildShop.setPlayerName(player.getName());
        guildShop.setPlayerUuid(player.getUniqueId());
        guildShop.setGuildInfoId(guildPlayer.getGuildInfoId());
        guildShop.setType(ShopBuyLimitTypeEnum.ONLY.getName());
        GuildShopService.getInstance().add(guildShop);
        MessageUtil.sendMessage(player, ConfigUtil.SHOP_CONFIG.getString("sellYesMsg"));
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String action = GuildConstants.CREATE_GUILD_INFO_MAP.get(player.getUniqueId());
        if (StrUtil.isEmpty(action)) {
            return;
        }
        AsyncPlayerChatEvent chatEvent = event;
        chatEvent.setCancelled(true);
        String message = chatEvent.getMessage();
        String strippedMessage = BaseUtil.stripColor(message);
        if ("T".equalsIgnoreCase(strippedMessage)) {
            MessageUtil.sendMessage(player, ConfigUtil.OPEN_CONFIG.getString("create.cancelMsh"));
            GuildConstants.CREATE_GUILD_INFO_MAP.remove(player.getUniqueId());
            return;
        }
        if ("sell".equals(action)) {
            this.sellItem(player, strippedMessage);
            return;
        }
        if ("add".equals(action)) {
            CreateCommand.create(player, message);
        }
        if ("edit".equals(action)) {
            EditGuildNameCommand.editGuildName(player, message);
        }
        if ("editDescription".equals(action)) {
            EditDescriptionCommand.editDescription(player, message);
        }
        if ("custom".equals(action)) {
            this.depositMoney(player, strippedMessage);
        }
        if ("send".equals(action)) {
            NoticeUtil.sendNotice(player, message);
        }
        GuildConstants.CREATE_GUILD_INFO_MAP.remove(player.getUniqueId());
    }
}
