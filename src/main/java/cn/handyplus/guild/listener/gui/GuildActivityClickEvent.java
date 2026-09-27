package cn.handyplus.guild.listener.gui;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.core.common.GuildPvpUtil;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.score.ScorePvpManager;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPvp;
import cn.handyplus.guild.inventory.GuildActivityGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.IHandyClickEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildPvpService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuildActivityClickEvent
implements IHandyClickEvent {
    @Override
    public void rawSlotClick(HandyInventory gui, InventoryClickEvent clickEvent) {
        int rawSlot = clickEvent.getRawSlot();
        Player player = gui.getPlayer();
        Integer guildInfoId = gui.getId();
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.ACTIVITY_CONFIG, "back")) {
            Inventory inventory = GuildViewGui.getInstance().createGui(player, guildInfoId);
            gui.syncOpen(inventory);
            return;
        }
        Map<Integer, String> map = HandyInventoryUtil.getCustomButton(ConfigUtil.ACTIVITY_CONFIG, "custom");
        String command = map.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.performReplaceCommand(player, command);
            return;
        }
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        if (optional.get().getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.ACTIVITY_CONFIG, "scoreMatePvp")) {
            this.toggleScorePvp(gui, player, guildInfoId);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.ACTIVITY_CONFIG, "strongholdPvp")) {
            this.toggleStrongholdPvp(gui, player, guildInfoId);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.ACTIVITY_CONFIG, "seasonPvp")) {
            this.toggleSeasonPvp(gui, player, guildInfoId);
            return;
        }
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.ACTIVITY_CONFIG, "matePvp")) {
            this.toggleMatchPvp(gui, player, guildInfoId);
        }
    }

    private void toggleMatchPvp(@NotNull HandyInventory gui, @NotNull Player player, @NotNull Integer guildInfoId) {
        if (MatchPvpUtil.isPreparing(guildInfoId) || MatchPvpUtil.isStarting(guildInfoId) || MatchPvpUtil.isRunning(guildInfoId)) {
            this.sendPvpActiveMessage(player, guildInfoId);
            return;
        }
        if (ScorePvpUtil.isActive(guildInfoId) || StrongholdPvpUtil.isActive(guildInfoId) || SeasonPvpUtil.isActive(guildInfoId)) {
            this.sendPvpActiveMessage(player, guildInfoId);
            return;
        }
        MatchPvpManager.toggleQueue(player, guildInfoId);
        GuildActivityGui.getInstance().setInventoryDate(gui);
        GuildActivityGui.getInstance().matePvpButtonTask(gui);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    private void toggleScorePvp(@NotNull HandyInventory gui, @NotNull Player player, @NotNull Integer guildInfoId) {
        if (ScorePvpUtil.isPreparing(guildInfoId) || ScorePvpUtil.isStarting(guildInfoId) || ScorePvpUtil.isRunning(guildInfoId)) {
            this.sendPvpActiveMessage(player, guildInfoId);
            return;
        }
        if (MatchPvpUtil.isActive(guildInfoId) || StrongholdPvpUtil.isActive(guildInfoId) || SeasonPvpUtil.isActive(guildInfoId)) {
            this.sendPvpActiveMessage(player, guildInfoId);
            return;
        }
        ScorePvpManager.toggleQueue(player, guildInfoId);
        GuildActivityGui.getInstance().setInventoryDate(gui);
    }

    private void sendPvpActiveMessage(Player player, Integer guildInfoId) {
        Map<String, String> map = MapUtil.of("${type}", GuildPvpUtil.getGuildPvpActiveName(guildInfoId));
        String message = BaseUtil.getLangMsg("guildPvpActiveMsg", map);
        MessageUtil.sendMessage(player, message);
    }

    private void toggleStrongholdPvp(@NotNull HandyInventory gui, @NotNull Player player, @NotNull Integer guildInfoId) {
        if (StrongholdPvpUtil.isPreparing(guildInfoId) || StrongholdPvpUtil.isStarting(guildInfoId) || StrongholdPvpUtil.isRunning(guildInfoId)) {
            this.sendPvpActiveMessage(player, guildInfoId);
            return;
        }
        if (MatchPvpUtil.isActive(guildInfoId) || ScorePvpUtil.isActive(guildInfoId) || SeasonPvpUtil.isActive(guildInfoId)) {
            this.sendPvpActiveMessage(player, guildInfoId);
            return;
        }
        StrongholdPvpManager.toggleQueue(player, guildInfoId);
        GuildActivityGui.getInstance().setInventoryDate(gui);
        GuildActivityGui.getInstance().strongholdPvpButtonTask(gui);
    }

    @Override
    public String guiType() {
        return GuiTypeEnum.GUILD_ACTIVITY.getType();
    }

    private void toggleSeasonPvp(@NotNull HandyInventory gui, @NotNull Player player, @NotNull Integer guildInfoId) {
        if (SeasonPvpUtil.isPreparing(guildInfoId) || SeasonPvpUtil.isRunning(guildInfoId)) {
            this.sendPvpActiveMessage((Player)player, guildInfoId);
            return;
        }
        Optional<GuildPvp> optional = GuildPvpService.getInstance().findByGuildInfoId(guildInfoId);
        if (optional.isPresent() && Boolean.TRUE.equals(optional.get().getIsJoin())) {
            GuildPvpService.getInstance().updateJoinById(optional.get().getId(), false);
            GuildActivityGui.getInstance().setInventoryDate(gui);
            return;
        }
        if (!optional.isPresent()) {
            GuildPvp guildPvp = new GuildPvp();
            guildPvp.setSeason(GuildPvpService.getInstance().findSeason());
            guildPvp.setGuildInfoId(guildInfoId);
            guildPvp.setIsJoin(true);
            guildPvp.setIsStart(false);
            GuildPvpService.getInstance().add(guildPvp);
        } else {
            GuildPvp guildPvp2 = optional.get();
            GuildPvpService.getInstance().updateJoinById(guildPvp2.getId(), true);
        }
        GuildActivityGui.getInstance().setInventoryDate(gui);
    }
}
