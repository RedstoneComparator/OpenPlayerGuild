package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.core.match.MatchPvpUtil;
import cn.handyplus.guild.core.score.ScorePvpUtil;
import cn.handyplus.guild.core.season.SeasonPvpUtil;
import cn.handyplus.guild.core.stronghold.StrongholdPvpUtil;
import cn.handyplus.guild.enter.GuildPvp;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.DateUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.HandyRunnable;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildPvpLogService;
import cn.handyplus.guild.service.GuildPvpService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuildActivityGui {
    private final static GuildActivityGui INSTANCE = new GuildActivityGui();

    private String getStrongholdPvpButton(Integer guildId) {
        if (!StrongholdPvpUtil.isOpen()) {
            return ConfigUtil.ACTIVITY_CONFIG.getString("stopButton");
        }
        Integer waitSecond = StrongholdPvpUtil.getWaitSecond(guildId);
        if (waitSecond != null) {
            return ConfigUtil.ACTIVITY_CONFIG.getString("strongholdButton", "").replace("${time}", String.valueOf(waitSecond));
        }
        if (StrongholdPvpUtil.isPreparing(guildId) || StrongholdPvpUtil.isRunning(guildId)) {
            return ConfigUtil.ACTIVITY_CONFIG.getString("matePvpAcceptPrepareMsg");
        }
        return ConfigUtil.ACTIVITY_CONFIG.getString("yesButton");
    }

    private String getSeasonPvpButton(Integer guildId, GuildPvp guildPvp) {
        if (SeasonPvpUtil.isPreparing((Integer)guildId) || SeasonPvpUtil.isRunning((Integer)guildId)) {
            return ConfigUtil.ACTIVITY_CONFIG.getString("matePvpAcceptPrepareMsg");
        }
        String yesButton = ConfigUtil.ACTIVITY_CONFIG.getString("yesButton");
        String joinButton = ConfigUtil.ACTIVITY_CONFIG.getString("joinButton");
        String noButton = ConfigUtil.ACTIVITY_CONFIG.getString("noButton");
        if (guildPvp != null) {
            yesButton = guildPvp.getIsJoin() != false ? joinButton : noButton;
        }
        return yesButton;
    }

    private GuildActivityGui() {
    }

    private Map<String, String> getScoreMatePvpMap(Integer guildId) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(6);
        String previousValue = placeholders.put("score", String.valueOf(ScorePvpUtil.getScore(guildId)));
        HashMap<String, String> resultMap = placeholders;
        placeholders.put("rank", ScorePvpUtil.getRank(guildId));
        resultMap.put("rank_percent", ScorePvpUtil.getRankPercent(guildId));
        placeholders.put("startDate", ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvp.startDate", ""));
        placeholders.put("startTime", ConfigUtil.ACTIVITY_CONFIG.getString("scoreMatePvp.startTime", ""));
        placeholders.put("button", this.getScorePvpButton(guildId));
        return resultMap;
    }

    public void strongholdPvpButtonTask(HandyInventory inventory) {
        Integer guildId = inventory.getId();
        HandySchedulerUtil.runTaskTimer(new HandyRunnable(){

            @Override
            public void run() {
                if (StrongholdPvpUtil.getWaitSecond(guildId) == null) {
                    this.cancel();
                }
                if (StrongholdPvpUtil.isPreparing(guildId)) {
                    this.cancel();
                    inventory.syncClose();
                    return;
                }
                int count = inventory.getObj() != null ? Integer.parseInt(inventory.getObj().toString()) : 0;
                HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, inventory, "matePvp", GuildActivityGui.this.getMatePvpMap(guildId, count));
                HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, inventory, "scoreMatePvp", GuildActivityGui.this.getScoreMatePvpMap(guildId));
                HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, inventory, "strongholdPvp", GuildActivityGui.this.getStrongholdPvpMap(guildId));
            }
        }, 1L, 20L);
    }

    public void setInventoryDate(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.GUILD_ACTIVITY.getType());
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        this.setButtons(handyInventory);
    }

    public void matePvpButtonTask(HandyInventory inventory) {
        Integer guildId = inventory.getId();
        HandySchedulerUtil.runTaskTimer(new HandyRunnable(){

            @Override
            public void run() {
                if (MatchPvpUtil.getWaitSecond(inventory.getId()) == null) {
                    this.cancel();
                }
                if (MatchPvpUtil.isPreparing(guildId)) {
                    this.cancel();
                    inventory.syncClose();
                    return;
                }
                int count = inventory.getObj() != null ? Integer.parseInt(inventory.getObj().toString()) : 0;
                HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, inventory, "matePvp", GuildActivityGui.this.getMatePvpMap(inventory.getId(), count));
                HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, inventory, "scoreMatePvp", GuildActivityGui.this.getScoreMatePvpMap(inventory.getId()));
                HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, inventory, "strongholdPvp", GuildActivityGui.this.getStrongholdPvpMap(inventory.getId()));
            }
        }, 1L, 20L);
    }

    public static GuildActivityGui getInstance() {
        return INSTANCE;
    }

    public Inventory createGui(Player player, Integer guildId) {
        Object title = ConfigUtil.ACTIVITY_CONFIG.getString("title");
        String sound = ConfigUtil.ACTIVITY_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, (String)title);
        int size = ConfigUtil.ACTIVITY_CONFIG.getInt("size", 27);
        Object handyInventory = title = new HandyInventory(GuiTypeEnum.GUILD_ACTIVITY.getType(), (String)title, size, sound);
        ((HandyInventory)title).setPlayer(player);
        ((HandyInventory)handyInventory).setId(guildId);
        this.setInventoryDate((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    private Map<String, String> getStrongholdPvpMap(Integer guildId) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(5);
        String previousValue = placeholders.put("score", StrongholdPvpUtil.getScoreText(guildId));
        HashMap<String, String> resultMap = placeholders;
        placeholders.put("startDate", ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.startDate", ""));
        resultMap.put("startTime", ConfigUtil.ACTIVITY_CONFIG.getString("strongholdPvp.startTime", ""));
        placeholders.put("matchGuildSize", String.valueOf(ConfigUtil.ACTIVITY_CONFIG.getInt("strongholdPvp.matchGuildSize", 2)));
        placeholders.put("button", this.getStrongholdPvpButton(guildId));
        return resultMap;
    }

    private Map<String, String> getMatePvpMap(Integer guildId, int remaining) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        String button = ConfigUtil.ACTIVITY_CONFIG.getString("yesButton");
        String mateButton = ConfigUtil.ACTIVITY_CONFIG.getString("mateButton");
        remaining = ConfigUtil.ACTIVITY_CONFIG.getInt("matePvp.number") - remaining;
        boolean unlimited = ConfigUtil.ACTIVITY_CONFIG.getBoolean("matePvp.unlimited");
        if (remaining < 1 && !unlimited) {
            button = ConfigUtil.ACTIVITY_CONFIG.getString("stopButton");
        }
        if (remaining < 1) {
            remaining = 0;
        }
        placeholders.put("number", String.valueOf(remaining));
        placeholders.put("startDate", ConfigUtil.ACTIVITY_CONFIG.getString("matePvp.startDate", ""));
        placeholders.put("startTime", ConfigUtil.ACTIVITY_CONFIG.getString("matePvp.startTime", ""));
        if (!MatchPvpUtil.isOpen()) {
            HashMap<String, String> resultMap = placeholders;
            resultMap.put("button", ConfigUtil.ACTIVITY_CONFIG.getString("stopButton"));
            return resultMap;
        }
        if (MatchPvpUtil.isPreparing(guildId) || MatchPvpUtil.isRunning(guildId)) {
            String preparedMessage = ConfigUtil.ACTIVITY_CONFIG.getString("matePvpAcceptPrepareMsg");
            HashMap<String, String> resultMap = placeholders;
            resultMap.put("button", preparedMessage);
            return resultMap;
        }
        Integer waitSecond = MatchPvpUtil.getWaitSecond(guildId);
        if (waitSecond != null && StrUtil.isNotEmpty(mateButton)) {
            button = mateButton.replace("${time}", String.valueOf(waitSecond));
        }
        HashMap<String, String> resultMap = placeholders;
        resultMap.put("button", button);
        return resultMap;
    }

    private String getScorePvpButton(Integer guildId) {
        if (!ScorePvpUtil.isDateOpen() || !ScorePvpUtil.isTimeOpen()) {
            return ConfigUtil.ACTIVITY_CONFIG.getString("stopButton");
        }
        if (ScorePvpUtil.isPreparing(guildId) || ScorePvpUtil.isStarting(guildId) || ScorePvpUtil.isRunning(guildId)) {
            return ConfigUtil.ACTIVITY_CONFIG.getString("matePvpAcceptPrepareMsg");
        }
        if (ScorePvpUtil.isQueueing(guildId)) {
            return ConfigUtil.ACTIVITY_CONFIG.getString("scoreMateButton", "");
        }
        return ConfigUtil.ACTIVITY_CONFIG.getString("yesButton");
    }

    private void setButtons(HandyInventory inventory) {
        Optional<GuildPvp> guildPvpOptional = GuildPvpService.getInstance().findByGuildInfoId(inventory.getId());
        int logCount = GuildPvpLogService.getInstance().findByGuildInfoIdAndStartTime(inventory.getId(), DateUtil.getToday());
        inventory.setObj(logCount);
        HandyInventory handyInventory = inventory;
        HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, handyInventory, "seasonPvp", this.getSeasonPvpMap(handyInventory.getId(), guildPvpOptional.orElse(null)));
        HandyInventory handyInventory2 = inventory;
        HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, handyInventory2, "matePvp", this.getMatePvpMap(handyInventory2.getId(), logCount));
        HandyInventory handyInventory3 = inventory;
        HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, handyInventory3, "scoreMatePvp", this.getScoreMatePvpMap(handyInventory3.getId()));
        HandyInventory handyInventory4 = inventory;
        HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, handyInventory4, "strongholdPvp", this.getStrongholdPvpMap(handyInventory4.getId()));
        HandyInventoryUtil.setButton(ConfigUtil.ACTIVITY_CONFIG, inventory, "back");
        HandyInventoryUtil.setCustomButton(ConfigUtil.ACTIVITY_CONFIG, inventory, "custom");
    }

    private Map<String, String> getSeasonPvpMap(Integer guildId, GuildPvp guildPvp) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        String button = this.getSeasonPvpButton(guildId, guildPvp);
        String previousValue = placeholders.put("number", GuildPvpService.getInstance().findSeason().toString());
        HashMap<String, String> resultMap = placeholders;
        placeholders.put("startDate", String.valueOf(ConfigUtil.ACTIVITY_CONFIG.getInt("seasonPvp.startDate")));
        resultMap.put("startTime", ConfigUtil.ACTIVITY_CONFIG.getString("seasonPvp.startTime", ""));
        placeholders.put("button", button);
        return resultMap;
    }
}
