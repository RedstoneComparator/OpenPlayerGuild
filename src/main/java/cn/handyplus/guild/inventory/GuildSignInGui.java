package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildPlayerSignIn;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.hook.PlayerCurrencyUtil;
import cn.handyplus.guild.hook.PlayerPointsUtil;
import cn.handyplus.guild.hook.VaultUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.HandyPermissionUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.NumberUtil;
import cn.handyplus.guild.service.GuildPlayerSignInService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuildSignInGui {
    private final static GuildSignInGui INSTANCE = new GuildSignInGui();

    private GuildSignInGui() {
    }

    private Map<String, String> buildSignInPlaceholders(Player player, String type, GuildPlayerSignIn signIn) {
        int amount;
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        String yesButton = ConfigUtil.SIGN_IN_CONFIG.getString("yesButton");
        String noButton = ConfigUtil.SIGN_IN_CONFIG.getString("noButton");
        String signedInButton = ConfigUtil.SIGN_IN_CONFIG.getString("signedInButton");
        Number price = ConfigUtil.SIGN_IN_CONFIG.getLong(new StringBuilder().insert(0, type).append(".price").toString());
        if ("point".equals(type)) {
            amount = ConfigUtil.SIGN_IN_CONFIG.getInt(new StringBuilder().insert(0, type).append(".price").toString());
            int points = PlayerPointsUtil.look(player);
            price = amount;
            if (points >= amount) {
                noButton = yesButton;
            }
        }
        if ("vault".equals(type)) {
            double vaultPrice = ConfigUtil.SIGN_IN_CONFIG.getDouble(new StringBuilder().insert(0, type).append(".price").toString());
            double vaultBalance = VaultUtil.look(player);
            price = vaultPrice;
            if (NumberUtil.ge(NumberUtil.toBigDecimal(vaultBalance), NumberUtil.toBigDecimal(vaultPrice))) {
                noButton = yesButton;
            }
        }
        if ("ply".equals(type)) {
            String currencyType = ConfigUtil.SIGN_IN_CONFIG.getString("ply.type");
            Long currencyBalance = PlayerCurrencyUtil.look(player, currencyType);
            if (currencyBalance >= price.longValue()) {
                noButton = yesButton;
            }
            placeholders.put("type", PlayerCurrencyUtil.getDesc(currencyType));
        }
        if (signIn != null) {
            noButton = signedInButton;
        }
        amount = ConfigUtil.SIGN_IN_CONFIG.getInt(new StringBuilder().insert(0, type).append(".money").toString());
        Double discountMultiplier = HandyPermissionUtil.getDoubleNumber(player, ConfigUtil.SIGN_IN_CONFIG, "signIn");
        int discountedMoney = (int)((double)amount * discountMultiplier);
        String moneyText = String.valueOf(amount);
        if (amount != discountedMoney) {
            moneyText = new StringBuilder().insert(0, " &m").append(amount).append("&r&a ").append(discountedMoney).toString();
        }
        HashMap<String, String> placeholdersRef = placeholders;
        placeholdersRef.put("money", moneyText);
        placeholders.put("price", NumberUtil.format(price));
        placeholders.put("button", noButton);
        return placeholdersRef;
    }

    public Inventory createGui(Player player, Integer guildId) {
        Object guiValue = ConfigUtil.SIGN_IN_CONFIG.getString("title");
        String sound = ConfigUtil.SIGN_IN_CONFIG.getString("sound");
        guiValue = PlaceholderApiUtil.set(player, (String)guiValue);
        int size = ConfigUtil.SIGN_IN_CONFIG.getInt("size", 27);
        Object handyInventory = guiValue = new HandyInventory(GuiTypeEnum.GUILD_SIGN_IN.getType(), (String)guiValue, size, sound);
        ((HandyInventory)guiValue).setPlayer(player);
        ((HandyInventory)handyInventory).setId(guildId);
        this.setInventoryDate((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    public void setInventoryDate(HandyInventory gui) {
        HandyInventory inventory = gui;
        inventory.setGuiType(GuiTypeEnum.GUILD_SIGN_IN.getType());
        HandyInventoryUtil.refreshInventory(inventory.getInventory());
        this.fillGui(inventory);
    }

    private void fillGui(HandyInventory gui) {
        Player player = gui.getPlayer();
        Optional<GuildPlayerSignIn> optional = GuildPlayerSignInService.getInstance().findByPlayerUuid(player.getUniqueId());
        HandyInventoryUtil.setButton(ConfigUtil.SIGN_IN_CONFIG, gui, "point", this.buildSignInPlaceholders(player, "point", optional.orElse(null)));
        HandyInventoryUtil.setButton(ConfigUtil.SIGN_IN_CONFIG, gui, "vault", this.buildSignInPlaceholders(player, "vault", optional.orElse(null)));
        HandyInventoryUtil.setButton(ConfigUtil.SIGN_IN_CONFIG, gui, "ply", this.buildSignInPlaceholders(player, "ply", optional.orElse(null)));
        HandyInventoryUtil.setButton(ConfigUtil.SIGN_IN_CONFIG, gui, "back");
        HandyInventoryUtil.setCustomButton(ConfigUtil.SIGN_IN_CONFIG, gui, "custom");
    }

    public static GuildSignInGui getInstance() {
        return INSTANCE;
    }
}
