package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.listener.gui.ConfirmClickEvent;
import cn.handyplus.guild.listener.gui.GuildManageMemberClickEvent;
import cn.handyplus.guild.service.GuildPlayerService;
import java.util.Optional;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetRoleCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextNull().next(GuildRoleEnum.getEnum());
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String permission() {
        return "playerGuild.setRole";
    }

    @Override
    public String command() {
        return "setRole";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 3, BaseUtil.getLangMsg("paramFailureMsg"));
        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(args[1]);
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(offlinePlayer.getUniqueId());
        AssertUtil.isTrue(guildPlayerOptional.isPresent(), BaseUtil.getLangMsg("noGuildMsg"));
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        GuildRoleEnum guildRoleEnum = GuildRoleEnum.getEnumThrow(AssertUtil.isPositiveToInt(args[2], BaseUtil.getLangMsg("amountFailureMsg")));
        if (GuildRoleEnum.ONE.equals(guildRoleEnum)) {
            Optional<GuildPlayer> oneRoleOptional = GuildPlayerService.getInstance().findOneRoleByGuildId(guildPlayer.getGuildInfoId());
            if (!oneRoleOptional.isPresent()) {
                return;
            }
            if (!oneRoleOptional.get().getId().equals(guildPlayer.getId())) {
                ConfirmClickEvent.upRole(guildPlayer, oneRoleOptional.get());
            }
        } else {
            GuildManageMemberClickEvent.setCache(guildPlayer, guildRoleEnum.getRoleId());
        }
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("succeedMsg"));
    }
}
