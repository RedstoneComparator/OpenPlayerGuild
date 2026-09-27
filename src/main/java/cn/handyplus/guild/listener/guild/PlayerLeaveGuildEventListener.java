package cn.handyplus.guild.listener.guild;

import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildPlayerCooling;
import cn.handyplus.guild.enter.GuildPlayerData;
import cn.handyplus.guild.event.PlayerLeaveGuildEvent;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.LuckPermsUtil;
import cn.handyplus.guild.hook.PlayerChatUtil;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerCoolingService;
import cn.handyplus.guild.service.GuildPlayerDataService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildShopService;
import cn.handyplus.guild.util.BuffManageUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Date;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@HandyListener
public class PlayerLeaveGuildEventListener
implements Listener {
    public static void savePlayerData(GuildPlayer sourcePlayer) {
        GuildPlayerData guildPlayerData;
        if (!BaseConstants.CONFIG.getBoolean("levelSavePlayerData")) {
            return;
        }
        GuildPlayerData guildPlayerData2 = guildPlayerData = new GuildPlayerData();
        GuildPlayer guildPlayer = sourcePlayer;
        GuildPlayerData guildPlayerData3 = guildPlayerData;
        GuildPlayer guildPlayer2 = sourcePlayer;
        GuildPlayerData guildPlayerData4 = guildPlayerData;
        GuildPlayer guildPlayer3 = sourcePlayer;
        guildPlayerData.setPlayerName(sourcePlayer.getPlayerName());
        guildPlayerData.setPlayerUuid(guildPlayer3.getPlayerUuid());
        guildPlayerData4.setMoney(guildPlayer3.getMoney());
        guildPlayerData4.setWeekMoney(sourcePlayer.getWeekMoney());
        guildPlayerData.setTotalMoney(guildPlayer2.getTotalMoney());
        guildPlayerData3.setGuildMoney(guildPlayer2.getGuildMoney());
        guildPlayerData3.setOre(sourcePlayer.getOre());
        guildPlayerData.setTotalOre(guildPlayer.getTotalOre());
        guildPlayerData2.setKill(guildPlayer.getKill());
        guildPlayerData2.setDie(sourcePlayer.getDie());
        GuildPlayerDataService.getInstance().add(guildPlayerData);
    }

    @EventHandler
    public void onPlayerLeaveGuild(PlayerLeaveGuildEvent event) {
        Player player;
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findById(event.getGuildPlayerId());
        if (!guildPlayerOptional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        GuildPlayerService.getInstance().deleteById(guildPlayer.getId());
        PlayerLeaveGuildEventListener.savePlayerData(guildPlayer);
        GuildInfoService.getInstance().subtractMemberCount(guildPlayer.getGuildInfoId(), 1);
        PlayerTitleUtil.removeGuildPlayerTitle(guildPlayer.getPlayerUuid(), guildInfo.getTitleId());
        GuildWorldUtil.removePlayerFromWorld(guildPlayer.getPlayerUuid(), guildInfo.getWorldName());
        GuildShopService.getInstance().delByPlayerUuid(guildPlayer.getPlayerUuid());
        if ("activeLeave".equals(event.getLeaveType())) {
            Player player2 = event.getPlayer();
            GuildPlayerCooling guildPlayerCooling = new GuildPlayerCooling();
            guildPlayerCooling.setPlayerName(player2.getName());
            guildPlayerCooling.setPlayerUuid(player2.getUniqueId());
            guildPlayerCooling.setLeaveTime(new Date());
            GuildPlayerCoolingService.getInstance().add(guildPlayerCooling);
        }
        BaseUtil.getOnlinePlayer(guildPlayer.getPlayerUuid()).ifPresent(BuffManageUtil::removeBuff);
        MessageUtil.sendMessage(guildPlayer.getPlayerName(), BaseUtil.getLangMsg("leaveMsg", MapUtil.of("${guild_name}", guildPlayer.getGuildInfoName())));
        PlayerLeaveGuildEvent playerLeaveGuildEvent = event;
        player = event.getPlayer() != null ? playerLeaveGuildEvent.getPlayer() : (Player)BaseUtil.getOnlinePlayer(playerLeaveGuildEvent.getOfflinePlayer().getUniqueId()).orElse(null);
        PlayerChatUtil.unRegPlayerChannel(player, guildInfo.getId());
        PlayerChatUtil.setPlayerChannelToDefault(player);
        LuckPermsUtil.removeUserFromGroup(guildPlayer.getPlayerUuid(), guildInfo.getLuckPermsGroup());
        GuildCacheUtil.remove(guildPlayer.getPlayerUuid());
        GuildCacheUtil.refresh(guildPlayer.getGuildInfoId());
    }
}
