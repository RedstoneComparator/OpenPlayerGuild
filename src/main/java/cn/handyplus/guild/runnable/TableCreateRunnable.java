package cn.handyplus.guild.runnable;

import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.HandyRunnable;
import cn.handyplus.guild.lib.LockUtil;
import cn.handyplus.guild.lib.SignService;
import cn.handyplus.guild.lib.TableName;
import java.util.List;

public final class TableCreateRunnable extends HandyRunnable {
    private static final String LOCK_NAME = "createTableRunnable";

    private final List<Class<?>> tables;

    public TableCreateRunnable(List<Class<?>> tables) {
        this.tables = tables;
    }

    @Override
    public void run() {
        if (!LockUtil.tryPass(LOCK_NAME)) {
            return;
        }
        try {
            for (Class<?> table : this.tables) {
                TableName annotation = table.getAnnotation(TableName.class);
                if (annotation == null || !annotation.create()) {
                    continue;
                }
                Db.use(table).createTable();
            }
            SignService.getInstance().setDbResult(true);
            cancel();
        } finally {
            LockUtil.done(LOCK_NAME);
        }
    }
}
