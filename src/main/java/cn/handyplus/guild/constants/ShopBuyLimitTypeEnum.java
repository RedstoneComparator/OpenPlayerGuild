package cn.handyplus.guild.constants;

import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public enum ShopBuyLimitTypeEnum {
    DAY("day", "day"),
    WEEK("week", "week"),
    MONTH("month", "month"),
    ONLY("only", "only");

    private final String langKey;
    private final String typeValue;

    @Generated
    ShopBuyLimitTypeEnum(String typeName, String configKey) {
        this.typeValue = typeName;
        this.langKey = configKey;
    }

    public static ShopBuyLimitTypeEnum getEnumDefaultOnly(String typeName) {
        for (ShopBuyLimitTypeEnum shopBuyLimitTypeEnum : values()) {
            if (shopBuyLimitTypeEnum.getName().equalsIgnoreCase(typeName)) {
                return shopBuyLimitTypeEnum;
            }
        }
        return ONLY;
    }

    public static String getTypeName(ShopBuyLimitTypeEnum limitType) {
        return ConfigUtil.SHOP_CONFIG.getString(limitType.langKey);
    }

    @Generated
    public String getName() {
        return this.typeValue;
    }

    public static ShopBuyLimitTypeEnum getEnum(String typeName) {
        for (ShopBuyLimitTypeEnum shopBuyLimitTypeEnum : values()) {
            if (shopBuyLimitTypeEnum.getName().equalsIgnoreCase(typeName)) {
                return shopBuyLimitTypeEnum;
            }
        }
        return null;
    }

    public static List<String> getShopBuyLimitTypeList() {
        ArrayList<String> typeNames = new ArrayList<String>();
        for (ShopBuyLimitTypeEnum shopBuyLimitTypeEnum : values()) {
            typeNames.add(shopBuyLimitTypeEnum.getName());
        }
        return typeNames;
    }

    @NotNull
    public static String getEnumThrow(@NotNull String typeName) {
        ShopBuyLimitTypeEnum shopBuyLimitTypeEnum = ShopBuyLimitTypeEnum.getEnum(typeName);
        AssertUtil.notNull((Object)shopBuyLimitTypeEnum,
                BaseUtil.getLangMsg("typeFailureMsg") + " " + typeName);
        return shopBuyLimitTypeEnum.getName();
    }
}
