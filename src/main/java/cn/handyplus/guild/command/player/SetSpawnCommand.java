package cn.handyplus.guild.command.player;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.GuildSetSpawnEvent;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.setSpawn";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    public static void setSpawn(Player player) {
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
        Location location = player.getLocation();
        String worldName = Objects.requireNonNull(location.getWorld()).getName();
        List disabledWorlds = ConfigUtil.SETTING_CONFIG.getStringList("setSpawn.noWorld");
        if (CollUtil.isNotEmpty(disabledWorlds) && disabledWorlds.contains(worldName)) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noSetSpawn"));
            return;
        }
        GuildSetSpawnEvent.callEvent(location, guildPlayer.getGuildInfoId(), player);
    }

    @Override
    public String command() {
        return "setSpawn";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        SetSpawnCommand.setSpawn(AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg")));
    }
}
