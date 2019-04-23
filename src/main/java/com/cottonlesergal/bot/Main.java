package com.cottonlesergal.bot;

import com.cottonlesergal.Controller;
import com.cottonlesergal.modules.*;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.ShutdownCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception
    {

        ArrayList list = new ArrayList();

        try (Stream<String> stream = Files.lines(Paths.get("C:\\Users\\Admin\\Desktop\\ideaprojects\\sergBotRevised\\src\\main\\resources\\files\\text\\bot_info"))) {
            stream.forEach(list::add);
        }

        // the first is the bot token
        String token = (String) list.get(0);

        // the second is the bot's owner's id
        String ownerId = (String) list.get(1);

        // define an eventwaiter, dont forget to add this to the JDABuilder!
        EventWaiter waiter = new EventWaiter();

        // define a command client
        CommandClientBuilder client = new CommandClientBuilder();

        // The default is "Type !!help" (or whatver prefix you set)
        client.useDefaultGame();

        // sets the owner of the bot
        client.setOwnerId(ownerId);

        // sets emojis used throughout the bot on successes, warnings, and failures
        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");

        // sets the bot prefix
        client.setPrefix("owo.");

        // adds commands
        client.addCommands(
                // command to show information about the bot
                new AboutCommand(Color.BLUE, "an example bot",
                        new String[]{"Cool commands","Nice examples","Lots of fun!"},
                        Permission.ADMINISTRATOR),

                // command to show a random cat
                new CatCommand(),

                /*// command to make a random choice
                new ChooseCommand(),

                // command to say hello
                new HelloCommand(waiter),*/

                new MusicCommand(),

                new GuildlistCommand(waiter),

                // command to check bot latency
                new PingCommand(),

                new SergMemeCommand(),

                new ListCommands(),

                // command to shut off the bot
                new ShutdownCommand());



        // start getting a bot account set up
        new JDABuilder(AccountType.BOT)
                // set the token
                .setToken(token)

                // set the game for when the bot is loading
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setGame(Game.playing("loading..."))

                // add the listeners
                .addEventListener(waiter)
                .addEventListener(client.build())
                .addEventListener(new Controller())

                // start it up!
                .buildAsync();
    }
}
