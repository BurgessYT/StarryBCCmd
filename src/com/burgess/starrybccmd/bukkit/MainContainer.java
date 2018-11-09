package com.burgess.starrybccmd.bukkit;

import com.burgess.starrybccmd.bukkit.event.BukkitCommand;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;

public class MainContainer extends JavaPlugin implements PluginMessageListener {

    private static MainContainer INSTANCE;
    private static String serverPrefix = null;

    @Override
    public void onLoad() {
        try {
            this.getLogger().info("StarryBCCmd正在加载...");
            if (!getDataFolder().exists()) {
                this.getDataFolder().mkdirs();
            }
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                this.saveDefaultConfig();
            }
            serverPrefix = this.getConfig().getString("serverPrefix");
        } catch (Exception e) {
            this.getLogger().info("StarryBCCmd加载失败");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getPluginCommand("bccmd").setExecutor(new BukkitCommand());
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        System.out.println(subchannel);
    }

    public static MainContainer getInstance() {
        return INSTANCE;
    }

    public static String getServerPrefix() {
        return serverPrefix;
    }
}
