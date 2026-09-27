package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.enter.GuildPvpLocation;
import cn.handyplus.guild.enter.GuildSpawnPvpLocation;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildPvpLocationService;
import cn.handyplus.guild.service.GuildSpawnPvpLocationService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildRunTaskTimer;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLocationCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.setLocation";
    }

    @Override
    public String command() {
        return "setLocation";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        int locationId;
        AssertUtil.notTrue(((String[])args).length < 4, BaseUtil.getLangMsg("paramFailureMsg"));
        String type = GuildActivityType.getTypeThrow(args[1]).getType();
        sender = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String name = args[2];
        String spawnName = args[3];
        Location location = ((Player)sender).getLocation();
        Optional<GuildPvpLocation> pvpLocationOptional = GuildPvpLocationService.getInstance().findByNameAndType(name, type);
        if (!pvpLocationOptional.isPresent()) {
            GuildPvpLocation guildPvpLocation = new GuildPvpLocation();
            guildPvpLocation.setName(name);
            guildPvpLocation.setType(type);
            locationId = GuildPvpLocationService.getInstance().add(guildPvpLocation);
        } else {
            locationId = pvpLocationOptional.get().getId();
        }
        GuildSpawnPvpLocation guildSpawnPvpLocation = new GuildSpawnPvpLocation();
        guildSpawnPvpLocation.setGuildPvpLocationId(locationId);
        guildSpawnPvpLocation.setSpawnName(spawnName);
        guildSpawnPvpLocation.setX(location.getX());
        guildSpawnPvpLocation.setY(location.getY());
        guildSpawnPvpLocation.setZ(location.getZ());
        guildSpawnPvpLocation.setYaw(location.getYaw());
        guildSpawnPvpLocation.setPitch(location.getPitch());
        guildSpawnPvpLocation.setWorld(location.getWorld() != null ? location.getWorld().getName() : null);
        GuildSpawnPvpLocationService.getInstance().add(guildSpawnPvpLocation);
        String message = ConfigUtil.PVP_LOCATION_CONFIG.getString("pvpLocation", "${name}");
        MessageUtil.sendMessage((Player)sender, message.replace("${name}", name).replace("${spawnName}", spawnName));
        GuildRunTaskTimer.addPluginChunkTicket();
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(GuildActivityType.gelAllNotScore()).nextLang("tabHelp.pvpName").nextLang("tabHelp.spawnName");
    }
}
