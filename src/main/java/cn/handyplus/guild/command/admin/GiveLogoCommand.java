package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.enter.GuildInfo;
import cn.handyplus.guild.enter.GuildLogo;
import cn.handyplus.guild.enter.GuildPlayer;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.ItemStackUtil;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.service.GuildLogoService;
import cn.handyplus.guild.service.GuildPlayerService;
import cn.handyplus.guild.util.GuildUtil;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveLogoCommand
implements IHandyCommandEvent {
    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(partialName -> GuildUtil.guildNamesWithVar());
    }

    @Override
    public String command() {
        return "giveLogo";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String permission() {
        return "playerGuild.giveLogo";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Integer guildInfoId;
        AssertUtil.notTrue(((String[])args).length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String guildName = args[1];
        ItemStack itemStack = ItemStackUtil.getItemInMainHand(player.getInventory());
        AssertUtil.notTrue(Material.AIR.equals((Object)itemStack.getType()), BaseUtil.getLangMsg("noAir"));
        itemStack = itemStack.clone();
        itemStack.setAmount(1);
        if ("${guildName}".equalsIgnoreCase(guildName)) {
            if (BaseUtil.isNotPlayer(sender)) {
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
                return;
            }
            Optional<GuildPlayer> guildPlayerOptional = GuildPlayerService.getInstance().findByPlayerUuid(player.getUniqueId());
            if (!guildPlayerOptional.isPresent()) {
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noGuildMsg"));
                return;
            }
            guildInfoId = guildPlayerOptional.get().getGuildInfoId();
        } else {
            Optional<GuildInfo> guildOptional = GuildInfoService.getInstance().findByGuildName(guildName);
            if (!guildOptional.isPresent()) {
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noGuildMsg"));
                return;
            }
            guildInfoId = guildOptional.get().getId();
        }
        GuildLogoService.getInstance().updateUseToFalse(guildInfoId);
        GuildLogo guildLogo = new GuildLogo();
        guildLogo.setGuildInfoId(guildInfoId);
        guildLogo.setLogoId(-1);
        guildLogo.setLogoName(ItemStackUtil.itemStackSerialize(itemStack));
        guildLogo.setUseFlag(true);
        int result = GuildLogoService.getInstance().add(guildLogo);
        GuildInfoService.getInstance().updateLogoById(guildInfoId, -1, ItemStackUtil.itemStackSerialize(itemStack));
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg(result > 0 ? "succeedMsg" : "failureMsg"));
    }
}
