package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.command.admin.GiveCommand;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.event.GuildDissolutionEvent;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.GuildUtil;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class DissolveCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(partialName -> GuildUtil.guildNamesWithVar());
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg(LegacyUtil.J("8\u0002:\u0002%%)\n$\u0016:\u0006\u0005\u0010/")));
        String useName = GiveCommand.getUseName(sender, args[1]);
        Optional<GuildInfo> guildOptional = GuildInfoService.getInstance().findByGuildName(useName);
        if (!guildOptional.isPresent()) {
            MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        Player player = null;
        if (sender instanceof Player) {
            player = (Player)sender;
        }
        GuildInfo guildInfo = guildOptional.get();
        Bukkit.getServer().getPluginManager().callEvent((Event)new GuildDissolutionEvent(player, guildInfo.getId(), guildInfo.getGuildName()));
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg(LegacyUtil.J("\u0010=\u0000+\u0006-\u0007\u0005\u0010/")));
    }

    @Override
    public String permission() {
        return "playerGuild.dissolve";
    }

    @Override
    public String command() {
        return LegacyUtil.J("\u0007!\u0010;\f$\u0015-");
    }
}
