package the.flash.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 在控制台要执行的指令操作
 */
public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}
