package cn.handyplus.guild.service;

import cn.handyplus.guild.core.score.ScorePvpData;
import cn.handyplus.guild.enter.GuildPlayerSignIn;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.Db;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildPlayerSignInService {
    public static String pPPppP(String value) {
        int length = value.length();
        int index = length - 1;
        char[] chars = new char[length];
        int xorKeyOne = 96;
        int remaining = index;
        int xorKeyTwo = 15;
        while (remaining >= 0) {
            int posOne = index--;
            chars[posOne] = (char)(value.charAt(posOne) ^ xorKeyTwo);
            if (index < 0) break;
            int posTwo = index--;
            chars[posTwo] = (char)(value.charAt(posTwo) ^ xorKeyOne);
            remaining = index;
        }
        return new String(chars);
    }

    public static GuildPlayerSignInService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int add(GuildPlayerSignIn guildPlayerSignIn) {
        return Db.use(GuildPlayerSignIn.class).execution().insert(guildPlayerSignIn);
    }

    public List<GuildPlayerSignIn> findAll() {
        return Db.use(GuildPlayerSignIn.class).execution().list();
    }

    private GuildPlayerSignInService() {
    }

    public Optional<GuildPlayerSignIn> findByPlayerUuid(UUID playerUuid) {
        Db<GuildPlayerSignIn> db = Db.use(GuildPlayerSignIn.class);
        db.where().eq(GuildPlayerSignIn::getPlayerUuid, playerUuid).ge(GuildPlayerSignIn::getSignInTime, DateUtil.getToday());
        return db.execution().selectOne();
    }

    private static class SingletonHolder {
        private static final GuildPlayerSignInService INSTANCE = new GuildPlayerSignInService();
    }
}
