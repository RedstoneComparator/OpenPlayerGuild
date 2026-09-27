package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildNoticeRead;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.inventory.GuildNoticeGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildNoticeReadService;
import cn.handyplus.guild.service.GuildNoticeService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildNoticeClickEvent
implements IHandyClickEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_NOTICE.getType();
    }

    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        HandyInventory inventoryGui = gui;
        Integer pageNum = inventoryGui.getPageNum();
        Integer pageCount = inventoryGui.getPageCount();
        Player player = inventoryGui.getPlayer();
        Map<Integer, Integer> map = inventoryGui.getIntMap();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.NOTICE_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, gui.getId());
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.NOTICE_CONFIG, "search")) {
            String searchType = null;
            if ("all".equals(gui.getSearchType())) {
                searchType = "noRead";
            }
            if ("noRead".equals(gui.getSearchType())) {
                searchType = "all";
            }
            Inventory inventory = GuildNoticeGui.getInstance().createGui(player, gui.getId(), searchType);
            gui.syncOpen(inventory);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.NOTICE_CONFIG, "previousPage")) {
            if (pageNum > 1) {
                HandyInventory pageGui = gui;
                pageGui.setPageNum(pageGui.getPageNum() - 1);
                GuildNoticeGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.NOTICE_CONFIG, "nextPage")) {
            if (pageNum + 1 <= pageCount) {
                HandyInventory nextPageGui = gui;
                nextPageGui.setPageNum(nextPageGui.getPageNum() + 1);
                GuildNoticeGui.getInstance().setInventoryDate(gui);
            }
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.NOTICE_CONFIG, "send")) {
            Optional<GuildInfo> optional = GuildInfoService.getInstance().findById(gui.getId());
            if (!optional.isPresent()) {
                return;
            }
            GuildInfo guildInfo = optional.get();
            int sendPrice = ConfigUtil.NOTICE_CONFIG.getInt("send.price");
            if (guildInfo.getMoney() < sendPrice) {
                MessageUtil.sendMessage(player, ConfigUtil.NOTICE_CONFIG.getString("send.noButton"));
                return;
            }
            GuildConstants.CREATE_GUILD_INFO_MAP.put(player.getUniqueId(), "send");
            MessageUtil.sendMessage(player, ConfigUtil.NOTICE_CONFIG.getString("send.content"));
            gui.syncClose();
            return;
        }
        Integer noticeId = map.get(rawSlot);
        if (noticeId == null) {
            return;
        }
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        if (guildPlayer.getRoleId() < GuildRoleEnum.TWO.getRoleId()) {
            GuildNoticeService.getInstance().delById(noticeId);
            MessageUtil.sendMessage(player, ConfigUtil.NOTICE_CONFIG.getString("delMsg"));
            GuildNoticeGui.getInstance().setInventoryDate(gui);
            return;
        }
        if (GuildNoticeReadService.getInstance().findGuildNoticeId(gui.getId(), player.getUniqueId()).contains(noticeId)) {
            return;
        }
        GuildNoticeRead guildNoticeRead = new GuildNoticeRead();
        Player readPlayer = player;
        guildNoticeRead.setGuildInfoId(gui.getId());
        guildNoticeRead.setGuildNoticeId(noticeId);
        guildNoticeRead.setReadPlayerName(player.getName());
        guildNoticeRead.setReadPlayerUuid(readPlayer.getUniqueId());
        guildNoticeRead.setReadTime(new Date());
        GuildNoticeReadService.getInstance().add(guildNoticeRead);
        MessageUtil.sendMessage(readPlayer, ConfigUtil.NOTICE_CONFIG.getString("readMsg"));
        GuildNoticeGui.getInstance().setInventoryDate(gui);
    }
}
