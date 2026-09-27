package cn.handyplus.guild.command.player;

import cn.handyplus.guild.constants.GuildSortEnum;
import cn.handyplus.guild.constants.ShopSearchEnum;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.inventory.GuildActivityGui;
import cn.handyplus.guild.inventory.GuildBankGui;
import cn.handyplus.guild.inventory.GuildMemberGui;
import cn.handyplus.guild.inventory.GuildNoticeGui;
import cn.handyplus.guild.inventory.GuildOpenGui;
import cn.handyplus.guild.inventory.GuildSacredStoneGui;
import cn.handyplus.guild.inventory.GuildShopGui;
import cn.handyplus.guild.inventory.GuildSignInGui;
import cn.handyplus.guild.inventory.GuildTaskGui;
import cn.handyplus.guild.inventory.GuildTitleGui;
import cn.handyplus.guild.inventory.GuildViewGui;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.lib.PlayerSchedulerUtil;
import cn.handyplus.guild.lib.fA;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.Arrays;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MeCommand
implements IHandyCommandEvent {
    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
        if (!guildPlayerOptional.isPresent()) {
            Inventory inventory = GuildOpenGui.getInstance().createGui(player, GuildSortEnum.PROSPERITY_DEGREE.getName());
            PlayerSchedulerUtil.openInventory(player, inventory);
            return;
        }
        GuildPlayer guildPlayer = guildPlayerOptional.get();
        String searchType = this.getArg(args, 1).orElse("me");
        fA guiType = fA.J(searchType);
        if (this.checkShopViewPermission(searchType, guildPlayer, player)) {
            return;
        }
        Inventory inventory;
        switch (guiType.ordinal()) {
            case 0: {
                inventory = GuildActivityGui.getInstance().createGui(player, guildPlayer.getGuildInfoId());
                break;
            }
            case 1: {
                inventory = GuildSacredStoneGui.getInstance().createGui(player, guildPlayer.getGuildInfoId());
                break;
            }
            case 2: {
                inventory = GuildSignInGui.getInstance().createGui(player, guildPlayer.getGuildInfoId());
                break;
            }
            case 3: {
                inventory = GuildShopGui.getInstance().createGui(player, guildPlayer.getGuildInfoId(), ShopSearchEnum.ALL.getType());
                break;
            }
            case 4: {
                inventory = GuildShopGui.getInstance().createGui(player, guildPlayer.getGuildInfoId(), ShopSearchEnum.SYSTEM.getType());
                break;
            }
            case 5: {
                inventory = GuildShopGui.getInstance().createGui(player, guildPlayer.getGuildInfoId(), ShopSearchEnum.PLAYER.getType());
                break;
            }
            case 6: {
                inventory = GuildBankGui.getInstance().createGui(player, guildPlayer.getGuildInfoId());
                break;
            }
            case 7: {
                inventory = GuildMemberGui.getInstance().createGui(player, guildPlayer.getGuildInfoId());
                break;
            }
            case 8: {
                inventory = GuildTaskGui.getInstance().createGui(player, guildPlayer.getGuildInfoId());
                break;
            }
            case 9: {
                inventory = GuildTitleGui.getInstance().createGui(player, guildPlayer.getGuildInfoId());
                break;
            }
            case 10: {
                inventory = GuildNoticeGui.getInstance().createGui(player, guildPlayer.getGuildInfoId(), "all");
                break;
            }
            default: {
                inventory = GuildViewGui.getInstance().createGui(player, guildPlayer.getGuildInfoId());
            }
        }
        PlayerSchedulerUtil.openInventory(player, inventory);
    }

    @Override
    public void tab(HandyTab handyTab) {
        String[] tabOptions = new String[8];
        tabOptions[0] = "activity";
        tabOptions[1] = "sacredStone";
        tabOptions[2] = "signIn";
        tabOptions[3] = "shop";
        tabOptions[4] = "bank";
        tabOptions[5] = "member";
        tabOptions[6] = "task";
        tabOptions[7] = "title";
        handyTab.next(Arrays.asList(tabOptions));
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    private boolean checkShopViewPermission(String searchType, GuildPlayer guildPlayer, Player player) {
        if (fA.g.OoOoOO().equalsIgnoreCase(searchType) || fA.m.OoOoOO().equalsIgnoreCase(searchType) || fA.A.OoOoOO().equalsIgnoreCase(searchType)) {
            int viewLevel = ConfigUtil.VIEW_CONFIG.getInt("shop.buyRoleLimit", 4);
            if (guildPlayer.getRoleId() > viewLevel) {
                MessageUtil.sendMessage(player, ConfigUtil.VIEW_CONFIG.getString("shop.noRoleButton"));
                return true;
            }
        }
        return false;
    }

    @Override
    public String permission() {
        return "playerGuild.me";
    }

    @Override
    public String command() {
        return "me";
    }
}
