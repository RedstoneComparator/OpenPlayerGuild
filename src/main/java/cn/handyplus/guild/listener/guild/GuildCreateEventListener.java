package cn.handyplus.guild.listener.guild;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.constants.ApplyTypeEnum;
import cn.handyplus.guild.constants.GuildRoleEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.event.GuildCreateEvent;
import cn.handyplus.guild.hook.LuckPermsUtil;
import cn.handyplus.guild.hook.PlayerChatUtil;
import cn.handyplus.guild.hook.PlayerTaskUtil;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildApplyService;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildLogoShopService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@HandyListener
public class GuildCreateEventListener
implements Listener {
    @EventHandler(priority=EventPriority.NORMAL)
    public void onGuildCreateCommandEvent(GuildCreateEvent event) {
        Player player = event.getPlayer();
        List<String> commands = ConfigUtil.CREATE_CONFIG.getStringList("command");
        if (CollUtil.isEmpty(commands)) {
            return;
        }
        for (String command : commands) {
            if (StrUtil.isEmpty(command)) continue;
            if (PlayerGuild.USE_PAPI) {
                command = PlaceholderAPI.setPlaceholders(player, command);
            }
            command = command.replace("${player}", player.getName());
            PlayerSchedulerUtil.dispatchCommand(command);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onGuildCreateEvent(GuildCreateEvent event) {
        String guildName = event.getGuildName().replace("§", "&");
        Player player = event.getPlayer();
        GuildApplyService.getInstance().updateApplyResultByPlayerUuid(player.getUniqueId(), ApplyTypeEnum.CANCEL.getId());
        GuildInfo guildInfo = new GuildInfo();
        guildInfo.setGuildName(guildName);
        String logoName = BaseConstants.CONFIG.getString("defaultLogo", "APPLE");
        logoName = ItemStackUtil.itemStackSerialize(new ItemStack(ItemStackUtil.getMaterial(logoName)));
        Optional<GuildLogoShop> logoShopOptional = GuildLogoShopService.getInstance().findDefaultLogo(1);
        if (logoShopOptional.isPresent()) {
            GuildLogoShop guildLogoShop = logoShopOptional.get();
            guildInfo.setLogoId(guildLogoShop.getId());
            logoName = guildLogoShop.getItemStack();
        }
        guildInfo.setLogoName(logoName);
        guildInfo.setLevel(1);
        guildInfo.setMoney(0);
        guildInfo.setProsperityDegree(0);
        guildInfo.setMonthProsperityDegree(0);
        guildInfo.setMemberCount(1);
        guildInfo.setMemberMaxCount(BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(guildInfo.getLevel()).append(".totalNumber").toString()));
        guildInfo.setSacredStoneLevel(1);
        guildInfo.setJoinMode(true);
        guildInfo.setCreator(player.getName());
        guildInfo.setCreateTime(new Date());
        guildInfo.setPvpStatus(ConfigUtil.SETTING_CONFIG.getBoolean("pvp.default", true));
        guildInfo.setOre(0);
        guildInfo.setSeasonRank(0);
        int guildId = GuildInfoService.getInstance().add(guildInfo);
        guildInfo.setId(guildId);
        if (logoShopOptional.isPresent()) {
            GuildLogoShop guildLogoShop2 = logoShopOptional.get();
            GuildLogo guildLogo = new GuildLogo();
            guildLogo.setGuildInfoId(guildId);
            guildLogo.setLogoId(guildLogoShop2.getId());
            guildLogo.setLogoName(guildLogoShop2.getItemStack());
            guildLogo.setUseFlag(true);
            GuildLogoService.getInstance().add(guildLogo);
        }
        GuildPlayer guildPlayer = new GuildPlayer();
        guildPlayer.setGuildInfoId(guildId);
        guildPlayer.setGuildInfoName(guildName);
        guildPlayer.setPlayerName(player.getName());
        guildPlayer.setPlayerUuid(player.getUniqueId());
        guildPlayer.setRoleId(GuildRoleEnum.ONE.getRoleId());
        guildPlayer.setPvpStatus(true);
        GuildPlayerService.getInstance().add(guildPlayer);
        PlayerTitleUtil.createTitle(guildInfo);
        PlayerTaskUtil.createTask(guildId);
        PlayerChatUtil.regChannel(guildId);
        LuckPermsUtil.createGroup(guildId);
        MessageUtil.sendMessage(player, ConfigUtil.CREATE_CONFIG.getString("createSucceedMsg"));
        String broadcastMessage = ConfigUtil.CREATE_CONFIG.getString("createSucceedAllMsg", "&8[&a✔&8] &7恭喜玩家&a${player}&7创建了${name}公会");
        if (StrUtil.isNotEmpty(broadcastMessage)) {
            broadcastMessage = broadcastMessage.replace("${player}", player.getName()).replace("${name}", guildName);
            MessageUtil.sendAllMessage(broadcastMessage);
        }
    }
}
