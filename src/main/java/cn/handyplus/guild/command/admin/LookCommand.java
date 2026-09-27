package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.command.admin.GiveCommand;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.GuildUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LookCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "look";
    }

    @Override
    public String permission() {
        return "playerGuild.look";
    }

    private void sendPvpStatusMessage(@NotNull CommandSender sender, @NotNull String type, boolean running) {
        String langKey = running ? "lookPvpRunningMsg" : "lookPvpNotRunningMsg";
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg(langKey, MapUtil.of("${type}", type)));
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(partialName -> GuildUtil.guildNamesWithVar());
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            String useName = GiveCommand.getUseName(sender, args[1]);
            Optional<GuildInfo> guildOptional = GuildInfoService.getInstance().findByGuildName(useName);
            AssertUtil.isTrue(guildOptional.isPresent(), BaseUtil.getLangMsg("noGuildMsg"));
            GuildInfo guildInfo = guildOptional.get();
            List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByGuildId(guildInfo.getId());
            List<String> oneNames = guildPlayers.stream().filter(guildPlayer -> {
                if (guildPlayer.getRoleId() == 1) {
                    return true;
                }
                return false;
            }).map(GuildPlayer::getPlayerName).collect(Collectors.toList());
            List<String> twoNames = guildPlayers.stream().filter(guildPlayer -> {
                if (guildPlayer.getRoleId() == 2) {
                    return true;
                }
                return false;
            }).map(GuildPlayer::getPlayerName).collect(Collectors.toList());
            List<String> threeNames = guildPlayers.stream().filter(guildPlayer -> {
                if (guildPlayer.getRoleId() == 3) {
                    return true;
                }
                return false;
            }).map(GuildPlayer::getPlayerName).collect(Collectors.toList());
            List<String> fourNames = guildPlayers.stream().filter(guildPlayer -> {
                if (guildPlayer.getRoleId() == 4) {
                    return true;
                }
                return false;
            }).map(GuildPlayer::getPlayerName).collect(Collectors.toList());
            String title = new StringBuilder().insert(0, "&a_________________/ &e").append(guildInfo.getGuildName()).append("(").append(guildInfo.getId()).append(")&a \\_________________\n").toString();
            String oneMsg = new StringBuilder().insert(0, "&7| ").append(GuildRoleEnum.getRoleName(GuildRoleEnum.ONE)).append(": ").append(CollUtil.listToStr(oneNames)).append("\n").toString();
            String twoMsg = new StringBuilder().insert(0, "&7| ").append(GuildRoleEnum.getRoleName(GuildRoleEnum.TWO)).append(": ").append(CollUtil.listToStr(twoNames)).append("\n").toString();
            String threeMsg = new StringBuilder().insert(0, "&7| ").append(GuildRoleEnum.getRoleName(GuildRoleEnum.THREE)).append(": ").append(CollUtil.listToStr(threeNames)).append("\n").toString();
            String fourMsg = new StringBuilder().insert(0, "&7| ").append(GuildRoleEnum.getRoleName(GuildRoleEnum.FOUR)).append(": ").append(CollUtil.listToStr(fourNames)).append("\n").toString();
            MessageUtil.sendMessage(sender, title);
            MessageUtil.sendMessage(sender, oneMsg);
            MessageUtil.sendMessage(sender, twoMsg);
            MessageUtil.sendMessage(sender, threeMsg);
            MessageUtil.sendMessage(sender, fourMsg);
            MessageUtil.sendMessage(sender, "&a----------------------------------------");
            return;
        }
        this.sendPvpStatusMessage(sender, BaseUtil.getLangMsg("activityType.match"), MatchPvpUtil.isRunning());
        this.sendPvpStatusMessage(sender, BaseUtil.getLangMsg("activityType.score_match"), ScorePvpUtil.isRunning());
        this.sendPvpStatusMessage(sender, BaseUtil.getLangMsg("activityType.season"), SeasonPvpUtil.isRunning());
        this.sendPvpStatusMessage(sender, BaseUtil.getLangMsg("activityType.stronghold"), StrongholdPvpUtil.isRunning());
    }
}
