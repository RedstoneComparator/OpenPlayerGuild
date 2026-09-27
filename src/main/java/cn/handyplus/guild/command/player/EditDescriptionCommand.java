package cn.handyplus.guild.command.player;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditDescriptionCommand
implements IHandyCommandEvent {
    public static void editDescription(Player player, String description) {
        Optional<GuildPlayer> guildPlayerOptional;
        int maxLength = ConfigUtil.SETTING_CONFIG.getInt("description.maxFont");
        if (BaseUtil.stripColor(description).length() > maxLength) {
            String message = ConfigUtil.CREATE_CONFIG.getString("lengthFailureMsg", "").replace("${length}", maxLength + "");
            MessageUtil.sendMessage(player, message);
            return;
        }
        List<String> descriptionLines = StrUtil.strToStrList(description);
        maxLength = ConfigUtil.SETTING_CONFIG.getInt("description.maxLine");
        for (String line : descriptionLines) {
            if (BaseUtil.stripColor(line).length() <= maxLength) continue;
            String message = ConfigUtil.CREATE_CONFIG.getString("lengthFailureMsg", "").replace("${length}", maxLength + "");
            MessageUtil.sendMessage(player, message);
            return;
        }
        List<String> blackList = ConfigUtil.CREATE_CONFIG.getStringList("nameBlackList");
        for (String blackName : blackList) {
            if (!description.contains((CharSequence)blackName)) continue;
            String message = ConfigUtil.CREATE_CONFIG.getString("nameBlackListFailureMsg", "").replace("${name}", (CharSequence)blackName);
            MessageUtil.sendMessage(player, message);
            return;
        }
        guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (guildPlayer.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        int price = ConfigUtil.SETTING_CONFIG.getInt("description.price");
        if (guildInfo.getMoney() < price) {
            MessageUtil.sendMessage(player, ConfigUtil.SETTING_CONFIG.getString("description.noButton"));
            return;
        }
        GuildInfoService.getInstance().editguildDescription(guildInfo.getId(), description, price);
        description = ConfigUtil.SETTING_CONFIG.getString("description.okButton", "");
        MessageUtil.sendMessage(player, description);
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextLang("tabHelp.description");
    }

    @Override
    public String permission() {
        return "playerGuild.editDescription";
    }

    @Override
    public String command() {
        return "editDescription";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        sender = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String description = args[1].trim();
        EditDescriptionCommand.editDescription((Player)sender, description);
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
