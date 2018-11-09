package com.burgess.starrybccmd.bukkit.event;

import com.burgess.starrybccmd.bukkit.MainContainer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BukkitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] strings) {
        if(!(sender instanceof Player)){
            sender.sendMessage(MainContainer.getServerPrefix() + "只有玩家才能执行此命令");
            return true;
        }

        if (strings.length < 2) {
            sender.sendMessage(MainContainer.getServerPrefix() + "指令有误 格式: /bccmd 服务器Id 执行命令");
            return true;
        }
        Player player = (Player) sender;

        String targetServerName = strings[0];
        StringBuilder command = new StringBuilder();
        command.append(strings[1]);
        for (int i = 2; i < strings.length; i++) {
            command.append(" ").append(strings[i]);
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(targetServerName);

        MainContainer.getInstance().getServer().sendPluginMessage(MainContainer.getInstance(), "BungeeCord", out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("bccmd");

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(command.toString());
        } catch (IOException e) {
            MainContainer.getInstance().getLogger().info(MainContainer.getServerPrefix() + "流写入失败，无法为远程服务器发送信息");
            e.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
        MainContainer.getInstance().getServer().sendPluginMessage(MainContainer.getInstance(), "BungeeCord", out.toByteArray());
        return true;
    }
}
