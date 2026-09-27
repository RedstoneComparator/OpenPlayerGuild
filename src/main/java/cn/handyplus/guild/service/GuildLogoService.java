package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.service.GuildNoticeService;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;

public class GuildLogoService {
    public static String pPPppP(String input) {
        int length = input.length();
        int index = length - 1;
        char[] chars = new char[length];
        int lowKey = 39;
        int cursor = index;
        int highKey = 66;
        while (cursor >= 0) {
            int highPos = index--;
            chars[highPos] = (char)(input.charAt(highPos) ^ highKey);
            if (index < 0) break;
            int lowPos = index--;
            chars[lowPos] = (char)(input.charAt(lowPos) ^ lowKey);
            cursor = index;
        }
        return new String(chars);
    }

    public void updateLogoNameByLogoId(Integer logoId, String logoName) {
        Db<GuildLogo> db = Db.use(GuildLogo.class);
        db.update().set(GuildLogo::getLogoName, logoName);
        db.where().eq(GuildLogo::getLogoId, logoId);
        db.execution().update();
    }

    public void updateUseById(Integer id) {
        Db<GuildLogo> db = Db.use(GuildLogo.class);
        db.update().set(GuildLogo::getUseFlag, true);
        db.execution().updateById(id);
    }

    public void updateUseToFalse(Integer guildInfoId) {
        Db<GuildLogo> db = Db.use(GuildLogo.class);
        db.where().eq(GuildLogo::getGuildInfoId, guildInfoId);
        db.update().set(GuildLogo::getUseFlag, false);
        db.execution().update();
    }

    public static GuildLogoService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<GuildLogo> findAll() {
        return Db.use(GuildLogo.class).execution().list();
    }

    private GuildLogoService() {
    }

    public void updateLogoName(Integer id, String logoName) {
        Db<GuildLogo> db = Db.use(GuildLogo.class);
        db.update().set(GuildLogo::getLogoName, logoName);
        db.execution().updateById(id);
    }

    public Optional<GuildLogo> findById(Integer id) {
        return Db.use(GuildLogo.class).execution().selectById(id);
    }

    public List<GuildLogo> list(Integer guildInfoId) {
        Db<GuildLogo> db = Db.use(GuildLogo.class);
        db.where().eq(GuildLogo::getGuildInfoId, guildInfoId).orderByDesc(GuildLogo::getId);
        return db.execution().list();
    }

    public void delByLogoId(Integer logoId) {
        Db<GuildLogo> db = Db.use(GuildLogo.class);
        db.where().eq(GuildLogo::getLogoId, logoId);
        db.execution().delete();
    }

    public int add(GuildLogo logo) {
        return Db.use(GuildLogo.class).execution().insert(logo);
    }

    public void delByGuildId(Integer guildInfoId) {
        Db<GuildLogo> db = Db.use(GuildLogo.class);
        db.where().eq(GuildLogo::getGuildInfoId, guildInfoId);
        db.execution().delete();
    }

    public Page<GuildLogo> page(Integer guildInfoId, Integer pageNum, Integer pageSize) {
        Db<GuildLogo> db = Db.use(GuildLogo.class);
        db.where().eq(GuildLogo::getGuildInfoId, guildInfoId).limit(pageNum, pageSize).orderByDesc(GuildLogo::getId);
        return db.execution().page();
    }

    private static class SingletonHolder {
        private static final GuildLogoService INSTANCE = new GuildLogoService();
    }
}
