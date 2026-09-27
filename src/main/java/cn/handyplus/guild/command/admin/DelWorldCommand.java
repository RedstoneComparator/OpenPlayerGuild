package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.command.admin.GiveCommand;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.ResidenceUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.GuildCacheUtil;
import cn.handyplus.guild.util.GuildUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DelWorldCommand
implements IHandyCommandEvent {
    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        String guildName = GiveCommand.getUseName(sender, this.getArg(args, 1, BaseUtil.getLangMsg("paramFailureMsg")));
        Optional<GuildInfo> guildOptional = GuildInfoService.getInstance().findByGuildName(guildName);
        AssertUtil.notTrue(!guildOptional.isPresent(), BaseUtil.getLangMsg("noGuildMsg"));
        GuildInfo guildInfo = guildOptional.get();
        AssertUtil.notTrue(StrUtil.isEmpty(guildInfo.getWorldName()), BaseUtil.getLangMsg("delWorldEmptyMsg"));
        List<UUID> playerUuids = new ArrayList<UUID>();
        for (GuildPlayer guildPlayer : GuildPlayerService.getInstance().findByGuildId(guildInfo.getId())) {
            playerUuids.add(guildPlayer.getPlayerUuid());
        }
        GuildWorldUtil.removeWorld(guildInfo.getWorldName(), playerUuids);
        GuildInfoService.getInstance().updateWorldName(guildInfo.getId(), null);
        GuildCacheUtil.initGuildWorldCache();
        ResidenceUtil.remove(guildInfo);
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("delWorldSucceedMsg", MapUtil.of("${guildName}", guildInfo.getGuildName())));
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(partialName -> GuildUtil.guildNamesWithVar());
    }

    @Override
    public String permission() {
        return "playerGuild.delWorld";
    }

    @Override
    public String command() {
        return "delWorld";
    }
}
