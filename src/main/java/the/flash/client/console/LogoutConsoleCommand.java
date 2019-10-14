package the.flash.client.console;

import io.netty.channel.Channel;
import the.flash.protocol.request.LogoutRequestPacket;

import java.util.Scanner;

/**
 * 登出指令操作
 */
public class LogoutConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        LogoutRequestPacket logoutRequestPacket = new LogoutRequestPacket();
        channel.writeAndFlush(logoutRequestPacket);
    }
}
