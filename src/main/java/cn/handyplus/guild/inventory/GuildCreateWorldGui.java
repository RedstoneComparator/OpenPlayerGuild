package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.hook.PlayerCurrencyUtil;
import cn.handyplus.guild.hook.PlayerPointsUtil;
import cn.handyplus.guild.hook.VaultUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.NumberUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuildCreateWorldGui {
    private final static GuildCreateWorldGui INSTANCE = new GuildCreateWorldGui();

    private Map<String, String> getPriceMap(HandyInventory inventory, String priceKey) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        Player player = inventory.getPlayer();
        Optional<GuildInfo> guildInfoOptional = GuildInfoService.getInstance().findById(inventory.getId());
        if (!guildInfoOptional.isPresent()) {
            return MapUtil.of();
        }
        GuildInfo guildInfo = guildInfoOptional.get();
        String yesButton = ConfigUtil.CREATE_WORLD_CONFIG.getString("yesButton");
        String button = ConfigUtil.CREATE_WORLD_CONFIG.getString("noButton");
        Number price = ConfigUtil.CREATE_WORLD_CONFIG.getLong(new StringBuilder().insert(0, priceKey).append(".price").toString());
        Number balance = 0;
        if ("point".equals(priceKey)) {
            int pointPrice = ConfigUtil.CREATE_WORLD_CONFIG.getInt(new StringBuilder().insert(0, priceKey).append(".price").toString());
            int points = PlayerPointsUtil.look(player);
            price = pointPrice;
            balance = points;
            if (points >= pointPrice) {
                button = yesButton;
            }
        }
        if ("vault".equals(priceKey)) {
            double vaultPrice = ConfigUtil.CREATE_WORLD_CONFIG.getDouble(new StringBuilder().insert(0, priceKey).append(".price").toString());
            double balanceDouble = VaultUtil.look(player);
            price = vaultPrice;
            balance = balanceDouble;
            if (NumberUtil.ge(NumberUtil.toBigDecimal(balanceDouble), NumberUtil.toBigDecimal(vaultPrice))) {
                button = yesButton;
            }
        }
        if ("money".equals(priceKey) && (balance = guildInfo.getMoney()).longValue() >= price.longValue()) {
            button = yesButton;
        }
        if ("ply".equals(priceKey)) {
            String currencyType = ConfigUtil.CREATE_WORLD_CONFIG.getString("ply.type");
            Long currencyBalance = PlayerCurrencyUtil.look(player, currencyType);
            balance = currencyBalance;
            if (currencyBalance >= price.longValue()) {
                button = yesButton;
            }
            placeholders.put("type", PlayerCurrencyUtil.getDesc(currencyType));
        }
        HashMap<String, String> resultMap = placeholders;
        resultMap.put("price", NumberUtil.format(price));
        placeholders.put("money", NumberUtil.format(balance));
        placeholders.put("button", button);
        return resultMap;
    }

    private void J(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.GUILD_WORLD_CREATE.getType());
        this.setButtons(handyInventory);
    }

    private void setButtons(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_WORLD_CONFIG, handyInventory, "point", this.getPriceMap(handyInventory, "point"));
        HandyInventory handyInventory2 = inventory;
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_WORLD_CONFIG, handyInventory2, "money", this.getPriceMap(handyInventory2, "money"));
        HandyInventory handyInventory3 = inventory;
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_WORLD_CONFIG, handyInventory3, "vault", this.getPriceMap(handyInventory3, "vault"));
        HandyInventory handyInventory4 = inventory;
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_WORLD_CONFIG, handyInventory4, "ply", this.getPriceMap(handyInventory4, "ply"));
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_WORLD_CONFIG, inventory, "back");
        HandyInventoryUtil.setButton(ConfigUtil.CREATE_WORLD_CONFIG, inventory, "pane");
    }

    private GuildCreateWorldGui() {
    }

    public static GuildCreateWorldGui getInstance() {
        return INSTANCE;
    }

    public Inventory createGui(Player player, Integer guildId) {
        Object title = ConfigUtil.CREATE_WORLD_CONFIG.getString("title");
        String sound = ConfigUtil.CREATE_WORLD_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, (String)title);
        int size = ConfigUtil.CREATE_WORLD_CONFIG.getInt("size", 27);
        Object handyInventory = title = new HandyInventory(GuiTypeEnum.GUILD_WORLD_CREATE.getType(), (String)title, size, sound);
        ((HandyInventory)title).setPlayer(player);
        ((HandyInventory)handyInventory).setId(guildId);
        this.J((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }
}
