package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.constants.ShopBuyLimitTypeEnum;
import cn.handyplus.guild.enter.GuildShop;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildShopService;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddShopCommand
implements IHandyCommandEvent {
    @Override
    public String command() {
        return "addShop";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextLang("tabHelp.price").nextLang("tabHelp.level").nextLang("tabHelp.limit").next(ShopBuyLimitTypeEnum.getShopBuyLimitTypeList());
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        Integer price = AssertUtil.isPositiveToInt(args[1], BaseUtil.getLangMsg("amountFailureMsg"));
        ItemStack itemStack = ItemStackUtil.getItemInMainHand(player.getInventory());
        AssertUtil.notTrue(Material.AIR.equals((Object)itemStack.getType()), BaseUtil.getLangMsg("noAir"));
        Integer level = this.getArg(args, 2).map(levelValue -> AssertUtil.isPositiveToInt(levelValue, BaseUtil.getLangMsg("amountFailureMsg"))).orElse(1);
        Integer number = this.getArg(args, 3).map(numberValue -> AssertUtil.isPositiveToInt(numberValue, BaseUtil.getLangMsg("amountFailureMsg"))).orElse(0);
        String type = this.getArg(args, 4).map(ShopBuyLimitTypeEnum::getEnumThrow).orElse(ShopBuyLimitTypeEnum.ONLY.getName());
        GuildShop guildShop = new GuildShop();
        guildShop.setPrice(price);
        guildShop.setLevel(level);
        guildShop.setNumber(number);
        guildShop.setType(type);
        guildShop.setGuildInfoId(0);
        guildShop.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg(GuildShopService.getInstance().add(guildShop) > 0 ? "succeedMsg" : "failureMsg"));
    }

    @Override
    public String permission() {
        return "playerGuild.addShop";
    }
}
