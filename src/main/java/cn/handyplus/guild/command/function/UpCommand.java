package cn.handyplus.guild.command.function;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.GuildUpEvent;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Optional;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpCommand
implements IHandyCommandEvent {
    private static void doGuildUp(@NotNull Player player, @NotNull GuildInfo guild, int moneyCost, @Nullable Consumer<Boolean> callback) {
        GuildInfo guildInfo = guild;
        GuildUpEvent guildUpEvent = new GuildUpEvent(guildInfo, guildInfo.getLevel() + 1, (CommandSender)player);
        Bukkit.getServer().getPluginManager().callEvent((Event)guildUpEvent);
        if (guildUpEvent.isCancelled()) {
            UpCommand.invokeCallback(callback, false);
            return;
        }
        GuildInfoService.getInstance().subtractMoneyById(guild.getId(), moneyCost);
        GuildCacheUtil.refresh(guild.getId());
        UpCommand.invokeCallback(callback, true);
    }

    public static void up(@NotNull Player player) {
        UpCommand.up(player, null);
    }

    public static void up(@NotNull Player player, @Nullable Consumer<Boolean> callback) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
            UpCommand.invokeCallback(callback, false);
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (guildPlayer.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            UpCommand.invokeCallback(callback, false);
            return;
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
        if (!guildInfoOptional.isPresent()) {
            UpCommand.invokeCallback(callback, false);
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        int upMoney = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(guildInfo.getLevel()).append(".upMoney").toString());
        int upProsperityDegree = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(guildInfo.getLevel()).append(".upProsperityDegree").toString());
        int upMember = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(guildInfo.getLevel()).append(".upMember").toString(), 0);
        if (guildInfo.getProsperityDegree() < upProsperityDegree) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("upProsperityDegree"));
            UpCommand.invokeCallback(callback, false);
            return;
        }
        if (guildInfo.getMoney() < upMoney) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("upMoney"));
            UpCommand.invokeCallback(callback, false);
            return;
        }
        if (guildInfo.getMemberCount() < upMember) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("upMember"));
            UpCommand.invokeCallback(callback, false);
            return;
        }
        if (BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(guildInfo.getLevel() + 1).append(".totalNumber").toString()) == 0) {
            MessageUtil.sendMessage(player, ConfigUtil.SETTING_CONFIG.getString("up.maxLevel"));
            UpCommand.invokeCallback(callback, false);
            return;
        }
        HandySchedulerUtil.runTask(() -> UpCommand.runGuildUpTask(player, (GuildInfo)((Object)guildPlayerOptional), upMoney, callback));
    }

    private static void invokeCallback(@Nullable Consumer<Boolean> callback, boolean success) {
        if (callback != null) {
            callback.accept(success);
        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String permission() {
        return "playerGuild.up";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        UpCommand.up(AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg")));
    }

    private static void runGuildUpTask(Player player, GuildInfo guildInfo, int moneyCost, Consumer callback) {
        UpCommand.doGuildUp(player, guildInfo, moneyCost, callback);
    }

    @Override
    public String command() {
        return "up";
    }
}
