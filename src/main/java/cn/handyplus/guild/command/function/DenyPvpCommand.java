package cn.handyplus.guild.command.function;

import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpManager;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DenyPvpCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "denyPvp";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    private boolean denyStrongholdPreparation(@NotNull Player player, @NotNull GuildPlayer guildPlayer) {
        if (!StrongholdPvpUtil.isPreparing(guildPlayer.getGuildInfoId())) {
            return false;
        }
        if (StrongholdPvpManager.getPreparationPlayers(guildPlayer.getGuildInfoId()).isEmpty()) {
            MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("matePvpNotPrepareMsg"));
            return true;
        }
        StrongholdPvpManager.denyPreparation(guildPlayer.getGuildInfoId(), player.getUniqueId());
        MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("matePvpDenyPrepareMsg"));
        return true;
    }

    @Override
    public String permission() {
        return "playerGuild.denyPvp";
    }

    private boolean denySeasonPreparation(@NotNull Player player, @NotNull GuildPlayer guildPlayer) {
        if (!SeasonPvpUtil.isPreparing(guildPlayer.getGuildInfoId())) {
            return false;
        }
        if (SeasonPvpUtil.getPreparationPlayers(guildPlayer.getGuildInfoId()).isEmpty()) {
            MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("matePvpNotPrepareMsg"));
            return true;
        }
        SeasonPvpManager.denyPreparation(guildPlayer.getGuildInfoId(), player.getUniqueId());
        MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("matePvpDenyPrepareMsg"));
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (this.denyMatchPreparation(player, guildPlayer)) {
            return;
        }
        if (this.denyStrongholdPreparation(player, guildPlayer)) {
            return;
        }
        if (this.denySeasonPreparation(player, guildPlayer)) {
            return;
        }
        MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("matePvpNotPrepareMsg"));
    }

    private boolean denyMatchPreparation(@NotNull Player player, @NotNull GuildPlayer guildPlayer) {
        if (!MatchPvpUtil.isPreparing(guildPlayer.getGuildInfoId())) {
            return false;
        }
        if (MatchPvpUtil.getPreparationPlayers(guildPlayer.getGuildInfoId()).isEmpty()) {
            MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("matePvpNotPrepareMsg"));
            return true;
        }
        MatchPvpManager.denyPreparation(guildPlayer.getGuildInfoId(), player.getUniqueId());
        MessageUtil.sendMessage(player, ConfigUtil.ACTIVITY_CONFIG.getString("matePvpDenyPrepareMsg"));
        return true;
    }
}
