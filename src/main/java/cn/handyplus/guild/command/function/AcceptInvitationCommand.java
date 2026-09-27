package cn.handyplus.guild.command.function;

import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCoolingUtil;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptInvitationCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.acceptInvitation";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 3, BaseUtil.getLangMsg("paramFailureMsg"));
        Integer guildId = AssertUtil.isPositiveToInt(args[1], BaseUtil.getLangMsg("amountFailureMsg"));
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        if (GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId()).isPresent()) {
            MessageUtil.sendMessage(player, ConfigUtil.CREATE_CONFIG.getString("haveGuild"));
            return;
        }
        if (GuildCoolingUtil.checkJoinCooling(player)) {
            return;
        }
        List<Integer> invitationIds = GuildConstants.INVITATION_PLAYER_MAP.get(player.getName());
        if (CollUtil.isEmpty(invitationIds) || !invitationIds.contains(guildId)) {
            MessageUtil.sendMessage(player, ConfigUtil.INVITATION_CONFIG.getString("member.noInvitation"));
            return;
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (guildInfo.getMemberCount() + 1 > guildInfo.getMemberMaxCount()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("numberFailureMsg"));
            return;
        }
        GuildApplyService.getInstance().updateApplyResultByPlayerUuid(player.getUniqueId(), ApplyTypeEnum.CANCEL.getId());
        GuildPlayer guildPlayer = new GuildPlayer();
        guildPlayer.setGuildInfoId(guildInfo.getId());
        guildPlayer.setGuildInfoName(guildInfo.getGuildName());
        guildPlayer.setPlayerName(player.getName());
        guildPlayer.setPlayerUuid(player.getUniqueId());
        guildPlayer.setPvpStatus(guildInfo.getPvpStatus());
        guildPlayer.setRoleId(GuildRoleEnum.FOUR.getRoleId());
        GuildPlayerService.getInstance().add(guildPlayer);
        GuildInfoService.getInstance().addMemberCount(guildInfo.getId(), 1);
        PlayerTitleUtil.addGuildPlayerTitle(guildPlayer.getPlayerUuid(), guildInfo.getTitleId());
        GuildConstants.INVITATION_PLAYER_MAP.remove(player.getName());
        MessageUtil.sendMessage(player, ConfigUtil.INVITATION_CONFIG.getString("member.acceptMsg"));
        MessageUtil.sendMessage(args[2], ConfigUtil.INVITATION_CONFIG.getString("member.acceptJoinMsg", "").replace("${player}", player.getName()));
    }

    @Override
    public String command() {
        return "acceptInvitation";
    }
}
