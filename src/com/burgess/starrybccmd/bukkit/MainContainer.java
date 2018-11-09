package com.burgess.starrybccmd.bukkit;

import com.burgess.starrybccmd.bukkit.event.BukkitCommand;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.craftersland.data.bridge.PD;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

public class MainContainer extends JavaPlugin implements PluginMessageListener {

    private static MainContainer INSTANCE;
    private static String serverPrefix = null;
    private static long delay;

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
            delay = this.getConfig().getLong("delay") * 20;
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

        if(!subchannel.equals("bccmd"))
            return;
        if (player == null || !player.isOnline())
            return;

        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);

        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
        String data = null;

        try {
            data = msgin.readUTF();
        } catch (IOException e) {
            this.getLogger().info(serverPrefix + "流写入失败，无法接受远程服务器信息");
            e.printStackTrace();
        }

        String finalData = data;
        this.getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
            try {
                while (PD.api.isSyncComplete(player)) {
                    Bukkit.getScheduler().runTask(this, () -> player.performCommand(finalData));
                    break;
                }
            } catch (NoClassDefFoundError e) {
                Bukkit.getScheduler().runTask(this, () -> player.performCommand(finalData));
            }
        }, delay);

    }

    public static MainContainer getInstance() {
        return INSTANCE;
    }

    public static String getServerPrefix() {
        return serverPrefix;
    }

}
