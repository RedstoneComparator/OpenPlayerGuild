package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.LuckPermsUtil;
import cn.handyplus.guild.hook.PlayerTaskUtil;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildTaskService;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RefreshCommand
implements IHandyCommandEvent {
    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<GuildInfo> guildInfos = GuildInfoService.getInstance().findAll();
        if (CollUtil.isEmpty(guildInfos)) {
            MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("succeedMsg"));
            return;
        }
        List<Integer> ids = guildInfos.stream().map(GuildInfo::getId).collect(Collectors.toList());
        Map<Integer, List<GuildPlayer>> playersByGuildId = GuildPlayerService.getInstance().findMapByGuildInfoIds(ids);
        for (GuildInfo guildInfo : guildInfos) {
            PlayerTitleUtil.setGuildTitle(guildInfo.getId());
            if (GuildTaskService.getInstance().findCount(guildInfo.getId()) < 1) {
                PlayerTaskUtil.createTask(guildInfo.getId());
            }
            if (!GuildPlayerService.getInstance().findOneRoleByGuildId(guildInfo.getId()).isPresent()) {
                Optional<GuildPlayer> topPlayerOptional = GuildPlayerService.getInstance().findOneTotalMoneyByGuildId(guildInfo.getId());
                topPlayerOptional.ifPresent(guildPlayer -> GuildPlayerService.getInstance().updateRoleIdById(guildPlayer.getId(), GuildRoleEnum.ONE.getRoleId()));
            }
            Integer count = GuildPlayerService.getInstance().findCountByGuildId(guildInfo.getId());
            int totalNumberLimit = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(guildInfo.getLevel()).append(".totalNumber").toString());
            if (totalNumberLimit != 0) {
                GuildInfoService.getInstance().setMemberCount(guildInfo.getId(), count, totalNumberLimit);
            }
            String luckPermsGroup = LuckPermsUtil.createGroup(guildInfo.getId());
            Iterator<GuildPlayer> playerIterator = playersByGuildId.getOrDefault(guildInfo.getId(), new ArrayList<>()).iterator();
            while (playerIterator.hasNext()) {
                LuckPermsUtil.addUserToGroup(playerIterator.next().getPlayerUuid(), luckPermsGroup);
            }
            HandySchedulerUtil.runTask(() -> RefreshCommand.refreshGuildWorld(guildInfo));
            GuildCacheUtil.refresh(guildInfo.getId());
        }
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("succeedMsg"));
    }

    @Override
    public String permission() {
        return "playerGuild.refresh";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    private static void refreshGuildWorld(GuildInfo guild) {
        GuildInfo guildInfo = guild;
        GuildWorldUtil.setGuildBorder(guildInfo);
        GuildWorldUtil.setGameRule(guildInfo.getWorldName());
        GuildWorldUtil.setWorldAlias(guildInfo.getWorldName(), guild.getGuildName());
    }

    @Override
    public String command() {
        return "refresh";
    }
}
