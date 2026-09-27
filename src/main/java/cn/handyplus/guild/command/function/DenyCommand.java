package cn.handyplus.guild.command.function;

import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildApply;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildPlayerService;
import java.util.Map;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DenyCommand
implements IHandyCommandEvent {
    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        Integer applyId = AssertUtil.isPositiveToInt(args[1], BaseUtil.getLangMsg("amountFailureMsg"));
        DenyCommand.deny(AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg")), applyId);
    }

    private static void sendPlayerMessage(String message, Player player) {
        MessageUtil.sendMessage(player, message);
    }

    public static synchronized void deny(Player player, Integer applyId) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (guildPlayer.getRoleId() > GuildRoleEnum.TWO.getRoleId()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        Optional<GuildApply> applyOptional = GuildApplyService.getInstance().findById(applyId);
        if (!applyOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noApply"));
            return;
        }
        GuildApply guildApply = applyOptional.get();
        if (ApplyTypeEnum.CANCEL.getId().equals(guildApply.getApplyResult())) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("joinApplyCancelMsg").replace("${player}", guildApply.getPlayerName()));
            return;
        }
        if (!ApplyTypeEnum.PENDING.getId().equals(guildApply.getApplyResult())) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("refuseFailureMsg").replace("${applyPlayerName}", guildApply.getApplyPlayerName()));
            return;
        }
        guildApply.setApplyResult(ApplyTypeEnum.REFUSE.getId());
        guildApply.setApplyPlayerName(player.getName());
        guildApply.setApplyPlayerUuid(player.getUniqueId());
        GuildApplyService.getInstance().updateById(guildApply);
        MessageUtil.sendMessage(player, BaseUtil.getLangMsg("refuseSucceedMsg").replace("${player}", guildApply.getPlayerName()));
        Optional<Player> onlinePlayerOptional = BaseUtil.getOnlinePlayer(guildApply.getPlayerUuid());
        Map<String, String> placeholders = MapUtil.of("${player}", guildPlayer.getPlayerName(), "${guild_name}", guildPlayer.getGuildInfoName());
        String message = BaseUtil.getLangMsg("joinApplyRefuseMsg", placeholders);
        onlinePlayerOptional.ifPresent(onlinePlayer -> DenyCommand.sendPlayerMessage(message, onlinePlayer));
    }

    @Override
    public String permission() {
        return "playerGuild.deny";
    }

    @Override
    public String command() {
        return "deny";
    }
}
