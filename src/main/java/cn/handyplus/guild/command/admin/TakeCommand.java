package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.api.PlayerGuildApi;
import cn.handyplus.guild.command.admin.GiveCommand;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.GuildCacheUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TakeCommand
implements IHandyCommandEvent {

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 4, BaseUtil.getLangMsg("paramFailureMsg"));
        String type = args[1].toLowerCase(Locale.ROOT);
        Integer amount = AssertUtil.isPositiveToInt(args[3], BaseUtil.getLangMsg("amountFailureMsg"));
        String name = GiveCommand.getUseName(sender, args[2]);
        if (StrUtil.isEmpty(name)) {
            return;
        }
        int typeIndex = -1;
        switch (type.hashCode()) {
            case -1440193363: {
                if (!type.equals("guildmoney")) break;
                typeIndex = 0;
                break;
            }
            case -2050770759: {
                if (!type.equals("guildactive")) break;
                typeIndex = 1;
                break;
            }
            case -1306523121: {
                if (!type.equals("guildore")) break;
                typeIndex = 2;
                break;
            }
            case -985752863: {
                if (!type.equals("player")) break;
                typeIndex = 3;
            }
        }
        boolean success;
        switch (typeIndex) {
            case 0: {
                success = PlayerGuildApi.takeGuildMoneyByName(name, amount);
                GuildCacheUtil.refresh(name);
                break;
            }
            case 1: {
                success = PlayerGuildApi.takeGuildProsperityDegreeByName(name, amount);
                GuildCacheUtil.refresh(name);
                break;
            }
            case 2: {
                success = PlayerGuildApi.takeGuildOreByName(name, amount);
                GuildCacheUtil.refresh(name);
                break;
            }
            case 3: {
                success = GuildPlayerService.getInstance().subtractMoneyByPlayerName(name, amount);
                GuildCacheUtil.refreshByPlayerName(name);
                if (success) {
                    HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
                    placeholders.put("${player}", name);
                    placeholders.put("${amount}", amount.toString());
                    placeholders.put("${balance}", String.valueOf(GuildPlayerService.getInstance().findByPlayerName(name).map(GuildPlayer::getMoney).orElse(0)));
                    MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("takeMsg", placeholders));
                    MessageUtil.sendMessage(name, BaseUtil.getLangMsg("takePlayerMsg", placeholders));
                    return;
                }
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("failureMsg"));
                return;
            }
            default: {
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("paramFailureMsg"));
                return;
            }
        }
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg(success ? "succeedMsg" : "failureMsg"));
    }

    @Override
    public String permission() {
        return "playerGuild.take";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void tab(HandyTab tab) {
        String[] types = new String[4];
        types[0] = "guildMoney";
        types[1] = "guildActive";
        types[2] = "player";
        types[3] = "guildOre";
        HandyTab handyTab = tab = tab.next(Arrays.asList(types));
        handyTab.when("player").nextNull().nextLang("tabHelp.number");
        handyTab.when("guildMoney").next(partialName -> GuildUtil.guildNamesWithVar()).nextLang("tabHelp.number");
        tab.when("guildActive").next(partialName -> GuildUtil.guildNamesWithVar()).nextLang("tabHelp.number");
        tab.when("guildOre").next(partialName -> GuildUtil.guildNamesWithVar()).nextLang("tabHelp.number");
    }

    @Override
    public String command() {
        return "take";
    }
}
