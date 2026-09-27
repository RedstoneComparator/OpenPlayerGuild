package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.core.season.SeasonPvpManager;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StartCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.start";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        SeasonPvpManager.startSeason();
    }

    @Override
    public String command() {
        return "start";
    }
}
