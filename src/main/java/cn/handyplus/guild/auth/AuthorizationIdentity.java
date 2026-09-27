package cn.handyplus.guild.auth;

public final class AuthorizationIdentity {
    public static final String PRODUCT_NAME = "PlayerGuild";
    public static final String PRODUCT_VERSION = "2.21.3";

    private AuthorizationIdentity() {
    }

    public static String pluginName() {
        return PRODUCT_NAME;
    }

    public static String pluginVersion() {
        return PRODUCT_VERSION;
    }
}
