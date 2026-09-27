package cn.handyplus.guild.util;

import cn.handyplus.guild.enter.GuildBuff;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.GuildBuffEvent;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.FormulaUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.attribute.AttributeEnum;
import cn.handyplus.guild.lib.attribute.AttributeUtil;
import cn.handyplus.guild.service.GuildBuffService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class BuffManageUtil {
    private static void applyBuffAttributes(Player player, List<String> attributeList) {
        if (CollUtil.isEmpty(attributeList)) {
            return;
        }
        AttributeEnum attributeEnum = AttributeEnum.getEnum(ConfigUtil.SACRED_STONE_CONFIG.getString("buffType"));
        if (attributeEnum == null) {
            return;
        }
        AttributeUtil.getInstance().addAttribute(player, attributeList, attributeEnum);
    }

    public static void removeBuff(Player player) {
        if (player == null) {
            return;
        }
        AttributeEnum attributeEnum = AttributeEnum.getEnum(ConfigUtil.SACRED_STONE_CONFIG.getString("buffType"));
        if (attributeEnum == null) {
            return;
        }
        AttributeUtil.getInstance().removeAttribute(player, attributeEnum);
    }

    public static synchronized void refreshCallBuffEvent(Integer guildId) {
        Iterator<GuildPlayer> guildPlayerIterator;
        Iterator<GuildPlayer> guildPlayerIterator2 = guildPlayerIterator = GuildPlayerService.getInstance().findByGuildId(guildId).iterator();
        while (guildPlayerIterator2.hasNext()) {
            BaseUtil.getOnlinePlayer(guildPlayerIterator.next().getPlayerUuid()).ifPresent(BuffManageUtil::callBuffEvent);
            guildPlayerIterator2 = guildPlayerIterator;
        }
    }

    public static void setBuff(Player targetPlayer, List<String> attributeList) {
        Player player = targetPlayer;
        BuffManageUtil.removeBuff(player);
        BuffManageUtil.applyBuffAttributes(player, attributeList);
    }

    public static void callBuffEvent(@Nullable Player player) {
        if (player == null) {
            return;
        }
        Optional<GuildPlayer> optional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = optional.get();
        Map<String, GuildBuff> guildBuffs = GuildBuffService.getInstance().findByGuildId(guildPlayer.getGuildInfoId());
        if (guildBuffs.isEmpty()) {
            return;
        }
        ArrayList<String> buffAttributes = new ArrayList<String>();
        Iterator<String> buffKeyIterator = guildBuffs.keySet().iterator();
        while (buffKeyIterator.hasNext()) {
            String buffKey = (String)buffKeyIterator.next();
            List<String> attributeList = ConfigUtil.SACRED_STONE_CONFIG.getStringList(new StringBuilder().insert(0, "buff.").append(buffKey).append(".attribute").toString());
            if (CollUtil.isEmpty(attributeList)) continue;
            Map<String, String> levelPlaceholders = MapUtil.of("level", guildBuffs.get(buffKey).getLevel().toString());
            Iterator<String> attributeIterator = attributeList.iterator();
            while (attributeIterator.hasNext()) {
                String attribute = (String)attributeIterator.next();
                attribute = FormulaUtil.evaluateFormula(PlaceholderApiUtil.set(player, attribute), levelPlaceholders);
                buffAttributes.add(attribute);
            }
        }
        Bukkit.getServer().getPluginManager().callEvent((Event)new GuildBuffEvent(player, buffAttributes));
    }

    private BuffManageUtil() {
    }
}
