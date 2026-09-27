package cn.handyplus.guild.command.function;

import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildApply;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.accept";
    }

    public static void getGuildPlayer(Player player, String playerName, UUID playerUuid, GuildInfo guildInfo) {
        String recipientName;
        GuildPlayer guildPlayer = new GuildPlayer();
        guildPlayer.setGuildInfoId(guildInfo.getId());
        guildPlayer.setGuildInfoName(guildInfo.getGuildName());
        guildPlayer.setPlayerName(playerName);
        guildPlayer.setPlayerUuid(playerUuid);
        guildPlayer.setPvpStatus(guildInfo.getPvpStatus());
        guildPlayer.setRoleId(GuildRoleEnum.FOUR.getRoleId());
        GuildPlayerService.getInstance().add(guildPlayer);
        GuildInfoService.getInstance().addMemberCount(guildInfo.getId(), 1);
        String message = BaseUtil.getLangMsg("joinApplyAcceptMsg", "");
        if (player != null) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("acceptSucceedMsg").replace("${player}", playerName));
            message = message.replace("${player}", player.getName()).replace("${guild_name}", guildInfo.getGuildName());
            recipientName = playerName;
        } else {
            message = message.replace("${player}", ConfigUtil.SETTING_CONFIG.getString("joinMode.auto", ""));
            recipientName = playerName;
        }
        MessageUtil.sendMessage(recipientName, message);
        PlayerTitleUtil.addGuildPlayerTitle(playerUuid, guildInfo.getTitleId());
    }

    public static synchronized void accept(Player player, Integer applyId) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (guildPlayer.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        Optional<GuildApply> applyOptional = GuildApplyService.getInstance().findById(applyId);
        if (!applyOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noApply"));
            return;
        }
        GuildApply guildApply = applyOptional.get();
        if (ApplyTypeEnum.CANCEL.getId().equals(guildApply.getApplyResult())) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noApply").replace("${player}", guildApply.getPlayerName()));
            return;
        }
        if (!ApplyTypeEnum.PENDING.getId().equals(guildApply.getApplyResult())) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("refuseFailureMsg").replace("${applyPlayerName}", guildApply.getApplyPlayerName()));
            return;
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (guildInfo.getMemberCount() + 1 > guildInfo.getMemberMaxCount()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("numberFailureMsg"));
            return;
        }
        GuildApplyService.getInstance().updateApplyResultByPlayerUuid(guildApply.getPlayerUuid(), ApplyTypeEnum.CANCEL.getId());
        guildApply.setApplyResult(ApplyTypeEnum.AGREE.getId());
        guildApply.setApplyPlayerName(player.getName());
        guildApply.setApplyPlayerUuid(player.getUniqueId());
        GuildApplyService.getInstance().updateById(guildApply);
        AcceptCommand.getGuildPlayer(player, guildApply.getPlayerName(), guildApply.getPlayerUuid(), guildInfo);
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        Integer applyId = AssertUtil.isPositiveToInt(args[1], BaseUtil.getLangMsg("amountFailureMsg"));
        AcceptCommand.accept(AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg")), applyId);
    }

    @Override
    public String command() {
        return "accept";
    }
}
