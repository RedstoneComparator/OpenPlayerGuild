package cn.handyplus.guild.service;

import cn.handyplus.guild.enter.GuildEquipment;
import cn.handyplus.guild.lib.Db;
import cn.handyplus.guild.lib.DbFunction;
import cn.handyplus.guild.lib.HandyInventoryUtil;
import cn.handyplus.guild.lib.ItemMetaUtil;
import cn.handyplus.guild.lib.Page;
import cn.handyplus.guild.util.LeatherUtil;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.Optional;

public class GuildEquipmentService {

    public List<GuildEquipment> findAll() {
        return Db.use(GuildEquipment.class).execution().list();
    }

    public Optional<GuildEquipment> findById(Integer id) {
        return Db.use(GuildEquipment.class).execution().selectById(id);
    }

    public static String pPPppP(String input) {
        int length = input.length();
        int index = length - 1;
        char[] chars = new char[length];
        int lowKey = 54;
        int cursor = index;
        int highKey = 100;
        while (cursor >= 0) {
            int highPos = index--;
            chars[highPos] = (char)(input.charAt(highPos) ^ highKey);
            if (index < 0) break;
            int lowPos = index--;
            chars[lowPos] = (char)(input.charAt(lowPos) ^ lowKey);
            cursor = index;
        }
        return new String(chars);
    }

    private GuildEquipmentService() {
    }

    public int add(GuildEquipment equipment) {
        int result = Db.use(GuildEquipment.class).execution().insert(equipment);
        if (result > 0) {
            LeatherUtil.refreshEquipmentCache();
        }
        return result;
    }

    public Page<GuildEquipment> page(Integer pageNum, Integer pageSize) {
        Db<GuildEquipment> db = Db.use(GuildEquipment.class);
        db.where().limit(pageNum, pageSize).orderByDesc(GuildEquipment::getId);
        return db.execution().page();
    }

    public void delById(Integer id) {
        Db.use(GuildEquipment.class).execution().deleteById(id);
        LeatherUtil.refreshEquipmentCache();
    }

    public static GuildEquipmentService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final GuildEquipmentService INSTANCE = new GuildEquipmentService();
    }
}
