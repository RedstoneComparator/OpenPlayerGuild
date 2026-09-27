package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.hook.PlayerCurrencyUtil;
import cn.handyplus.guild.hook.PlayerPointsUtil;
import cn.handyplus.guild.hook.VaultUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.NumberUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuildCreateGui {
    private final static GuildCreateGui INSTANCE = new GuildCreateGui();

    private void J(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.GUILD_CREATE.getType());
        this.setButtons(handyInventory);
    }

    public static GuildCreateGui getInstance() {
        return INSTANCE;
    }

    public Inventory createGui(Player player, String guildName) {
        Object title = ConfigUtil.CREATE_CONFIG.getString("title");
        String sound = ConfigUtil.CREATE_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, (String)title);
        int size = ConfigUtil.CREATE_CONFIG.getInt("size", 27);
        Object handyInventory = title = new HandyInventory(GuiTypeEnum.GUILD_CREATE.getType(), (String)title, size, sound);
        ((HandyInventory)title).setPlayer(player);
        ((HandyInventory)handyInventory).setSearchType(guildName);
        this.J((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }

    private void setButtons(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        Player player = handyInventory.getPlayer();
        String guildName = handyInventory.getSearchType();
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_CONFIG, inventory, "point", this.getPriceMap(player, "point", guildName));
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_CONFIG, inventory, "vault", this.getPriceMap(player, "vault", guildName));
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_CONFIG, inventory, "ply", this.getPriceMap(player, "ply", guildName));
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_CONFIG, inventory, "back");
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_CONFIG, inventory, "pane");
    }

    private GuildCreateGui() {
    }

    private Map<String, String> getPriceMap(Player player, String priceKey, String guildName) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(4);
        String yesButton = ConfigUtil.CREATE_CONFIG.getString("yesButton");
        String button = ConfigUtil.CREATE_CONFIG.getString("noButton");
        Number price = ConfigUtil.CREATE_CONFIG.getLong(new StringBuilder().insert(0, priceKey).append(".price").toString());
        Number balance = 0;
        if ("point".equals(priceKey)) {
            int pointPrice = ConfigUtil.CREATE_CONFIG.getInt(new StringBuilder().insert(0, priceKey).append(".price").toString());
            int points = PlayerPointsUtil.look(player);
            price = pointPrice;
            balance = points;
            if (points >= pointPrice) {
                button = yesButton;
            }
        }
        if ("vault".equals(priceKey)) {
            double vaultPrice = ConfigUtil.CREATE_CONFIG.getDouble(new StringBuilder().insert(0, priceKey).append(".price").toString());
            double balanceDouble = VaultUtil.look(player);
            price = vaultPrice;
            balance = balanceDouble;
            if (NumberUtil.ge(NumberUtil.toBigDecimal(balanceDouble), NumberUtil.toBigDecimal(vaultPrice))) {
                button = yesButton;
            }
        }
        if ("ply".equals(priceKey)) {
            String currencyType = ConfigUtil.CREATE_CONFIG.getString("ply.type");
            Long currencyBalance = PlayerCurrencyUtil.look(player, currencyType);
            balance = currencyBalance;
            if (currencyBalance >= price.longValue()) {
                button = yesButton;
            }
            placeholders.put("type", PlayerCurrencyUtil.getDesc(currencyType));
        }
        HashMap<String, String> resultMap = placeholders;
        resultMap.put("price", NumberUtil.format(price));
        placeholders.put("guild_name", guildName);
        placeholders.put("money", NumberUtil.format(balance));
        placeholders.put("button", button);
        return resultMap;
    }
}
