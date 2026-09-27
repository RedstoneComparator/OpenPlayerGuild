package cn.handyplus.guild.command.admin;

import cn.handyplus.guild.constants.GuildActivityType;
import cn.handyplus.guild.constants.GuildPvpBattleTypeEnum;
import cn.handyplus.guild.core.match.MatchPvpManager;
import cn.handyplus.guild.core.season.SeasonPvpManager;
import cn.handyplus.guild.core.stronghold.StrongholdPvpManager;
import cn.handyplus.guild.lib.AssertUtil;
import cn.handyplus.guild.lib.BaseUtil;
import cn.handyplus.guild.lib.HandyTab;
import cn.handyplus.guild.lib.IHandyCommandEvent;
import cn.handyplus.guild.lib.MapUtil;
import cn.handyplus.guild.lib.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StopCommand
implements IHandyCommandEvent {
    @Override
    public String permission() {
        return "playerGuild.stop";
    }

    private int stopActivity(@NotNull GuildActivityType activityType) {
        switch (activityType) {
            case MATCH: {
                return MatchPvpManager.forceStop(GuildPvpBattleTypeEnum.MATCH);
            }
            case SCORE_MATCH: {
                return MatchPvpManager.forceStop(GuildPvpBattleTypeEnum.SCORE_MATCH);
            }
            case STRONGHOLD: {
                return StrongholdPvpManager.forceStop();
            }
            case SEASON: {
                if (!SeasonPvpManager.forceStop()) return 0;
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.next(GuildActivityType.getTypeList());
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public String command() {
        return "stop";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String typeName, String[] args) {
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        GuildActivityType guildActivityType = GuildActivityType.getTypeThrow(args[1].toLowerCase());
        int stoppedCount = this.stopActivity(guildActivityType);
        typeName = GuildActivityType.getDesc(guildActivityType);
        if (stoppedCount == 0) {
            MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("stopEmptyMsg", MapUtil.of("${type}", typeName)));
            return;
        }
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("stopSucceedMsg", MapUtil.of("${type}", typeName, "${num}", String.valueOf(stoppedCount))));
    }
}
