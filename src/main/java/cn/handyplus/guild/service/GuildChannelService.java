package cn.handyplus.guild.service;

import cn.handyplus.guild.core.score.ScorePvpReward;
import cn.handyplus.guild.enter.GuildChannel;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.GameRuleUtil;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildChannelService {
    public Optional<GuildChannel> findByPlayerUuidAndServerName(UUID playerUuid, String serverName) {
        Db<GuildChannel> db = Db.use(GuildChannel.class);
        db.where().eq(GuildChannel::getPlayerUuid, playerUuid).eq(GuildChannel::getServerName, serverName);
        return db.execution().selectOne();
    }

    public void add(GuildChannel channel) {
        channel.setId(this.findMaxId());
        Db.use(GuildChannel.class).execution().insert(channel);
    }

    public Integer findMaxId() {
        Db<GuildChannel> db = Db.use(GuildChannel.class);
        db.where().orderByDesc(GuildChannel::getId).limit(1, 1);
        return db.execution().selectOne().map(channel -> channel.getId() + 1).orElse(1);
    }

    public void delById(Integer id) {
        Db.use(GuildChannel.class).execution().deleteById(id);
    }

    private GuildChannelService() {
    }

    public static GuildChannelService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<GuildChannel> findAll() {
        return Db.use(GuildChannel.class).execution().list();
    }

    private static class SingletonHolder {
        private static final GuildChannelService INSTANCE = new GuildChannelService();
    }
}
