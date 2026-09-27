package cn.handyplus.guild.hook;

import cn.handyplus.guild.PlayerGuild;
import cn.handyplus.guild.lib.MessageUtil;
import cn.handyplus.guild.util.ConfigUtil;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WorldEditUtil {
    private WorldEditUtil() {
    }

    public static void create(Location location) {
        if (!PlayerGuild.USE_WORLD_EDIT || !ConfigUtil.WORLD_EDIT_CONFIG.getBoolean("enable", true)) {
            return;
        }
        String schemName = ConfigUtil.WORLD_EDIT_CONFIG.getString("schemName", "");
        String schemPath = PlayerGuild.INSTANCE.getDataFolder().getAbsolutePath() + "/function/" + schemName;
        File schemFile = new File(schemPath);
        if (!schemFile.exists()) {
            MessageUtil.sendConsoleMessage("[SchematicPaste] Schematic not found!");
            return;
        }
        Clipboard clipboard = null;
        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile((File)schemFile);
        if (clipboardFormat != null) {
            try (ClipboardReader clipboardReader = clipboardFormat.getReader(Files.newInputStream(schemFile.toPath(), new OpenOption[0]));){
                clipboard = clipboardReader.read();
            }
            catch (IOException ioException) {
                Bukkit.getLogger().log(Level.SEVERE, "WorldEdit \u53d1\u751f\u5f02\u5e38", ioException);
            }
        }
        if (clipboard == null) {
            MessageUtil.sendConsoleMessage("[SchematicPaste]  clipboard is null!");
            return;
        }
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        if (ConfigUtil.WORLD_EDIT_CONFIG.getBoolean("customLocation.enable")) {
            x = ConfigUtil.WORLD_EDIT_CONFIG.getDouble("customLocation.x");
            y = ConfigUtil.WORLD_EDIT_CONFIG.getDouble("customLocation.y");
            z = ConfigUtil.WORLD_EDIT_CONFIG.getDouble("customLocation.z");
        }
        BukkitWorld bukkitWorld = new BukkitWorld(location.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world((World)bukkitWorld).maxBlocks(-1).build();){
            Operation operation = new ClipboardHolder(clipboard).createPaste((Extent)editSession).to(BlockVector3.at((double)x, (double)y, (double)z)).ignoreAirBlocks(false).build();
            Operations.complete((Operation)operation);
        }
        catch (WorldEditException worldEditException) {
            Bukkit.getLogger().log(Level.SEVERE, "WorldEdit \u53d1\u751f\u5f02\u5e38", worldEditException);
        }
    }
}
