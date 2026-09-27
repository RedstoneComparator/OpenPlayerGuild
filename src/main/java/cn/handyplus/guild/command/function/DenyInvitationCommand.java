package cn.handyplus.guild.command.function;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DenyInvitationCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.denyInvitation";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 3, BaseUtil.getLangMsg("paramFailureMsg"));
        Integer id = AssertUtil.isPositiveToInt(args[1], BaseUtil.getLangMsg("amountFailureMsg"));
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        List<Integer> invitationIds = GuildConstants.INVITATION_PLAYER_MAP.get(player.getName());
        if (CollUtil.isEmpty(invitationIds) || !invitationIds.contains(id)) {
            MessageUtil.sendMessage(player, ConfigUtil.INVITATION_CONFIG.getString("member.noInvitation"));
            return;
        }
        invitationIds.remove(id);
        GuildConstants.INVITATION_PLAYER_MAP.put(player.getName(), invitationIds);
        MessageUtil.sendMessage(player, ConfigUtil.INVITATION_CONFIG.getString("member.denyMsg"));
        MessageUtil.sendMessage(args[2], ConfigUtil.INVITATION_CONFIG.getString("member.denyJoinMsg", "").replace("${player}", player.getName()));
    }

    @Override
    public String command() {
        return "denyInvitation";
    }
}
