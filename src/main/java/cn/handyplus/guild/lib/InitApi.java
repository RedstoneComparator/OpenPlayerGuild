package cn.handyplus.guild.lib;

import java.util.List;

import cn.handyplus.guild.runnable.TableCreateRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public final class InitApi {
    private static final InitApi oOOoOO = new InitApi();
    public static JavaPlugin PLUGIN;

    private InitApi() {
    }

    public static InitApi getInstance(JavaPlugin plugin) {
        PLUGIN = plugin;
        BaseConstants.VERSION_CHECK_ENUM = VersionCheckEnum.getEnum();
        BaseConstants.VERSION_ID = BaseConstants.VERSION_CHECK_ENUM.getVersionId();
        HandySchedulerUtil.init(plugin);
        LegacyUtil.initClasses();
        CompatCore.init(BaseConstants.VERSION_ID);
        LoginCompatUtil.init(plugin);
        return oOOoOO;
    }

    public InitApi initCommand(String packageName) {
        HandyCommandWrapper.initCommand(packageName);
        return this;
    }

    public InitApi enablePlayerInput() {
        PlayerInputApi.enable(PLUGIN);
        return this;
    }

    public InitApi initListener(String packageName, List<String> exclusions) {
        HandyInventoryWrapper.initListener(packageName, exclusions);
        return this;
    }

    public InitApi initListener(String packageName) {
        return initListener(packageName, null);
    }

    public InitApi initClickEvent(String packageName) {
        HandyInventoryWrapper.initClickEvent(packageName);
        return this;
    }

    public InitApi addMetrics(int serviceId, List<?> charts) {
        return this;
    }

    public InitApi addMetrics(int serviceId) {
        SignService.getInstance().init(serviceId);
        return this;
    }

    public InitApi checkVersion() {
        return this;
    }

    public InitApi enableBc() {
        BcUtil.registerOut();
        return this;
    }

    public InitApi enableSqlBackup() {
        SqlBackupUtil.enableSchedule();
        return this;
    }

    public InitApi enableSql(String packageName) {
        List<Class<?>> tables = ClassUtil.getInstance().getClassByAnnotation(packageName, TableName.class);
        if (!CollUtil.isEmpty(tables)) {
            SqlManagerUtil.getInstance().enableSql(null, packageName);
            ppPPpp(tables);
        }
        return this;
    }

    private void ppPPpp(List<Class<?>> tables) {
        HandySchedulerUtil.runTaskTimerAsynchronously(new TableCreateRunnable(tables), 20L, 20L);
    }

    @Deprecated
    public void initClass(String className, String payload) {
        SignService.init(className, payload);
    }

    public static void disable() {
        PlayerInputApi.disable();
        if (!HandySchedulerUtil.isFolia()) {
            HandyInventoryUtil.closeHandyInventory();
        }
        HandySchedulerUtil.cancelTask();
        SqlBackupUtil.disableSchedule();
        SqlManagerUtil.getInstance().close();
    }
}
