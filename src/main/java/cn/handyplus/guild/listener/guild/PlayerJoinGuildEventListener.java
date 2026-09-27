package cn.handyplus.guild.listener.guild;

import cn.handyplus.guild.enter.GuildPlayerData;
import cn.handyplus.guild.event.PlayerJoinGuildEvent;
import cn.handyplus.guild.hook.LuckPermsUtil;
import cn.handyplus.guild.hook.PlayerChatUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerDataService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@HandyListener
public class PlayerJoinGuildEventListener
implements Listener {
    @EventHandler
    public void onEvent(PlayerJoinGuildEvent event) {
        UUID playerUuid = event.getPlayerUuid();
        BaseUtil.getOnlinePlayer(playerUuid).ifPresent(onlinePlayer -> PlayerChatUtil.regPlayerChannel(onlinePlayer, event.getGuildInfoId()));
        Object data = GuildPlayerDataService.getInstance().findByPlayerUuid(playerUuid);
        if (((Optional)data).isPresent()) {
            data = ((Optional)data).get();
            GuildPlayerService.getInstance().addOnlyMoneyByPlayerUuid(playerUuid, ((GuildPlayerData)data).getMoney());
            GuildPlayerDataService.getInstance().deleteByPlayerUuId(event.getPlayerUuid());
        }
        GuildInfoService.getInstance().findById(event.getGuildInfoId()).ifPresent(guildInfo -> LuckPermsUtil.addUserToGroup(playerUuid, guildInfo.getLuckPermsGroup()));
        GuildCacheUtil.refresh(playerUuid);
    }
}
