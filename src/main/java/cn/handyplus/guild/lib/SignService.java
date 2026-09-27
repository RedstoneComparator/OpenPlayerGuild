package cn.handyplus.guild.lib;

import org.bukkit.entity.Player;

public interface SignService {

    @Deprecated
    static void init(String className, String payload) {
    }

    boolean isInit();

    boolean isSuccess();

    void init(int serviceId);

    void init();

    void noticeOp(Player player);

    void setDbResult(Boolean dbInit);

    String get();

    void set(String url);

    static SignService getInstance() {
        return Holder.INSTANCE;
    }

    boolean isFailure(String command);

    final class Holder {
        static final SignService INSTANCE = new SignServiceImpl();

        private Holder() {
        }
    }
}
