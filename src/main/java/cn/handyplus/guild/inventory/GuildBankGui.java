package cn.handyplus.guild.inventory;

import cn.handyplus.guild.constants.GuiTypeEnum;
import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.hook.PlaceholderApiUtil;
import cn.handyplus.guild.lib.HandyInventory;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuildBankGui {
    private final static GuildBankGui INSTANCE = new GuildBankGui();

    private void setButtons(HandyInventory inventory) {
        Player player = inventory.getPlayer();
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuidAndGuildInfo(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        HandyInventoryUtil.setButton(ConfigUtil.BANK_CONFIG, inventory, "info", MapUtil.of("money", guildPlayer.getGuildInfoMoney().toString()));
        HandyInventoryUtil.setButton(ConfigUtil.BANK_CONFIG, inventory, "small", this.getPriceMap(guildPlayer, "small"));
        HandyInventoryUtil.setButton(ConfigUtil.BANK_CONFIG, inventory, "big", this.getPriceMap(guildPlayer, "big"));
        HandyInventoryUtil.setButton(ConfigUtil.BANK_CONFIG, inventory, "all", this.getPriceMap(guildPlayer, "all"));
        HandyInventoryUtil.setButton(ConfigUtil.BANK_CONFIG, inventory, "custom", this.getPriceMap(guildPlayer, "custom"));
        HandyInventoryUtil.setButton(ConfigUtil.BANK_CONFIG, inventory, "back");
        HandyInventoryUtil.setCustomButton(ConfigUtil.BANK_CONFIG, inventory, "bankCustom");
    }

    private GuildBankGui() {
    }

    private Map<String, String> getMoneyPlaceholders(GuildInfo guildInfo) {
        return MapUtil.of("money", guildInfo.getMoney().toString());
    }

    public void setInventoryDate(HandyInventory inventory) {
        HandyInventory handyInventory = inventory;
        handyInventory.setGuiType(GuiTypeEnum.GUILD_BANK.getType());
        this.setButtons(handyInventory);
    }

    private Map<String, String> getPriceMap(GuildPlayer guildPlayer, String priceKey) {
        HashMap<String, String> placeholders = MapUtil.newHashMapWithExpectedSize(3);
        String yesButton = ConfigUtil.BANK_CONFIG.getString("yesButton");
        String button = ConfigUtil.BANK_CONFIG.getString("noButton");
        int price = ConfigUtil.BANK_CONFIG.getInt(new StringBuilder().insert(0, priceKey).append(".price").toString(), 1);
        int money = guildPlayer.getMoney();
        if (money >= price) {
            button = yesButton;
        }
        HashMap<String, String> resultMap = placeholders;
        resultMap.put("price", String.valueOf(price));
        placeholders.put("money", Integer.toString(money));
        placeholders.put("button", button);
        return resultMap;
    }

    public static GuildBankGui getInstance() {
        return INSTANCE;
    }

    public Inventory createGui(Player player, Integer guildId) {
        Object title = ConfigUtil.BANK_CONFIG.getString("title");
        String sound = ConfigUtil.BANK_CONFIG.getString("sound");
        title = PlaceholderApiUtil.set(player, (String)title);
        int size = ConfigUtil.BANK_CONFIG.getInt("size", 27);
        Object handyInventory = title = new HandyInventory(GuiTypeEnum.GUILD_BANK.getType(), (String)title, size, sound);
        ((HandyInventory)title).setPlayer(player);
        ((HandyInventory)handyInventory).setId(guildId);
        this.setInventoryDate((HandyInventory)handyInventory);
        return ((HandyInventory)handyInventory).getInventory();
    }
}
