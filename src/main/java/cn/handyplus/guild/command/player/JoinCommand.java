package cn.handyplus.guild.command.player;

import cn.handyplus.guild.command.admin.GiveCommand;
import cn.handyplus.guild.command.function.AcceptCommand;
import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildApply;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.RgbTextUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.GuildCoolingUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinCommand
implements IHandyCommandEvent {
    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        CommandSender commandSender = sender;
        Player player = AssertUtil.notPlayer(commandSender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String useName = GiveCommand.getUseName(commandSender, args[1]);
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findByGuildName(useName);
        if (!guildInfoOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId()).isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("existGuild"));
            return;
        }
        JoinCommand.join(player, guildInfo.getId());
    }

    @Override
    public String command() {
        return "join";
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(partialName -> GuildUtil.guildNamesWithVar());
    }

    private static void processJoinRequest(@NotNull Integer guildId, @NotNull Player player) {
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (guildInfo.getMemberCount() + 1 > guildInfo.getMemberMaxCount()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("numberFailureMsg"));
            return;
        }
        String joinPapi = BaseConstants.CONFIG.getString("joinPapi");
        if (StrUtil.isNotEmpty(joinPapi)) {
            Optional<GuildPlayer> ownerOptional = GuildPlayerService.getInstance().findOneRoleByGuildId(guildId);
            if (ownerOptional.isPresent()) {
                GuildPlayer guildPlayer = ownerOptional.get();
                String ownerName = PlaceholderApiUtil.set(guildPlayer.getPlayerUuid(), joinPapi);
                String playerName = PlaceholderApiUtil.set(player, joinPapi);
                if (!ownerName.equals(playerName)) {
                    MessageUtil.sendMessage(player, BaseUtil.getLangMsg("joinPapiMsg"));
                    return;
                }
            }
        }
        if (guildInfo.getJoinMode() != null && !guildInfo.getJoinMode().booleanValue()) {
            AcceptCommand.getGuildPlayer(null, player.getName(), player.getUniqueId(), guildInfo);
            return;
        }
        GuildApply guildApply = new GuildApply();
        guildApply.setGuildInfoId(guildInfo.getId());
        guildApply.setPlayerName(player.getName());
        guildApply.setPlayerUuid(player.getUniqueId());
        guildApply.setApplyTime(new Date());
        guildApply.setApplyResult(ApplyTypeEnum.PENDING.getId());
        int addedRows = GuildApplyService.getInstance().add(guildApply);
        AssertUtil.notTrue(addedRows < 1, BaseUtil.getLangMsg("failureMsg"));
        MessageUtil.sendMessage(player, BaseUtil.getLangMsg("joinApplyMsg").replace("${guild_name}", guildInfo.getGuildName()));
        Integer[] roleIds = new Integer[2];
        roleIds[0] = GuildRoleEnum.ONE.getRoleId();
        roleIds[1] = GuildRoleEnum.TWO.getRoleId();
        List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByIdAndRoleIds(guildInfo.getId(), Arrays.asList(roleIds));
        for (GuildPlayer member : guildPlayers) {
            Optional<Player> onlinePlayerOptional = BaseUtil.getOnlinePlayer(member.getPlayerName());
            if (!onlinePlayerOptional.isPresent()) {
                continue;
            }
            JoinCommand.sendApplyMessage(player.getName(), addedRows, onlinePlayerOptional.get());
        }
    }

    public static void join(Player player, Integer guildId) {
        Optional<GuildApply> pendingApplyOptional = GuildApplyService.getInstance().findByPlayerUuid(player.getUniqueId(), ApplyTypeEnum.PENDING.getId());
        if (pendingApplyOptional.isPresent() && pendingApplyOptional.get().getGuildInfoId().equals(guildId)) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("haveApplied"));
            return;
        }
        if (pendingApplyOptional.isPresent()) {
            GuildApplyService.getInstance().updateApplyResultByPlayerUuid(player.getUniqueId(), ApplyTypeEnum.CANCEL.getId());
            JoinCommand.processJoinRequest(guildId, player);
            return;
        }
        if (GuildCoolingUtil.checkJoinCooling(player)) {
            return;
        }
        JoinCommand.processJoinRequest(guildId, player);
    }

    @Override
    public String permission() {
        return "playerGuild.join";
    }

    public static void sendApplyMessage(String playerName, int applyId, Player player) {
        String message = BaseUtil.getLangMsg("joinApplyNoticeMsg");
        message = StrUtil.replace(message, "player", playerName);
        message = StrUtil.replace(message, "id", String.valueOf(applyId));
        MessageUtil.sendMessage(player, message);
        RgbTextUtil.init(BaseUtil.getLangMsg("accept")).addClickCommand(new StringBuilder().insert(0, "/plg accept ").append(applyId).toString()).addExtra(RgbTextUtil.init(BaseUtil.getLangMsg("refuse")).addClickCommand(new StringBuilder().insert(0, "/plg deny ").append(applyId).toString())).send((CommandSender)player);
    }
}
