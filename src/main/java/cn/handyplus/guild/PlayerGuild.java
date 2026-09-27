package cn.handyplus.guild;

import cn.handyplus.guild.hook.MultiverseLegacyUtil;
import cn.handyplus.guild.hook.PlaceholderUtil;
import cn.handyplus.guild.hook.PlayerChatUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HookPluginEnum;
import cn.handyplus.guild.lib.HookPluginUtil;
import cn.handyplus.guild.lib.InitApi;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.SignService;
import cn.handyplus.guild.lib.hologram.HdUtil;
import cn.handyplus.guild.listener.guild.GuildPluginMessageListener;
import cn.handyplus.guild.listener.hook.AdaptAdaptationUseEventListener;
import cn.handyplus.guild.listener.hook.DamageApEventListener;
import cn.handyplus.guild.listener.hook.PlayerTaskScheduleEventListener;
import cn.handyplus.guild.util.ConfigUtil;
import cn.handyplus.guild.util.GuildRunTaskTimer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerGuild
extends JavaPlugin {
    public static boolean USE_RES;
    public static PlayerGuild INSTANCE;
    public static boolean USE_CHAT;
    public static boolean USE_PLY;
    public static boolean USE_WORLD_EDIT;
    public static boolean USE_WORLDS;
    public static PlayerPoints PLAYER_POINTS;
    public static boolean USE_WORLD_BORDER;
    public static boolean USE_RACE;
    public static boolean USE_ADAPT;
    public static boolean USE_PSB;
    public static boolean USE_MV_4;
    public static boolean USE_PAPI;
    public static Economy ECON;
    public static boolean USE_MY_PET_3;
    public static boolean USE_LP;
    public static boolean USE_MY_PET_4;
    public static boolean USE_MONSTER_API;
    public static boolean USE_TITLE;
    public static boolean USE_MV_5;
    public static boolean USE_TASK;

    public void onEnable() {
        Iterator<String> lineIterator;
        ArrayList<String> listeners;
        InitApi initApi;
        block12: {
            block11: {
                INSTANCE = this;
                initApi = InitApi.getInstance(INSTANCE);
                ConfigUtil.init();
                USE_PAPI = HookPluginUtil.hook(HookPluginEnum.PLACEHOLDER_API);
                if (USE_PAPI) {
                    new PlaceholderUtil(this).register();
                }
                this.initVaultEconomy();
                HookPluginUtil.hookToPlugin(HookPluginEnum.PLAYER_POINTS).ifPresent(pointsPlugin -> {
                    PLAYER_POINTS = (PlayerPoints)pointsPlugin;
                });
                USE_MV_5 = HookPluginUtil.hook(HookPluginEnum.MULTIVERSE_CORE, 5);
                if (!USE_MV_5) {
                    if (HookPluginUtil.hook(HookPluginEnum.MULTIVERSE_CORE, 4)) {
                        USE_MV_4 = true;
                        MultiverseLegacyUtil.init();
                    }
                }
                USE_WORLD_BORDER = HookPluginUtil.hook(HookPluginEnum.WORLD_BORDER);
                USE_TITLE = HookPluginUtil.hook(HookPluginEnum.PLAYER_TITLE);
                USE_TASK = HookPluginUtil.hook(HookPluginEnum.PLAYER_TASK);
                USE_RACE = HookPluginUtil.hook(HookPluginEnum.PLAYER_RACE);
                USE_WORLD_EDIT = HookPluginUtil.hook(HookPluginEnum.WORLD_EDIT);
                USE_CHAT = HookPluginUtil.hook(HookPluginEnum.PLAYER_CHAT);
                USE_ADAPT = HookPluginUtil.hook(HookPluginEnum.ADAPT);
                USE_PLY = HookPluginUtil.hook(HookPluginEnum.PLAYER_CURRENCY);
                USE_MY_PET_4 = HookPluginUtil.hook(HookPluginEnum.MY_PET, 4);
                if (!USE_MY_PET_4) {
                    if (HookPluginUtil.hook(HookPluginEnum.MY_PET, 3)) {
                        USE_MY_PET_3 = true;
                    }
                }
                USE_LP = HookPluginUtil.hook(HookPluginEnum.LUCK_PERMS);
                USE_MONSTER_API = HookPluginUtil.hook(HookPluginEnum.MONSTER_API);
                USE_PSB = HookPluginUtil.hook(HookPluginEnum.PLAYER_SCOREBOARD);
                USE_RES = HookPluginUtil.hook(HookPluginEnum.RESIDENCE);
                USE_WORLDS = HookPluginUtil.hook(HookPluginEnum.WORLDS);
                listeners = new ArrayList<String>();
                if (!USE_TASK) {
                    listeners.add(PlayerTaskScheduleEventListener.class.getName());
                }
                if (!USE_ADAPT) break block11;
                if (ConfigUtil.KIT_PVP_CONFIG.getBoolean("adapt", false)) break block12;
            }
            listeners.add(AdaptAdaptationUseEventListener.class.getName());
        }
        if (!USE_MONSTER_API) {
            listeners.add(DamageApEventListener.class.getName());
        }
        String[] logoLines = new String[7];
        logoLines[0] = "";
        logoLines[1] = "  ____  _                        ____       _ _     _ ";
        logoLines[2] = " |  _ \\| | __ _ _   _  ___ _ __ / ___|_   _(_) | __| |";
        logoLines[3] = " | |_) | |/ _` | | | |/ _ \\ '__| |  _| | | | | |/ _` |";
        logoLines[4] = " |  __/| | (_| | |_| |  __/ |  | |_| | |_| | | | (_| |";
        logoLines[5] = " |_|   |_|\\__,_|\\__, |\\___|_|   \\____|\\__,_|_|_|\\__,_|";
        logoLines[6] = "                |___/                                  ";
        Iterator<String> lineIteratorRef = lineIterator = Arrays.asList(logoLines).iterator();
        while (lineIteratorRef.hasNext()) {
            String logoLine = lineIterator.next();
            MessageUtil.sendConsoleMessage(new StringBuilder().insert(0, "&a").append(logoLine).toString());
            lineIteratorRef = lineIterator;
        }

        initApi.enableBc().enableSqlBackup().initCommand("cn.handyplus.guild.command").initListener("cn.handyplus.guild.listener", listeners).initClickEvent("cn.handyplus.guild.listener.gui").enableSql("cn.handyplus.guild.enter");
        new GuildPluginMessageListener();
        GuildRunTaskTimer.initTask();
        HdUtil.init();
        MessageUtil.sendConsoleMessage("&a已成功载入服务器！");
        MessageUtil.sendConsoleMessage("&aAuthor:handy WIKI: https://ricedoc.handyplus.cn/wiki/PlayerGuild/README/");
        MessageUtil.sendConsoleMessage("&ahttps://github.com/RedstoneComparator/OpenPlayerGuild");
    }

    public void onDisable() {
        InitApi.disable();
        PlayerChatUtil.unRegChannel();
    }

    private void initVaultEconomy() {
        if (this.getServer().getPluginManager().getPlugin(HookPluginEnum.VAULT.getName()) == null) {
            MessageUtil.sendConsoleMessage(BaseUtil.getLangMsg(HookPluginEnum.VAULT.getFailMsg()));
            return;
        }
        RegisteredServiceProvider economyRegistration = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyRegistration == null) {
            MessageUtil.sendConsoleMessage(BaseUtil.getLangMsg(HookPluginEnum.VAULT.getFailMsg()));
            return;
        }
        ECON = (Economy)economyRegistration.getProvider();
        MessageUtil.sendConsoleMessage(BaseUtil.getLangMsg(HookPluginEnum.VAULT.getSuccessMsg()));
    }
}
