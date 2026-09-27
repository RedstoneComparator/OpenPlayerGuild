package cn.handyplus.guild.command;

import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyCommand;
import cn.handyplus.guild.lib.HandyCommandWrapper;
import cn.handyplus.guild.lib.MessageUtil;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

@HandyCommand(name="playerGuild")
public class PlayerGuildCommand
implements TabExecutor {
    private final static String RELOAD_PERMISSION = "playerGuild.reload";

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            this.sendHelpMessages(sender);
            return true;
        }
        if (!HandyCommandWrapper.onCommand(sender, command, label, args, BaseUtil.getLangMsg("noPermission"))) {
            this.sendHelpMessages(sender);
        }
        return true;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return HandyCommandWrapper.onTabComplete(sender, command, label, args);
    }

    private void sendHelpMessages(CommandSender sender) {
        Iterator helpIterator;
        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            return;
        }
        Iterator helpIteratorCursor = helpIterator = BaseConstants.LANG_CONFIG.getStringList("helps").iterator();
        while (helpIteratorCursor.hasNext()) {
            String helpLine = (String)helpIterator.next();
            helpIteratorCursor = helpIterator;
            MessageUtil.sendMessage(sender, helpLine);
        }
    }
}
