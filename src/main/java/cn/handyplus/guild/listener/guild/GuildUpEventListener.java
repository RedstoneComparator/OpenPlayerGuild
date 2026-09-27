package cn.handyplus.guild.listener.guild;

import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.event.GuildUpEvent;
import cn.handyplus.guild.hook.GuildWorldUtil;
import cn.handyplus.guild.hook.PlayerTitleUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildLogoShopService;
import cn.handyplus.guild.util.GuildCacheUtil;
import java.util.Optional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@HandyListener
public class GuildUpEventListener
implements Listener {
    @EventHandler
    public void onGuildUpEvent(GuildUpEvent event) {
        if (event.isCancelled()) {
            return;
        }
        GuildUpEvent guildUpEvent = event;
        GuildInfo guildInfo = guildUpEvent.getGuildInfo();
        Integer newLevel = guildUpEvent.getNewLevel();
        int memberMaxCount = BaseConstants.CONFIG.getInt(new StringBuilder().insert(0, "guildSetting.").append(newLevel).append(".totalNumber").toString(), guildInfo.getMemberMaxCount().intValue());
        GuildInfoService.getInstance().setLevel(guildInfo.getId(), newLevel, memberMaxCount);
        guildInfo.setLevel(newLevel);
        GuildWorldUtil.setGuildBorder(guildInfo);
        GuildWorldUtil.setGameRule(guildInfo.getWorldName());
        PlayerTitleUtil.setGuildTitle(guildInfo.getId());
        Optional<GuildLogoShop> logoShopOptional = GuildLogoShopService.getInstance().findDefaultLogo(newLevel);
        if (!logoShopOptional.isPresent()) {
            return;
        }
        GuildLogoShop guildLogoShop = logoShopOptional.get();
        GuildLogoService.getInstance().updateUseToFalse(guildInfo.getId());
        GuildLogo guildLogo = new GuildLogo();
        guildLogo.setGuildInfoId(guildInfo.getId());
        guildLogo.setLogoId(guildLogoShop.getId());
        guildLogo.setLogoName(guildLogoShop.getItemStack());
        guildLogo.setUseFlag(true);
        GuildLogoService.getInstance().add(guildLogo);
        GuildInfoService.getInstance().updateLogoById(guildInfo.getId(), guildLogoShop.getId(), guildLogoShop.getItemStack());
        GuildCacheUtil.refresh(guildInfo.getId());
    }
}
