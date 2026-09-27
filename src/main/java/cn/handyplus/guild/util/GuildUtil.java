package cn.handyplus.guild.util;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.constants.GuildConstants;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.enter.GuildSpawnPvpLocation;
import cn.handyplus.guild.enter.GuildTaskDemand;
import cn.handyplus.guild.hook.PlayerScoreboardUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.EntitySchedulerUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.param.GuildSpawn;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.service.GuildTaskDemandService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildUtil {
    private GuildUtil() {
    }

    public static Location getLocation(@NotNull GuildSpawnPvpLocation guildSpawnPvpLocation) {
        return new Location(Bukkit.getWorld((String)guildSpawnPvpLocation.getWorld()), guildSpawnPvpLocation.getX(), guildSpawnPvpLocation.getY(), guildSpawnPvpLocation.getZ(), (float)guildSpawnPvpLocation.getYaw(), (float)guildSpawnPvpLocation.getPitch());
    }

    public static void setHead(@NotNull Inventory inventory) {
        if (!BaseConstants.CONFIG.getBoolean("setHead", true)) {
            return;
        }
        Object holder = inventory.getHolder();
        if (!(holder instanceof HandyInventory)) {
            return;
        }
        Map objMap = ((HandyInventory)holder).getObjMap();
        if (MapUtil.isEmpty(objMap)) {
            return;
        }
        Iterator slotIterator = objMap.keySet().iterator();
        while (slotIterator.hasNext()) {
            Integer slot = (Integer)slotIterator.next();
            Object ownerName = objMap.get(slot);
            ItemStack itemStack = inventory.getItem(slot.intValue());
            if (itemStack == null) continue;
            if (itemStack.getType().equals((Object)Material.AIR)) continue;
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (!(itemMeta instanceof SkullMeta)) continue;
            itemMeta = (SkullMeta)itemMeta;
            ItemMetaUtil.setOwner((SkullMeta)itemMeta, (String)ownerName);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot.intValue(), itemStack);
        }
    }

    public static Map<String, List<String>> getDescriptionMap(@Nullable String description) {
        ArrayList<String> descriptionLines = new ArrayList<String>();
        if (StrUtil.isEmpty(description)) {
            descriptionLines.add(BaseUtil.getLangMsg("noDescription"));
        } else {
            descriptionLines.addAll(StrUtil.strToStrList(description));
        }
        return MapUtil.of("description", descriptionLines);
    }

    public static Location getLocation(@NotNull GuildSpawn guildSpawn) {
        return new Location(Bukkit.getWorld((String)guildSpawn.getWorld()), guildSpawn.getX(), guildSpawn.getY(), guildSpawn.getZ(), guildSpawn.getYaw(), guildSpawn.getPitch());
    }

    public static List<String> guildNamesWithVar() {
        ArrayList<String> names;
        ArrayList<String> allNames = names = new ArrayList<String>();
        names.add("${guildName}");
        allNames.addAll(GuildInfoService.getInstance().findNameAll());
        return allNames;
    }

    public static void cacheGuildTask(@NotNull Integer guildId) {
        boolean taskEnabled = ConfigUtil.TASK_CONFIG.getBoolean("enable", true);
        if (!PlayerGuild.USE_TASK || !taskEnabled) {
            return;
        }
        List<GuildTaskDemand> taskDemands = GuildTaskDemandService.getInstance().findByGuildId(guildId);
        GuildConstants.GUILD_TASK_DEMAND_MAP.put(guildId, taskDemands);
        List<Player> onlinePlayers = new ArrayList<Player>();
        Iterator<GuildPlayer> guildPlayerIterator = GuildPlayerService.getInstance().findByGuildId(guildId).iterator();
        while (guildPlayerIterator.hasNext()) {
            BaseUtil.getOnlinePlayer(guildPlayerIterator.next().getPlayerUuid()).ifPresent(onlinePlayers::add);
        }
        if (CollUtil.isNotEmpty(onlinePlayers)) {
            PlayerScoreboardUtil.addLine(onlinePlayers, guildId);
        }
    }

    public static void teleport(@NotNull Player player, @NotNull GuildSpawn guildSpawn, @NotNull String welcomeTitle) {
        EntitySchedulerUtil.teleport((Entity)player, GuildUtil.getLocation((GuildSpawn)guildSpawn));
        Map<String, String> placeholders = MapUtil.of("${player}", player.getName());
        MessageUtil.sendTitle(player, welcomeTitle, BaseUtil.getLangMsg("welcomeTitle", placeholders));
        GuildConstants.SPAWN_WAIT_TIME.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
