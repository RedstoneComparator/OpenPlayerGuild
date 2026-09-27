package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.lib.StrUtil;
import cn.handyplus.guild.service.GuildInfoService;
import cn.handyplus.guild.util.ConfigUtil;
import java.util.UUID;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.ScopedNode;
import net.luckperms.api.util.Tristate;

public class LuckPermsUtil {
    private LuckPermsUtil() {
    }

    private static boolean isLuckPermsEnabled() {
        return PlayerGuild.USE_LP && ConfigUtil.LUCK_PERMS_CONFIG.getBoolean("enable");
    }

    public static String createGroup(Integer guildId) {
        if (!LuckPermsUtil.isLuckPermsEnabled() || guildId == null) {
            return null;
        }
        return InnerLuckPermsUtil.createGroup(guildId);
    }

    public static void deleteGroup(String groupName) {
        if (!PlayerGuild.USE_LP || StrUtil.isEmpty(groupName)) {
            return;
        }
        InnerLuckPermsUtil.deleteGroup(groupName);
    }

    public static void addUserToGroup(UUID playerUuid, String groupName) {
        if (!PlayerGuild.USE_LP || StrUtil.isEmpty(groupName) || playerUuid == null) {
            return;
        }
        InnerLuckPermsUtil.addUserToGroup(playerUuid, groupName);
    }

    public static void removeUserFromGroup(UUID playerUuid, String groupName) {
        if (!PlayerGuild.USE_LP || StrUtil.isEmpty(groupName) || playerUuid == null) {
            return;
        }
        InnerLuckPermsUtil.removeUserFromGroup(playerUuid, groupName);
    }

    private static class InnerLuckPermsUtil {
        private InnerLuckPermsUtil() {
        }

        public static String createGroup(Integer guildId) {
            String groupName = ConfigUtil.LUCK_PERMS_CONFIG.getString("groupName", "");
            String replacedGroupName = groupName = StrUtil.replace(groupName, "id", guildId.toString());
            LuckPermsProvider.get().getGroupManager().createAndLoadGroup(groupName).thenAcceptAsync(group -> {
                if (group != null) {
                    GuildInfoService.getInstance().setLuckPermsGroup(guildId, replacedGroupName);
                }
            });
            return replacedGroupName;
        }

        public static void deleteGroup(String groupName) {
            Group group = LuckPermsProvider.get().getGroupManager().getGroup(groupName);
            if (group != null) {
                LuckPermsProvider.get().getGroupManager().deleteGroup(group);
            }
        }

        public static void addUserToGroup(UUID playerUuid, String groupName) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            luckPerms.getUserManager().loadUser(playerUuid).thenAcceptAsync(user -> {
                ScopedNode groupNode = Node.builder((String)("group." + groupName)).build();
                if (user.data().contains((Node)groupNode, NodeEqualityPredicate.IGNORE_VALUE) == Tristate.UNDEFINED) {
                    user.data().add((Node)groupNode);
                    luckPerms.getUserManager().saveUser(user);
                }
            });
        }

        public static void removeUserFromGroup(UUID playerUuid, String groupName) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            luckPerms.getUserManager().loadUser(playerUuid).thenAcceptAsync(user -> {
                ScopedNode groupNode = Node.builder((String)("group." + groupName)).build();
                if (user.data().contains((Node)groupNode, NodeEqualityPredicate.IGNORE_VALUE) != Tristate.UNDEFINED) {
                    user.data().remove((Node)groupNode);
                    luckPerms.getUserManager().saveUser(user);
                }
            });
        }
    }
}
