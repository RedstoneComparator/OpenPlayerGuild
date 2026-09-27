package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.BeanUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.Page;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;

public class GuildLogoShopService {
    public int add(GuildLogoShop guildLogoShop) {
        return Db.use(GuildLogoShop.class).execution().insert(guildLogoShop);
    }

    private GuildLogoShopService() {
    }

    public void updateLogoName(Integer logoId, String itemStack) {
        Db<GuildLogoShop> db = Db.use(GuildLogoShop.class);
        db.update().set(GuildLogoShop::getItemStack, itemStack);
        db.execution().updateById(logoId);
    }

    public Optional<GuildLogoShop> findDefaultLogo(Integer level) {
        Db<GuildLogoShop> db = Db.use(GuildLogoShop.class);
        db.where().eq(GuildLogoShop::getDefaultLogoFlag, true).eq(GuildLogoShop::getLevel, level);
        return db.execution().selectOne();
    }

    public int update(GuildLogoShop guildLogoShop) {
        Db<GuildLogoShop> db = Db.use(GuildLogoShop.class);
        db.update().set(GuildLogoShop::getLevel, guildLogoShop.getLevel()).set(GuildLogoShop::getPrice, guildLogoShop.getPrice()).set(GuildLogoShop::getItemStack, guildLogoShop.getItemStack());
        return db.execution().updateById(guildLogoShop.getId());
    }

    public Page<GuildLogoShop> page(List<Integer> excludeIds, Integer pageNum, Integer pageSize, Boolean defaultLogoFlag) {
        Db<GuildLogoShop> db = Db.use(GuildLogoShop.class);
        db.where().notIn(CollUtil.isNotEmpty(excludeIds), GuildLogoShop::getId, excludeIds).eq(defaultLogoFlag != null, GuildLogoShop::getDefaultLogoFlag, defaultLogoFlag).limit(pageNum, pageSize).orderByDesc(GuildLogoShop::getId);
        return db.execution().page();
    }

    public Optional<GuildLogoShop> findById(Integer id) {
        return Db.use(GuildLogoShop.class).execution().selectById(id);
    }

    public void delById(Integer id) {
        Db.use(GuildLogoShop.class).execution().deleteById(id);
    }

    public static GuildLogoShopService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<GuildLogoShop> findAll() {
        return Db.use(GuildLogoShop.class).execution().list();
    }

    private static class SingletonHolder {
        private static final GuildLogoShopService INSTANCE = new GuildLogoShopService();
    }
}
