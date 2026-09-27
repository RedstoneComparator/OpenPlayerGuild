package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.event.GuildCreateEvent;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCreateCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "adminCreate";
    }

    @Override
    public String permission() {
        return "playerGuild.adminCreate";
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextLang("tabHelp.guildName");
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(((String[])args).length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        String guildName = args[1];
        Player player;
        if (((String[])args).length > 2) {
            Optional<Player> playerOptional = BaseUtil.getOnlinePlayer(args[2]);
            String langMsg = BaseUtil.getLangMsg("onlinePlayerMsg", MapUtil.of("${player}", args[2]));
            AssertUtil.notTrue(!playerOptional.isPresent(), langMsg);
            player = playerOptional.get();
        } else {
            player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        }
        Optional<GuildInfo> guildOptional = GuildInfoService.getInstance().findByGuildName(guildName);
        if (guildOptional.isPresent()) {
            MessageUtil.sendMessage(sender, ConfigUtil.CREATE_CONFIG.getString("duplicateName"));
            return;
        }
        GuildCreateEvent.callEvent(player, guildName);
    }
}
