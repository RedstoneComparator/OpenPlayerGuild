package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.DbTypeEnum;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.SqlConvertUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ConvertCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(DbTypeEnum.getEnum());
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        SqlConvertUtil.convert(args[1]);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String command() {
        return "convert";
    }

    @Override
    public String permission() {
        return "playerGuild.convert";
    }
}
