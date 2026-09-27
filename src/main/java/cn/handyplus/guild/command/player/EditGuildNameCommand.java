package cn.handyplus.guild.command.player;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditGuildNameCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "editGuildName";
    }

    public static void editGuildName(Player player, String guildName) {
        int maxLength = ConfigUtil.CREATE_CONFIG.getInt("guildNameLength");
        if (BaseUtil.stripColor(guildName).length() > maxLength) {
            String message = ConfigUtil.CREATE_CONFIG.getString("lengthFailureMsg", "").replace("${length}", maxLength + "");
            MessageUtil.sendMessage(player, message);
            return;
        }
        if ((guildName.contains("&") || guildName.contains("§")) && !player.hasPermission("playerguild.create.color")) {
            String message = ConfigUtil.CREATE_CONFIG.getString("colorFailureMsg", "&4你没有 &aplayerguild.create.color &4权限来创建彩色名称的公会");
            MessageUtil.sendMessage(player, message);
            return;
        }
        List<String> blackList = ConfigUtil.CREATE_CONFIG.getStringList("nameBlackList");
        for (String blackName : blackList) {
            if (!guildName.contains(blackName)) continue;
            String blackListMsg = ConfigUtil.CREATE_CONFIG.getString("nameBlackListFailureMsg", "").replace("${name}", blackName);
            MessageUtil.sendMessage(player, blackListMsg);
            return;
        }
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
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
        if (GuildInfoService.getInstance().findByGuildName(guildName).isPresent()) {
            MessageUtil.sendMessage(player, ConfigUtil.CREATE_CONFIG.getString("duplicateName"));
            return;
        }
        int price = ConfigUtil.SETTING_CONFIG.getInt("editName.price");
        if (guildInfo.getMoney() < price) {
            MessageUtil.sendMessage(player, ConfigUtil.SETTING_CONFIG.getString("editName.noButton"));
            return;
        }
        String replacedName = guildName.replace("§", "&");
        GuildInfoService.getInstance().editGuildName(guildInfo.getId(), replacedName, price);
        GuildWorldUtil.setWorldAlias(guildInfo.getWorldName(), replacedName);
        PlayerTitleUtil.setGuildTitle(guildInfo.getId());
        guildName = ConfigUtil.SETTING_CONFIG.getString("editName.okButton", "").replace("${guildName}", guildName);
        MessageUtil.sendMessage(player, guildName);
        GuildCacheUtil.refresh(guildInfo.getId());
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextLang("tabHelp.guildName");
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String guildName = args[1].trim();
        EditGuildNameCommand.editGuildName(player, guildName);
    }

    @Override
    public String permission() {
        return "playerGuild.editGuildName";
    }
}
