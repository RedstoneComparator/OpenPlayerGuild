package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class GetIpCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "getIp";
    }

    @Override
    public String permission() {
        return "playerGuild.getIp";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        BaseUtil.sendTip(sender);
    }
}
