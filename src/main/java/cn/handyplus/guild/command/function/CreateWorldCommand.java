package cn.handyplus.guild.command.function;

import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Date;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateWorldCommand
implements IHandyCommandEvent {
    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        if (!ConfigUtil.SETTING_CONFIG.getBoolean("createGuildWorld.enable")) {
            MessageUtil.sendMessage((Player)sender, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        MessageUtil.sendMessage((Player)sender, BaseUtil.getLangMsg("createWorldMsg"));
        this.createWorld((Player)sender);
    }

    public synchronized void createWorld(Player player) {
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        if (!GuildRoleEnum.ONE.getRoleId().equals(guildPlayer.getRoleId())) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noPermission"));
            return;
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildPlayer.getGuildInfoId());
        if (!guildInfoOptional.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getLangMsg("noGuildMsg"));
            return;
        }
        if (StrUtil.isNotEmpty(guildInfoOptional.get().getWorldName())) {
            MessageUtil.sendMessage(player, ConfigUtil.WORLD_CONFIG.getString("HasBeenCreated"));
            return;
        }
        String folderOrAlias = ConfigUtil.SETTING_CONFIG.getString("createGuildWorld.format", "world_guild/");
        String worldName = ConfigUtil.WORLD_CONFIG.getString("worldName", "${playerName}_${dateTime}");
        worldName = new StringBuilder().insert(0, folderOrAlias).append(worldName.replace("${playerName}", player.getName().toLowerCase()).replace("${playerUuid}", player.getUniqueId().toString()).replace("${dateTime}", DateUtil.format(new Date(), "yyyyMMddHHmmss"))).toString();
        folderOrAlias = ConfigUtil.WORLD_CONFIG.getString("aliasName", "${guildName}").replace("${guildName}", guildPlayer.getGuildInfoName());
        GuildWorldUtil.createWorld(player, guildPlayer.getGuildInfoId(), worldName, folderOrAlias);
    }

    @Override
    public String command() {
        return "createWorld";
    }

    @Override
    public String permission() {
        return "playerGuild.createWorld";
    }
}
