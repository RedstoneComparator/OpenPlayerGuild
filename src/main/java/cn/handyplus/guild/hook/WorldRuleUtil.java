package cn.handyplus.guild.hook;

import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.LegacyUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldRuleUtil {
    private WorldRuleUtil() {
    }

    public static void setGameRule(String worldName) {
        List<String> falseFlags;
        if (StrUtil.isEmpty(worldName)) {
            return;
        }
        World world = Bukkit.getWorld((String)worldName);
        if (world == null) {
            return;
        }
        List<String> trueFlags = ConfigUtil.WORLD_CONFIG.getStringList("gameRule.trueFlag");
        if (CollUtil.isNotEmpty(trueFlags)) {
            for (String flag : trueFlags) {
                if (!StrUtil.isNotEmpty(flag)) continue;
                LegacyUtil.setGameRuleValue(world, flag, true);
            }
        }
        if (CollUtil.isNotEmpty(falseFlags = ConfigUtil.WORLD_CONFIG.getStringList("gameRule.falseFlag"))) {
            for (String falseFlag : falseFlags) {
                if (!StrUtil.isNotEmpty(falseFlag)) continue;
                LegacyUtil.setGameRuleValue(world, falseFlag, false);
            }
        }
    }
}
