package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.constants.EquipmentSlotEnum;
import cn.handyplus.guild.enter.GuildEquipment;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildEquipmentService;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class SetEquipmentCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(EquipmentSlotEnum.getEquipmentSlotList());
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String command() {
        return "setEquipment";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        ItemStack itemStack = ItemStackUtil.getItemInMainHand(AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg")).getInventory());
        AssertUtil.notTrue(Material.AIR.equals(itemStack.getType()), BaseUtil.getLangMsg("noAir"));
        GuildEquipment guildEquipment = new GuildEquipment();
        if (args.length > 1) {
            EquipmentSlotEnum equipmentSlotEnum = EquipmentSlotEnum.getType(args[1]);
            if (equipmentSlotEnum != null) {
                guildEquipment.setSlot(equipmentSlotEnum.getType());
            }
        }
        guildEquipment.setItemStack(ItemStackUtil.itemStackSerialize(itemStack));
        int result = GuildEquipmentService.getInstance().add(guildEquipment);
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg(result > 0 ? "succeedMsg" : "failureMsg"));
    }

    @Override
    public String permission() {
        return "playerGuild.setEquipment";
    }
}
