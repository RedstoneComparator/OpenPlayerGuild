package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.enter.GuildPlayerItem;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.CollUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildPlayerItemService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RestoreCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextNull();
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String command() {
        return "restore";
    }

    @Override
    public String permission() {
        return "playerGuild.restore";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(((String[])args).length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        sender = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(args[1].trim());
        List<GuildPlayerItem> playerItems = GuildPlayerItemService.getInstance().findByPlayerUuid(offlinePlayer.getUniqueId());
        if (CollUtil.isEmpty(playerItems)) {
            MessageUtil.sendMessage((Player)sender, ConfigUtil.KIT_PVP_CONFIG.getString("noItem"));
            return;
        }
        for (GuildPlayerItem guildPlayerItem : playerItems) {
            ItemStack itemStack = ItemStackUtil.itemStackDeserialize(guildPlayerItem.getItemStack());
            ItemStackUtil.addItem((Player)sender, itemStack, itemStack.getAmount());
        }
        GuildPlayerItemService.getInstance().delByPlayerUuid(offlinePlayer.getUniqueId());
        MessageUtil.sendMessage((Player)sender, ConfigUtil.KIT_PVP_CONFIG.getString("restore"));
    }
}
