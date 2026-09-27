package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.inventory.ViewEquipmentGui;
import cn.handyplus.guild.inventory.ViewLocationGui;
import cn.handyplus.guild.inventory.ViewLogoShopGui;
import cn.handyplus.guild.inventory.ViewNoticeGui;
import cn.handyplus.guild.inventory.ViewShopGui;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import java.util.Arrays;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ViewCommand
implements IHandyCommandEvent {

    @Override
    public void onCommand(CommandSender sender, Command command, String subCommand, String[] args) {
        Inventory inventory;
        int typeIndex;
        block15: {
            AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
            sender = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
            subCommand = args[1];
            switch (subCommand.hashCode()) {
                case 1901043637: {
                    if (!subCommand.equals("location")) break;
                    typeIndex = 0;
                    break block15;
                }
                case 3529462: {
                    if (!subCommand.equals("shop")) break;
                    typeIndex = 1;
                    break block15;
                }
                case 3327403: {
                    if (!subCommand.equals("logo")) break;
                    typeIndex = 2;
                    break block15;
                }
                case 1076356494: {
                    if (!subCommand.equals("equipment")) break;
                    typeIndex = 3;
                    break block15;
                }
                case -1039690024: {
                    if (!subCommand.equals("notice")) break;
                    typeIndex = 4;
                    break block15;
                }
            }
            typeIndex = -1;
        }
        switch (typeIndex) {
            case 0: {
                inventory = ViewLocationGui.getInstance().createGui((Player)sender);
                break;
            }
            case 1: {
                inventory = ViewShopGui.getInstance().createGui((Player)sender);
                break;
            }
            case 2: {
                inventory = ViewLogoShopGui.getInstance().createGui((Player)sender);
                break;
            }
            case 3: {
                inventory = ViewEquipmentGui.getInstance().createGui((Player)sender);
                break;
            }
            case 4: {
                inventory = ViewNoticeGui.getInstance().createGui((Player)sender);
                break;
            }
            default: {
                return;
            }
        }
        if (inventory == null) {
            return;
        }
        PlayerSchedulerUtil.openInventory((Player)sender, inventory);
    }

    @Override
    public String permission() {
        return "playerGuild.view";
    }

    @Override
    public void tab(HandyTab handyTab) {
        String[] subCommands = new String[5];
        subCommands[0] = "location";
        subCommands[1] = "shop";
        subCommands[2] = "logo";
        subCommands[3] = "equipment";
        subCommands[4] = "notice";
        handyTab.next(Arrays.asList(subCommands));
    }

    @Override
    public String command() {
        return "view";
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
