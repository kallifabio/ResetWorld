/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 11.09.2024 um 19:39
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.utils
 */

package de.kallifabio.resetworld.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FileBuilder {

    private File file;
    private YamlConfiguration cfg;

    public FileBuilder(String filePath, String fileName) {
        file = new File(filePath, fileName);
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public FileBuilder setValue(String valuePath, Object value) {
        cfg.set(valuePath, value);
        return this;
    }

    public boolean exist() {
        return file.exists();
    }

    public Object getObject(String valuePath) {
        return cfg.get(valuePath);
    }

    public int getInt(String valuePath) {
        return cfg.getInt(valuePath);
    }

    public String getString(String valuePath) {
        return cfg.getString(valuePath);
    }

    public boolean getBoolean(String valuePath) {
        return cfg.getBoolean(valuePath);
    }

    public long getLong(String valuePath) {
        return cfg.getLong(valuePath);
    }

    public List<String> getStringList(String valuePath) {
        return cfg.getStringList(valuePath);
    }

    public Set<String> getKeys(boolean deep) {
        return cfg.getKeys(deep);
    }

    public ConfigurationSection getConfigurationSection(String section) {
        return cfg.getConfigurationSection(section);
    }

    public FileBuilder save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}
