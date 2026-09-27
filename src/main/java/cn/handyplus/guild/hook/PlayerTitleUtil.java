package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.title.api.PlayerTitleApi;
import cn.handyplus.title.api.param.TitleBuffParam;
import cn.handyplus.title.api.param.TitleListParam;
import cn.handyplus.title.api.param.TitleRequireParam;
import cn.handyplus.title.constants.BuffApplyTypeEnum;
import cn.handyplus.title.constants.BuffTypeEnum;
import cn.handyplus.title.constants.BuyTypeEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerTitleUtil {
    private PlayerTitleUtil() {
    }

    public static void createTitle(GuildInfo guildInfo) {
        boolean enabled = ConfigUtil.TITLE_CONFIG.getBoolean("enable", true);
        if (!PlayerGuild.USE_TITLE || !enabled) {
            return;
        }
        Integer guildId = guildInfo.getId();
        Integer level = guildInfo.getLevel();
        String buffType = ConfigUtil.TITLE_CONFIG.getString("buffType");
        String titleName = ConfigUtil.TITLE_CONFIG.getString(level + ".title");
        List descriptionLines = ConfigUtil.TITLE_CONFIG.getStringList("description");
        List buffLines = ConfigUtil.TITLE_CONFIG.getStringList(level + ".buff");
        List displayLines = ConfigUtil.TITLE_CONFIG.getStringList(level + ".display");
        if (StrUtil.isEmpty(titleName)) {
            return;
        }
        titleName = titleName.replace("${guild_name}", guildInfo.getGuildName());
        TitleListParam titleParam = new TitleListParam();
        titleParam.setTitleName(titleName);
        titleParam.setDay(Integer.valueOf(0));
        titleParam.setIsHide(Integer.valueOf(1));
        titleParam.setDescription(CollUtil.listToStr(descriptionLines).replace("${guild_name}", guildInfo.getGuildName()));
        titleParam.setIsPrefixAndSuffix(Boolean.valueOf(false));
        titleParam.setBuffs(PlayerTitleUtil.getTitleBuffList(buffLines, buffType, displayLines));
        ArrayList<TitleRequireParam> requires = new ArrayList<TitleRequireParam>();
        requires.add(TitleRequireParam.builder().buyType(BuyTypeEnum.ACTIVITY).build());
        titleParam.setRequires(requires);
        Integer titleId = PlayerTitleApi.addTitle((TitleListParam)titleParam);
        GuildInfoService.getInstance().updateTitleIdById(guildId, titleId);
        List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByGuildId(guildId);
        for (GuildPlayer guildPlayer : guildPlayers) {
            PlayerTitleApi.setPlayerTitle((UUID)guildPlayer.getPlayerUuid(), (Integer)titleId);
        }
    }

    public static void setGuildTitle(Integer guildId) {
        boolean enabled = ConfigUtil.TITLE_CONFIG.getBoolean("enable", true);
        if (!PlayerGuild.USE_TITLE || !enabled) {
            return;
        }
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(guildId);
        if (!guildInfoOptional.isPresent()) {
            return;
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        Integer titleId = guildInfo.getTitleId();
        if (titleId == null) {
            PlayerTitleUtil.createTitle(guildInfo);
            return;
        }
        boolean exists = PlayerTitleApi.existTitleId((Integer)titleId);
        if (!exists) {
            PlayerTitleUtil.createTitle(guildInfo);
            return;
        }
        Integer level = guildInfo.getLevel();
        String buffType = ConfigUtil.TITLE_CONFIG.getString("buffType");
        List descriptionLines = ConfigUtil.TITLE_CONFIG.getStringList("description");
        String titleName = ConfigUtil.TITLE_CONFIG.getString(level + ".title");
        List buffLines = ConfigUtil.TITLE_CONFIG.getStringList(level + ".buff");
        List displayLines = ConfigUtil.TITLE_CONFIG.getStringList(level + ".display");
        if (StrUtil.isEmpty(titleName)) {
            return;
        }
        titleName = titleName.replace("${guild_name}", guildInfo.getGuildName());
        PlayerTitleApi.updateTitleName((Integer)titleId, (String)titleName, (boolean)false);
        PlayerTitleApi.updateTitleDesc((Integer)titleId, (String)CollUtil.listToStr(descriptionLines).replace("${guild_name}", guildInfo.getGuildName()));
        PlayerTitleApi.removeTitleBuff((Integer)titleId);
        PlayerTitleApi.addTitleBuff((Integer)titleId, PlayerTitleUtil.getTitleBuffList(buffLines, buffType, displayLines));
        List<GuildPlayer> guildPlayers = GuildPlayerService.getInstance().findByGuildId(guildId);
        for (GuildPlayer guildPlayer : guildPlayers) {
            PlayerTitleApi.setPlayerTitle((UUID)guildPlayer.getPlayerUuid(), (Integer)titleId);
        }
    }

    public static void removeTitle(Integer titleId) {
        if (!PlayerGuild.USE_TITLE) {
            return;
        }
        if (titleId == null) {
            return;
        }
        PlayerTitleApi.removeTitle((Integer)titleId);
    }

    public static void addGuildPlayerTitle(UUID playerUuid, Integer titleId) {
        if (!PlayerGuild.USE_TITLE) {
            return;
        }
        if (titleId == null) {
            return;
        }
        PlayerTitleApi.setPlayerTitle((UUID)playerUuid, (Integer)titleId);
    }

    public static void removeGuildPlayerTitle(UUID playerUuid, Integer titleId) {
        if (!PlayerGuild.USE_TITLE) {
            return;
        }
        if (titleId == null) {
            return;
        }
        PlayerTitleApi.removePlayerTitle((UUID)playerUuid, (Integer)titleId);
    }

    private static List<TitleBuffParam> getTitleBuffList(List<String> buffLines, String buffType, List<String> displayLines) {
        ArrayList<TitleBuffParam> result = new ArrayList<TitleBuffParam>();
        if (CollUtil.isEmpty(buffLines)) {
            return result;
        }
        String applyType = ConfigUtil.TITLE_CONFIG.getString("applyType");
        for (int i = 0; i < buffLines.size(); ++i) {
            String buffContent = buffLines.get(i);
            if (StrUtil.isEmpty(buffContent)) continue;
            TitleBuffParam buffParam = new TitleBuffParam();
            buffParam.setBuffType(BuffTypeEnum.getEnum((String)buffType));
            buffParam.setBuffContent(buffContent);
            if (CollUtil.isNotEmpty(displayLines) && displayLines.size() >= i + 1) {
                buffParam.setDescription(displayLines.get(i));
            }
            if (StrUtil.isNotEmpty(applyType)) {
                buffParam.setApplyTypeEnum(BuffApplyTypeEnum.getEnum((String)applyType));
            }
            result.add(buffParam);
        }
        return result;
    }
}
