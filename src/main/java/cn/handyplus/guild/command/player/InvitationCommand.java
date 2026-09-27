package cn.handyplus.guild.command.player;

import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.RgbTextUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildCoolingUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InvitationCommand
implements IHandyCommandEvent {
    private static boolean isJoinPapiMismatch(@Nullable Integer guildId, @NotNull String playerName) {
        String joinPapi = BaseConstants.CONFIG.getString("joinPapi");
        if (StrUtil.isEmpty(joinPapi)) {
            return false;
        }
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findOneRoleByGuildId(guildId);
        if (guildPlayerOptional.isPresent()) {
            String ownerValue = PlaceholderApiUtil.set(guildPlayerOptional.get().getPlayerUuid(), joinPapi);
            String targetValue = PlaceholderApiUtil.set(BaseUtil.getOfflinePlayer(playerName), joinPapi);
            if (!ownerValue.equals(targetValue)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        InvitationCommand.invitation(AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg")), args[1]);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String permission() {
        return "playerGuild.invitation";
    }

    public static void invitation(Player inviter, String targetName) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(inviter.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(inviter, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        if (guildPlayer.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            MessageUtil.sendMessage(inviter, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        int price = ConfigUtil.INVITATION_CONFIG.getInt("member.price");
        if (guildInfo.getMoney() < price) {
            MessageUtil.sendMessage(inviter, ConfigUtil.INVITATION_CONFIG.getString("member.noButton"));
            return;
        }
        Optional<Player> onlinePlayerOptional = BaseUtil.getOnlinePlayer(targetName);
        if (!onlinePlayerOptional.isPresent()) {
            MessageUtil.sendMessage(inviter, BaseUtil.getLangMsg("onlinePlayerMsg", MapUtil.of("${player}", targetName)));
            return;
        }
        Player player = onlinePlayerOptional.get();
        if (GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId()).isPresent()) {
            MessageUtil.sendMessage(inviter, BaseUtil.getLangMsg("invitationExistGuild"));
            return;
        }
        if (GuildCoolingUtil.getJoinCoolingRemainHours(player.getUniqueId()) > 0L) {
            MessageUtil.sendMessage(inviter, GuildCoolingUtil.getInvitationCoolingButton(player.getUniqueId()));
            return;
        }
        if (InvitationCommand.isJoinPapiMismatch(guildInfo.getId(), targetName)) {
            MessageUtil.sendMessage(inviter, BaseUtil.getLangMsg("joinPapiMsg"));
            return;
        }
        InvitationCommand.addInvitation(inviter, targetName, guildInfo);
    }

    private static void addInvitation(Player inviter, String targetName, GuildInfo guildInfo) {
        Player player;
        int price = ConfigUtil.INVITATION_CONFIG.getInt("member.price");
        List<Integer> invitationGuildIds = GuildConstants.INVITATION_PLAYER_MAP.get(targetName);
        if (CollUtil.isNotEmpty(invitationGuildIds)) {
            if (invitationGuildIds.contains(guildInfo.getId())) return;
            player = inviter;
            invitationGuildIds.add(guildInfo.getId());
            GuildInfoService.getInstance().subtractMoneyById(guildInfo.getId(), price);
        } else {
            invitationGuildIds = new ArrayList<Integer>();
            player = inviter;
            invitationGuildIds.add(guildInfo.getId());
            GuildConstants.INVITATION_PLAYER_MAP.put(targetName, invitationGuildIds);
            GuildInfoService.getInstance().subtractMoneyById(guildInfo.getId(), price);
        }
        InvitationCommand.sendInvitationMessage(player.getName(), guildInfo, targetName);
        MessageUtil.sendMessage(inviter, BaseUtil.getLangMsg("invitationSucceedMsg"));
    }

    private static void sendInvitationMessage(@NotNull String inviterName, @NotNull GuildInfo guildInfo, @NotNull String targetName) {
        String invitationMsg = ConfigUtil.INVITATION_CONFIG.getString("member.invitationMsg", "");
        invitationMsg = StrUtil.replace(invitationMsg, "player", inviterName);
        invitationMsg = StrUtil.replace(invitationMsg, "guildName", ((GuildInfo)guildInfo).getGuildName());
        RgbTextUtil rgbTextUtil = RgbTextUtil.init(ConfigUtil.INVITATION_CONFIG.getString("member.accept"));
        rgbTextUtil.addClickCommand("/plg acceptInvitation " + ((GuildInfo)guildInfo).getId() + " " + inviterName);
        rgbTextUtil.addExtra(RgbTextUtil.init(ConfigUtil.INVITATION_CONFIG.getString("member.refuse")).addClickCommand(new StringBuilder().insert(0, "/plg denyInvitation ").append(((GuildInfo)guildInfo).getId()).append(" ").append(inviterName).toString()));
        Optional<Player> onlinePlayerOptional = BaseUtil.getOnlinePlayer(targetName);
        if (onlinePlayerOptional.isPresent()) {
            Player player = onlinePlayerOptional.get();
            MessageUtil.sendMessage(player, invitationMsg);
            rgbTextUtil.send((CommandSender)player);
        }
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextNull();
    }

    @Override
    public String command() {
        return "invitation";
    }
}
