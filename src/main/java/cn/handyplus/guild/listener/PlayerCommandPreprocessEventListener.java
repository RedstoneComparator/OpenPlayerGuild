package cn.handyplus.guild.listener;

import cn.handyplus.guild.api.PlayerGuildApi;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyListener;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@HandyListener
public class PlayerCommandPreprocessEventListener
implements Listener {
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!ConfigUtil.KIT_PVP_CONFIG.getBoolean("disabledCommand", true)) {
            return;
        }
        if (event.getPlayer().isOp()) {
            return;
        }
        String message = event.getMessage();
        message = message.replace("/", "");
        if (message.isEmpty()) {
            return;
        }
        String command = message.split(" ")[0];
        List<String> whiteCommands = ConfigUtil.KIT_PVP_CONFIG.getStringList("whiteCommand");
        if (CollUtil.isNotEmpty(whiteCommands)) {
            for (String whiteCommand : whiteCommands) {
                if (StrUtil.isEmpty(whiteCommand)) {
                    continue;
                }
                if (message.equalsIgnoreCase(whiteCommand)) {
                    return;
                }
                if (command.equalsIgnoreCase(whiteCommand)) {
                    return;
                }
            }
        }
        if (!PlayerGuildApi.isPvp(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
        MessageUtil.sendMessage(event.getPlayer(), ConfigUtil.KIT_PVP_CONFIG.getString("disabledCommandMsg"));
    }
}
