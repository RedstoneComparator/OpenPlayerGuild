package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.command.admin.GiveCommand;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AdminEditGuildNameCommand
implements IHandyCommandEvent {
    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 3, BaseUtil.getLangMsg("paramFailureMsg"));
        String useName = GiveCommand.getUseName(sender, args[1]);
        Optional<GuildInfo> guildOptional = GuildInfoService.getInstance().findByGuildName(useName);
        AssertUtil.notTrue(!guildOptional.isPresent(), BaseUtil.getLangMsg("joinFailureMsg"));
        GuildInfo guildInfo = guildOptional.get();
        String newName = args[2];
        Optional<GuildInfo> duplicateOptional = GuildInfoService.getInstance().findByGuildName(newName);
        String duplicateNameMsg = ConfigUtil.CREATE_CONFIG.getString("duplicateName", "");
        AssertUtil.notTrue(duplicateOptional.isPresent(), duplicateNameMsg);
        String replacedName = newName.replace("§", "&");
        GuildInfoService.getInstance().editGuildName(guildInfo.getId(), replacedName, 0);
        GuildWorldUtil.setWorldAlias(guildInfo.getWorldName(), replacedName);
        PlayerTitleUtil.setGuildTitle(guildInfo.getId());
        String message = ConfigUtil.SETTING_CONFIG.getString("editName.okButton", "").replace("${guildName}", newName);
        MessageUtil.sendMessage(sender, message);
        GuildCacheUtil.refresh(guildInfo.getId());
    }

    @Override
    public String command() {
        return "adminEditGuildName";
    }

    @Override
    public String permission() {
        return "playerGuild.adminEditGuildName";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(partialName -> GuildUtil.guildNamesWithVar()).nextLang("tabHelp.guildName");
    }
}
