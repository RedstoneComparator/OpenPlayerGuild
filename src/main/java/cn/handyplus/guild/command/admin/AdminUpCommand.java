package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.command.admin.GiveCommand;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.event.GuildUpEvent;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.GuildUtil;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

public class AdminUpCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.adminUp";
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(partialName -> GuildUtil.guildNamesWithVar()).nextLang("tabHelp.level");
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        String useName = GiveCommand.getUseName(sender, args[1]);
        if (StrUtil.isEmpty(useName)) {
            return;
        }
        Optional<GuildInfo> guildOptional = GuildInfoService.getInstance().findByGuildName(useName);
        if (!guildOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildOptional.get();
        int level;
        if (args.length > 2) {
            level = AssertUtil.isPositiveToInt(args[2], BaseUtil.getLangMsg("amountFailureMsg"));
        } else {
            level = guildInfo.getLevel() + 1;
        }
        HandySchedulerUtil.runTask(() -> AdminUpCommand.callGuildUpEvent(guildInfo, level, sender));
    }

    @Override
    public String command() {
        return "adminUp";
    }

    private static void callGuildUpEvent(GuildInfo guildInfo, int level, CommandSender sender) {
        Bukkit.getServer().getPluginManager().callEvent((Event)new GuildUpEvent(guildInfo, level, sender));
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
