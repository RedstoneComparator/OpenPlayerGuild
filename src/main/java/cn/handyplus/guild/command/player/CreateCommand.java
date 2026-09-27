package cn.handyplus.guild.command.player;

import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.inventory.GuildCreateGui;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CreateCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextLang(LegacyUtil.J("<\u0002*+-\u000f8M/\u0016!\u000f,-)\u000e-"));
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    public static void create(Player player, String guildName) {
        int maxLength = ConfigUtil.CREATE_CONFIG.getInt("guildNameLength");
        if (BaseUtil.stripColor(guildName).length() > maxLength) {
            String message = ConfigUtil.CREATE_CONFIG.getString(LegacyUtil.J("\u000f-\r/\u0017 %)\n$\u0016:\u0006\u0005\u0010/"), "").replace("${length}", maxLength + "");
            MessageUtil.sendMessage(player, message);
            return;
        }
        if ((guildName.contains(LegacyUtil.J("n")) || guildName.contains("§")) && !player.hasPermission(LegacyUtil.J("\u0013$\u00021\u0006:\u0004=\n$\u0007f\u0000:\u0006)\u0017-M+\f$\f:"))) {
            String message = ConfigUtil.CREATE_CONFIG.getString("colorFailureMsg", LegacyUtil.J("nW\u4f28\u6cc2\u6741Cn\u00028\u000f)\u001a-\u0011/\u0016!\u000f,M+\u0011-\u0002<\u0006f\u0000'\u000f'\u0011hE|\u6720\u9618\u6706\u5253\u5e99\u5f21\u8211\u5445\u7993\u76cc\u510f\u4f52"));
            MessageUtil.sendMessage(player, message);
            return;
        }
        List<String> blackList = ConfigUtil.CREATE_CONFIG.getStringList("nameBlackList");
        for (String blackName : blackList) {
            if (!guildName.contains(blackName)) continue;
            String blackListMsg = ConfigUtil.CREATE_CONFIG.getString(LegacyUtil.J("&\u0002%\u0006\n\u000f)\u0000#/!\u0010<%)\n$\u0016:\u0006\u0005\u0010/"), "").replace("${name}", blackName);
            MessageUtil.sendMessage(player, blackListMsg);
            return;
        }
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(player, ConfigUtil.CREATE_CONFIG.getString(LegacyUtil.J(" \u0002>\u0006\u000f\u0016!\u000f,")));
            return;
        }
        Optional<GuildInfo> guildOptional = GuildInfoService.getInstance().findByGuildName(guildName);
        if (guildOptional.isPresent()) {
            MessageUtil.sendMessage(player, ConfigUtil.CREATE_CONFIG.getString("duplicateName"));
            return;
        }
        Inventory inventory = GuildCreateGui.getInstance().createGui(player, guildName);
        PlayerSchedulerUtil.openInventory(player, inventory);
    }

    @Override
    public String permission() {
        return "playerGuild.create";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg(LegacyUtil.J("\r'3$\u00021\u0006:%)\n$\u0016:\u0006\u0005\u0010/")));
        String guildName = args[1].trim();
        CreateCommand.create(player, guildName);
    }

    @Override
    public String command() {
        return LegacyUtil.J("\u0000:\u0006)\u0017-");
    }
}
