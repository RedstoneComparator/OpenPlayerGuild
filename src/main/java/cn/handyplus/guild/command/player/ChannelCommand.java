package cn.handyplus.guild.command.player;

import cn.handyplus.guild.hook.PlayerChatUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ChannelCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.channel";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        PlayerChatUtil.setPlayerChannel(AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg")));
    }

    @Override
    public String command() {
        return "channel";
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
