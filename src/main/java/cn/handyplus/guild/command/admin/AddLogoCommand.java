package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.enter.GuildLogoShop;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildLogoShopService;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddLogoCommand
implements IHandyCommandEvent {
    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        int result;
        AssertUtil.notTrue(((String[])args).length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        Integer price = AssertUtil.isPositiveToInt(args[1], BaseUtil.getLangMsg("amountFailureMsg"));
        ItemStack itemStack = ItemStackUtil.getItemInMainHand(player.getInventory());
        AssertUtil.notTrue(Material.AIR.equals((Object)itemStack.getType()), BaseUtil.getLangMsg("noAir"));
        Integer level = this.getArg(args, 2).map(levelValue -> AssertUtil.isPositiveToInt(levelValue, BaseUtil.getLangMsg("amountFailureMsg"))).orElse(1);
        itemStack.setAmount(1);
        if (((String[])args).length > 3) {
            Integer id = AssertUtil.isPositiveToInt(args[3], BaseUtil.getLangMsg("amountFailureMsg"));
            Optional<GuildLogoShop> optional = GuildLogoShopService.getInstance().findById(id);
            AssertUtil.notTrue(!optional.isPresent(), BaseUtil.getLangMsg("failureMsg"));
            GuildLogoShop guildLogoShop = optional.get();
            String serializedItem = ItemStackUtil.itemStackSerialize(itemStack);
            guildLogoShop.setItemStack(serializedItem);
            guildLogoShop.setPrice(price);
            guildLogoShop.setLevel(level);
            result = GuildLogoShopService.getInstance().update(guildLogoShop);
            GuildLogoService.getInstance().updateLogoNameByLogoId(guildLogoShop.getId(), serializedItem);
            GuildInfoService.getInstance().updateLogoByLogoId(guildLogoShop.getId(), serializedItem);
        } else {
            GuildLogoShop guildLogoShop = new GuildLogoShop();
            guildLogoShop.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
            guildLogoShop.setPrice(price);
            guildLogoShop.setLevel(level);
            guildLogoShop.setDefaultLogoFlag(false);
            result = GuildLogoShopService.getInstance().add(guildLogoShop);
        }
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg(result > 0 ? "succeedMsg" : "failureMsg"));
    }

    @Override
    public String permission() {
        return "playerGuild.addLogo";
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextLang("tabHelp.price").nextLang("tabHelp.level").nextLang("tabHelp.logoId");
    }

    @Override
    public String command() {
        return "addLogo";
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
