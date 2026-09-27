package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.GuildRunTaskTimer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClearCommand
implements IHandyCommandEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String command() {
        return "clear";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        switch (args[1]) {
            case "task": {
                GuildTaskDemandService.getInstance().delete();
                int deletedCount = GuildTaskService.getInstance().delete();
                GuildConstants.GUILD_TASK_DEMAND_MAP.clear();
                GuildRunTaskTimer.cachePlayerGuild();
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("clearTaskSucceedMsg", MapUtil.of("${delNum}", String.valueOf(deletedCount))));
                return;
            }
        }
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("paramFailureMsg"));
    }

    @Override
    public String permission() {
        return "playerGuild.clear";
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next("task");
    }
}
