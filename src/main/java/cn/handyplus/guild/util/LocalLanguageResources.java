package cn.handyplus.guild.util;

import cn.handyplus.guild.lib.BaseConstants;
import cn.handyplus.guild.lib.HandyConfigUtil;
import cn.handyplus.guild.lib.InitApi;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public final class LocalLanguageResources {
    private LocalLanguageResources() {
    }

    public static FileConfiguration loadLanguage() {
        Plugin plugin = InitApi.PLUGIN;
        String configured = BaseConstants.CONFIG.getString("language", "zh_CN");
        String locale = normalize(configured);
        String yamlLocale = locale.equals("zh_cn") ? "zh_CN" : configured;
        String yamlPath = "languages/" + yamlLocale + ".yml";
        FileConfiguration messages = HandyConfigUtil.load(yamlPath);
        if (messages == null) {
            messages = HandyConfigUtil.load("languages/zh_CN.yml");
        }
        BaseConstants.LANG_CONFIG = messages;
        BaseConstants.IS_CHECK_UPDATE = false;
        BaseConstants.IS_CHECK_UPDATE_TO_OP_MSG = false;
        loadDictionaries(plugin, locale);
        return messages;
    }

    public static void loadDictionaries(Plugin plugin, String language) {
        String locale = normalize(language);
        Map<String, String> translations;
        try (InputStream bundled = plugin.getResource("languages/zh_cn.json")) {
            if (bundled == null) {
                throw new IllegalStateException("Bundled languages/zh_cn.json is missing");
            }
            translations = read(new InputStreamReader(bundled, StandardCharsets.UTF_8));
        } catch (IOException error) {
            throw new IllegalStateException("Unable to read bundled translations", error);
        }
        String relativePath = "languages/" + locale + ".json";
        File local = new File(plugin.getDataFolder(), relativePath);
        if (!local.isFile()) {
            File legacy = new File(plugin.getDataFolder(), locale + ".json");
            try {
                if (legacy.isFile()) {
                    Files.createDirectories(local.toPath().getParent());
                    Files.copy(legacy.toPath(), local.toPath());
                } else {
                    try (InputStream resource = plugin.getResource(relativePath)) {
                        if (resource != null) {
                            plugin.saveResource(relativePath, false);
                        }
                    }
                }
            } catch (IOException error) {
                plugin.getLogger().warning("Unable to initialize local translations: " + relativePath);
            }
        }
        mergeLocal(plugin, local, translations);
        Map<String, String> custom = new HashMap<>();
        File items = new File(plugin.getDataFolder(), "item.json");
        if (!items.isFile()) {
            plugin.saveResource("item.json", false);
        }
        mergeLocal(plugin, items, custom);
        Map<String, String> materialNames = new HashMap<>();
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("item.minecraft.") || key.startsWith("block.minecraft.")) {
                String material = key.substring(key.indexOf('.', key.indexOf('.') + 1) + 1);
                if (material.matches("[a-z0-9_]+")) {
                    materialNames.put(material.toUpperCase(Locale.ROOT), entry.getValue());
                }
            }
        }
        materialNames.putAll(custom);
        BaseConstants.JSON_CACHE_MAP = translations;
        BaseConstants.ITEM_JSON_CACHE_MAP = custom;

        BaseConstants.CLOUD_ITEM_JSON_CACHE_MAP = materialNames;
    }

    private static String normalize(String language) {
        if (language == null || !language.matches("[a-zA-Z0-9_]+")) {
            return "zh_cn";
        }
        return language.toLowerCase(Locale.ROOT);
    }

    private static void mergeLocal(Plugin plugin, File file, Map<String, String> target) {
        if (!file.isFile()) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            target.putAll(read(reader));
        } catch (IOException | IllegalArgumentException | JsonParseException error) {
            plugin.getLogger().warning("Invalid local language JSON; using bundled values: " + file.getName());
        }
    }

    private static Map<String, String> read(Reader reader) {
        JsonElement root = new JsonParser().parse(reader);
        if (!root.isJsonObject()) {
            throw new IllegalArgumentException("Language JSON must be an object");
        }
        JsonObject object = root.getAsJsonObject();
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            if (!entry.getValue().isJsonPrimitive() || !entry.getValue().getAsJsonPrimitive().isString()) {
                throw new IllegalArgumentException("Language entries must be strings");
            }
            result.put(entry.getKey(), entry.getValue().getAsString());
        }
        return result;
    }
}
