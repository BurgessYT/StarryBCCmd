package com.burgess.starrybccmd.bukkit.event;

import com.burgess.starrybccmd.bukkit.MainContainer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        String targetServerName = strings[0];
        StringBuilder command = new StringBuilder();
        command.append(strings[1]);
        for (int i = 2; i < strings.length; i++) {
            command.append(" ").append(strings[i]);
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("");

        return false;
    }
}
