package com.burgess.starrybccmd.bungeecord;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MainContainer extends Plugin {

    public static Configuration config = null;

    @Override
    public void onLoad() {
        getLogger().info("StarryBCCmd正在加载...");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().info("StarryBCCmd配置文件加载异常");
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            getLogger().info("StarryBCCmd配置文件加载异常");
        }

    }

    @Override
    public void onEnable() {
//        this.getProxy().getPluginManager().registerCommand(this, new BungeecordCommand().setPlugin(this));
        getLogger().info("StarryBCCmd已经启动!");
    }

    @Override
    public void onDisable() {
        getLogger().info("StarryBCCmd已关闭!");
    }
}
