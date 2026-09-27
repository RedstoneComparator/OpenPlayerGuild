package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.api.PlayerGuildApi;
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
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "give";
    }

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
                success = PlayerGuildApi.giveGuildMoneyByName(name, amount);
                GuildCacheUtil.refresh(name);
                break;
            }
            case 1: {
                success = PlayerGuildApi.giveGuildProsperityDegreeByName(name, amount);
                GuildCacheUtil.refresh(name);
                break;
            }
            case 2: {
                success = PlayerGuildApi.giveGuildOreByName(name, amount);
                GuildCacheUtil.refresh(name);
                break;
            }
            case 3: {
                success = GuildPlayerService.getInstance().addMoneyByPlayerName(name, amount);
                GuildCacheUtil.refreshByPlayerName(name);
                if (success) {
                    HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
                    placeholders.put("${player}", name);
                    placeholders.put("${amount}", amount.toString());
                    placeholders.put("${balance}", String.valueOf(GuildPlayerService.getInstance().findByPlayerName(name).map(GuildPlayer::getMoney).orElse(0)));
                    MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("giveMsg", placeholders));
                    MessageUtil.sendMessage(name, BaseUtil.getLangMsg("givePlayerMsg", placeholders));
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
    public boolean isAsync() {
        return true;
    }

    public static String getUseName(CommandSender sender, String inputName) {
        if ("${guildName}".equalsIgnoreCase(inputName)) {
            if (BaseUtil.isNotPlayer(sender)) {
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
                return null;
            }
            Player player = (Player)sender;
            Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
            if (!guildPlayerOptional.isPresent()) {
                MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
                return null;
            }
            return guildPlayerOptional.get().getGuildInfoName();
        }
        if ("${player}".equalsIgnoreCase(inputName)) {
            if (BaseUtil.isNotPlayer(sender)) {
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
                return null;
            }
            Player player = (Player)sender;
            return player.getName();
        }
        return inputName;
    }

    @Override
    public String permission() {
        return "playerGuild.give";
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
}
