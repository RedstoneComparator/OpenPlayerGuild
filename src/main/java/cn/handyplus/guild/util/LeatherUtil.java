package cn.handyplus.guild.util;

import cn.handyplus.guild.constants.EquipmentSlotEnum;
import cn.handyplus.guild.enter.GuildEquipment;
import cn.handyplus.guild.enter.GuildPlayerItem;
import cn.handyplus.guild.lib.C;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandySchedulerUtil;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.lib.VersionCheckEnum;
import cn.handyplus.guild.param.PlayerItem;
import cn.handyplus.guild.service.GuildEquipmentService;
import cn.handyplus.guild.service.GuildPlayerItemService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherUtil {
    private final static Map<UUID, PlayerItem> playerItems = new ConcurrentHashMap<UUID, PlayerItem>();
    private volatile static List<GuildEquipment> equipmentCache;

    public static void refreshEquipmentCache() {
        equipmentCache = GuildEquipmentService.getInstance().findAll();
    }

    private LeatherUtil() {
    }

    public static void equipment(Player player, Color color) {
        if (ConfigUtil.KIT_PVP_CONFIG.getBoolean("equipment", false) && VersionCheckEnum.getEnum().getVersionId() >= VersionCheckEnum.V_1_9.getVersionId()) {
            LeatherUtil.applyEquipment(player, color, LeatherUtil.getEquipmentCache());
        }
    }

    private static void backupEquipment(Player player, PlayerItem playerItem) {
        HandySchedulerUtil.runTaskAsynchronously(() -> {
            ArrayList<GuildPlayerItem> guildPlayerItems = new ArrayList<GuildPlayerItem>();
            if (playerItem.getStorageContents() != null) {
                for (ItemStack itemStack : playerItem.getStorageContents()) {
                    if (itemStack == null) continue;
                    if (Material.AIR.equals((Object)itemStack.getType())) continue;
                    GuildPlayerItem guildPlayerItem = new GuildPlayerItem();
                    guildPlayerItem.setPlayerName(player.getName());
                    guildPlayerItem.setPlayerUuid(player.getUniqueId());
                    guildPlayerItem.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    guildPlayerItem.setCreateTime(new Date());
                    guildPlayerItems.add(guildPlayerItem);
                }
            }
            if (playerItem.getArmorContents() != null) {
                for (ItemStack itemStack : playerItem.getArmorContents()) {
                    if (itemStack == null) continue;
                    if (Material.AIR.equals((Object)itemStack.getType())) continue;
                    GuildPlayerItem guildPlayerItem = new GuildPlayerItem();
                    guildPlayerItem.setPlayerName(player.getName());
                    guildPlayerItem.setPlayerUuid(player.getUniqueId());
                    guildPlayerItem.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    guildPlayerItem.setCreateTime(new Date());
                    guildPlayerItems.add(guildPlayerItem);
                }
            }
            if (playerItem.getExtraContents() != null) {
                for (ItemStack itemStack : playerItem.getExtraContents()) {
                    if (itemStack == null) continue;
                    if (Material.AIR.equals((Object)itemStack.getType())) continue;
                    GuildPlayerItem guildPlayerItem = new GuildPlayerItem();
                    guildPlayerItem.setPlayerName(player.getName());
                    guildPlayerItem.setPlayerUuid(player.getUniqueId());
                    guildPlayerItem.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
                    guildPlayerItem.setCreateTime(new Date());
                    guildPlayerItems.add(guildPlayerItem);
                }
            }
            if (playerItem.getItemOnCursor() != null && !Material.AIR.equals((Object)playerItem.getItemOnCursor().getType())) {
                GuildPlayerItem guildPlayerItem = new GuildPlayerItem();
                guildPlayerItem.setPlayerName(player.getName());
                guildPlayerItem.setPlayerUuid(player.getUniqueId());
                guildPlayerItem.setItemStack(ItemStackUtil.itemStackSerialize(playerItem.getItemOnCursor()));
                guildPlayerItem.setCreateTime(new Date());
                guildPlayerItems.add(guildPlayerItem);
            }
            int savedCount = GuildPlayerItemService.getInstance().addAll(guildPlayerItems);
            MessageUtil.sendConsoleDebugMessage(new StringBuilder().insert(0, "本次装备 备份数量:").append(savedCount).toString());
        });
    }

    private static List<GuildEquipment> getEquipmentCache() {
        List<GuildEquipment> cachedEquipments = equipmentCache;
        if (cachedEquipments != null) {
            return cachedEquipments;
        }
        Class<LeatherUtil> leatherUtilClass = LeatherUtil.class;
        synchronized (LeatherUtil.class) {
            if (equipmentCache == null) {
                LeatherUtil.refreshEquipmentCache();
            }

            return equipmentCache;
        }
    }

    private static void applyEquipment(Player player, Color color, List<GuildEquipment> guildEquipments) {
        PlayerInventory playerInventory = player.getInventory();
        PlayerItem playerItem = new PlayerItem();
        playerItem.setStorageContents(playerInventory.getStorageContents());
        playerItem.setArmorContents(playerInventory.getArmorContents());
        playerItem.setExtraContents(playerInventory.getExtraContents());
        playerItem.setItemOnCursor(player.getItemOnCursor());
        playerItems.put(player.getUniqueId(), playerItem);
        LeatherUtil.backupEquipment(player, playerItem);
        playerInventory.clear();
        player.setItemOnCursor(new ItemStack(Material.AIR));
        boolean defaultEquipment = ConfigUtil.KIT_PVP_CONFIG.getBoolean("defaultEquipment", true);
        ItemStack itemStack = LeatherUtil.colorLeatherArmor(new ItemStack(Material.LEATHER_HELMET), color);
        ItemStack itemStack2 = defaultEquipment ? LeatherUtil.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), color) : new ItemStack(Material.AIR);
        ItemStack itemStack3 = defaultEquipment ? LeatherUtil.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), color) : new ItemStack(Material.AIR);
        ItemStack itemStack4 = defaultEquipment ? LeatherUtil.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), color) : new ItemStack(Material.AIR);
        ItemStack itemStack5 = new ItemStack(defaultEquipment ? Material.STONE_SWORD : Material.AIR);
        ItemStack itemStack6 = new ItemStack(defaultEquipment ? Material.SHIELD : Material.AIR);
        ArrayList<ItemStack> extraEquipments = new ArrayList<ItemStack>();
        if (CollUtil.isNotEmpty(guildEquipments)) {
            for (GuildEquipment guildEquipment : guildEquipments) {
                ItemStack itemStack7 = ItemStackUtil.itemStackDeserialize(guildEquipment.getItemStack());
                if (StrUtil.isNotEmpty(guildEquipment.getSlot())) {
                    EquipmentSlotEnum equipmentSlotEnum = EquipmentSlotEnum.getType(guildEquipment.getSlot());
                    if (equipmentSlotEnum != null) {
                        switch (C.pPpPpP[equipmentSlotEnum.ordinal()]) {
                            case 1: {
                                itemStack5 = itemStack7;
                                continue;
                            }
                            case 2: {
                                itemStack6 = itemStack7;
                                continue;
                            }
                            case 3: {
                                itemStack2 = itemStack7;
                                continue;
                            }
                            case 4: {
                                itemStack3 = itemStack7;
                                continue;
                            }
                            case 5: {
                                itemStack4 = itemStack7;
                                continue;
                            }
                        }
                    }
                }
                extraEquipments.add(itemStack7);
            }
        }
        EntityEquipment entityEquipment = player.getEquipment();
        if (entityEquipment != null) {
            entityEquipment.setHelmet(itemStack);
            entityEquipment.setChestplate(itemStack2);
            entityEquipment.setLeggings(itemStack3);
            entityEquipment.setBoots(itemStack4);
            entityEquipment.setItemInMainHand(itemStack5);
            entityEquipment.setItemInOffHand(itemStack6);
        }
        if (defaultEquipment) {
            ItemStack[] appleStackArray = new ItemStack[1];
            appleStackArray[0] = new ItemStack(Material.APPLE, 64);
            playerInventory.addItem(appleStackArray);
            ItemStack[] arrowStackArray = new ItemStack[1];
            arrowStackArray[0] = new ItemStack(Material.ARROW, 64);
            playerInventory.addItem(arrowStackArray);
            ItemStack[] stoneAxeStackArray = new ItemStack[1];
            stoneAxeStackArray[0] = new ItemStack(Material.STONE_AXE);
            playerInventory.addItem(stoneAxeStackArray);
            ItemStack[] bowStackArray = new ItemStack[1];
            bowStackArray[0] = new ItemStack(Material.BOW);
            playerInventory.addItem(bowStackArray);
        }
        for (ItemStack itemStack7 : extraEquipments) {
            ItemStack[] equipmentStackArray = new ItemStack[1];
            equipmentStackArray[0] = itemStack7;
            playerInventory.addItem(equipmentStackArray);
        }
    }

    public static void setPlayerItemStack(Player player) {
        boolean equipmentEnabled = ConfigUtil.KIT_PVP_CONFIG.getBoolean("equipment", false);
        if (ConfigUtil.KIT_PVP_CONFIG == null || !equipmentEnabled || VersionCheckEnum.getEnum().getVersionId() < VersionCheckEnum.V_1_9.getVersionId()) {
            return;
        }
        UUID playerUuid = player.getUniqueId();
        PlayerItem playerItem = playerItems.get(playerUuid);
        if (playerItem == null) {
            return;
        }
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();
        player.setItemOnCursor(new ItemStack(Material.AIR));
        ItemStack[] savedContents = playerItem.getStorageContents();
        if (savedContents != null && savedContents.length > 0) {
            playerInventory.setStorageContents(savedContents);
        }
        if ((savedContents = playerItem.getArmorContents()) != null && savedContents.length > 0) {
            playerInventory.setArmorContents(savedContents);
        }
        if ((savedContents = playerItem.getExtraContents()) != null && savedContents.length > 0) {
            playerInventory.setExtraContents(savedContents);
        }
        ItemStack cursorItem = playerItem.getItemOnCursor();
        if (cursorItem != null && !Material.AIR.equals((Object)cursorItem.getType())) {
            player.setItemOnCursor(cursorItem);
        }
        GuildPlayerItemService.getInstance().delByPlayerUuid(player.getUniqueId());
        playerItems.remove(playerUuid);
    }

    private static ItemStack colorLeatherArmor(ItemStack leatherItem, Color color) {
        ItemMeta itemMeta = ItemStackUtil.getItemMeta(leatherItem);
        if (!(itemMeta instanceof LeatherArmorMeta)) {
            return new ItemStack(Material.AIR);
        }
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)itemMeta;
        ItemStack itemStack = leatherItem;
        leatherArmorMeta.setColor(color);
        itemStack.setItemMeta((ItemMeta)leatherArmorMeta);
        return itemStack;
    }
}
