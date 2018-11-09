package com.burgess.starrybccmd.bungeecord.event;

import com.burgess.starrybccmd.bungeecord.MainContainer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class BungeecordCommand extends Command {

    private Plugin plugin = null;

    BungeecordCommand() {
        super("bccmd");
    }

    BungeecordCommand setPlugin(Plugin plugin) {
        this.plugin = plugin;
        return this;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        String serverPrefix = MainContainer.config.getString("serverPrefix").replaceAll("&","§");
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(serverPrefix + "只有玩家才能执行此命令");
        } else {
            //玩家执行
            ProxiedPlayer player = (ProxiedPlayer)commandSender;
            if (strings.length < 2) {
                commandSender.sendMessage(serverPrefix + "指令有误 格式: /bccmd 服务器Id 执行命令");
                return;
            }
            ServerInfo target = ProxyServer.getInstance().getServerInfo(strings[0]);
            if (target == null || (!target.canAccess(player))) {
                player.sendMessage(serverPrefix + "服务器名称有误或玩家无权限传送指定服务器");
            } else {
                player.connect(target);
                long delay = MainContainer.config.getLong("delay");
                if (delay > 0) {
                    StringBuilder command = new StringBuilder();
                    for (int i = 1; i < strings.length; i++) {
                        if (i != 1) {
                            command.append(" ").append(strings[i]);
                        } else {
                            command.append(strings[i]);
                        }

                    }
                    plugin.getProxy().getScheduler().schedule(plugin, () -> {
                        player.chat(command.toString());
                    }, delay, TimeUnit.SECONDS);
                } else {
                    player.sendMessage(serverPrefix + "配置中执行延时时间有误");
                }
            }
        }

    }
}
