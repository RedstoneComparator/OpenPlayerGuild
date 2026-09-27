package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildLogoShopService;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class SetDefaultLogoCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextLang("tabHelp.level");
    }

    @Override
    public String command() {
        return "setDefaultLogo";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        ItemStack itemStack = ItemStackUtil.getItemInMainHand(AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg")).getInventory());
        AssertUtil.notTrue(Material.AIR.equals((Object)itemStack.getType()), BaseUtil.getLangMsg("noAir"));
        itemStack.setAmount(1);
        GuildLogoShop guildLogoShop = new GuildLogoShop();
        guildLogoShop.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
        guildLogoShop.setPrice(0);
        int level = 1;
        if (args.length > 1) {
            level = AssertUtil.isPositiveToInt(args[1], BaseUtil.getLangMsg("amountFailureMsg"));
        }
        guildLogoShop.setLevel(level);
        guildLogoShop.setDefaultLogoFlag(true);
        int result = GuildLogoShopService.getInstance().add(guildLogoShop);
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg(result > 0 ? "succeedMsg" : "failureMsg"));
    }

    @Override
    public String permission() {
        return "playerGuild.setDefaultLogo";
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
