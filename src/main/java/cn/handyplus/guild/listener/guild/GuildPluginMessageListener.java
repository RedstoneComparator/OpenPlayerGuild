package cn.handyplus.guild.listener.guild;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BcUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.StrUtil;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class GuildPluginMessageListener
implements PluginMessageListener {
    public void onPluginMessageReceived(@NotNull String channelName, @NotNull Player player, byte[] message) {
        String serverName = BaseConstants.CONFIG.getString("serverName");
        if (StrUtil.isEmpty(serverName)) {
            return;
        }
        MessageUtil.sendConsoleDebugMessage(new StringBuilder().insert(0, "子服:").append(serverName).append("收到消息").toString());
        Optional<BcUtil.BcMessageParam> messageParam = BcUtil.getParamByForward(message);
        if (!messageParam.isPresent()) {
            return;
        }
        MessageUtil.sendAllMessage(messageParam.get().getMessage());
    }

    public GuildPluginMessageListener() {
        Bukkit.getMessenger().registerIncomingPluginChannel((Plugin)PlayerGuild.INSTANCE, "BungeeCord", (PluginMessageListener)this);
    }
}
