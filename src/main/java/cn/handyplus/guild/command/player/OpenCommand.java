package cn.handyplus.guild.command.player;

import cn.handyplus.guild.constants.GuildSortEnum;
import cn.handyplus.guild.inventory.GuildOpenGui;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OpenCommand
implements IHandyCommandEvent {
    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String command() {
        return "open";
    }

    @Override
    public String permission() {
        return "playerGuild.open";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String sortName = BaseConstants.CONFIG.getString("openDefaultSort", GuildSortEnum.PROSPERITY_DEGREE.getName());
        if (args.length > 1) {
            sortName = args[1];
        }
        Inventory inventory = GuildOpenGui.getInstance().createGui(player, sortName);
        PlayerSchedulerUtil.openInventory(player, inventory);
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(GuildSortEnum.getSortList());
    }
}
