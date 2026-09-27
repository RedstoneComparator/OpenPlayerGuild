package cn.handyplus.guild.util;

import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildLogoShopService;
import cn.handyplus.guild.service.GuildPlayerService;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class UpUtil {
    private UpUtil() {
    }

    public static void up() {
        List<GuildLogo> guildLogos = GuildLogoService.getInstance().findAll();
        if (CollUtil.isNotEmpty(guildLogos)) {
            for (GuildLogo guildLogo : guildLogos) {
                String logoName = guildLogo.getLogoName();
                if (StrUtil.isEmpty(logoName)) continue;
                if (logoName.contains("item:")) continue;
                String serializedItem = ItemStackUtil.itemStackSerialize(new ItemStack(ItemStackUtil.getMaterial(logoName)));
                GuildLogoService.getInstance().updateLogoName(guildLogo.getId(), serializedItem);
            }
        }
        List<GuildLogoShop> guildLogoShops = GuildLogoShopService.getInstance().findAll();
        if (CollUtil.isNotEmpty(guildLogoShops)) {
            for (GuildLogoShop guildLogoShop : guildLogoShops) {
                String shopItemStack = guildLogoShop.getItemStack();
                if (StrUtil.isEmpty(shopItemStack)) continue;
                if (shopItemStack.contains(LegacyUtil.J("!\u0017-\u000er"))) continue;
                String serializedItem = ItemStackUtil.itemStackSerialize(new ItemStack(ItemStackUtil.getMaterial(shopItemStack)));
                GuildLogoShopService.getInstance().updateLogoName(guildLogoShop.getId(), serializedItem);
            }
        }
        List<GuildInfo> guildInfos = GuildInfoService.getInstance().findAll();
        if (CollUtil.isNotEmpty(guildInfos)) {
            for (GuildInfo guildInfo : guildInfos) {
                String logoName = guildInfo.getLogoName();
                if (StrUtil.isEmpty(logoName)) continue;
                if (logoName.contains("item:")) continue;
                String serializedItem = ItemStackUtil.itemStackSerialize(new ItemStack(ItemStackUtil.getMaterial(logoName)));
                GuildInfoService.getInstance().updateLogoById(guildInfo.getId(), guildInfo.getLogoId(), serializedItem);
            }
        }
        GuildInfoService.getInstance().updateOre();
        GuildPlayerService.getInstance().updateOre();
    }
}
