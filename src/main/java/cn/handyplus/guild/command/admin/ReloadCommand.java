package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.SignService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildRunTaskTimer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "reload";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigUtil.init();
        HandySchedulerUtil.runTaskLaterAsynchronously(() -> {

                GuildRunTaskTimer.cachePlayerGuild();
                GuildRunTaskTimer.addPluginChunkTicket();

        }, 20L);
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("reloadMsg"));
    }

    @Override
    public String permission() {
        return "playerGuild.reload";
    }
}
