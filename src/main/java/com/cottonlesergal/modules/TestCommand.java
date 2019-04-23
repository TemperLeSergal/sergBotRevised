package com.cottonlesergal.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

import java.time.temporal.ChronoUnit;

/**
 *
 * @author John Grosh (jagrosh)
 */
@CommandInfo(
        name = {"Test"},
        description = "Checks the bot's latency"
)
@Author("John Grosh (jagrosh)")
public class TestCommand extends Command {

    public TestCommand()
    {
        this.name = "test";
        this.help = "checks the bot's latency";
        this.guildOnly = false;
        this.aliases = new String[]{"pong"};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ping: ...", m -> {
            long ping = event.getMessage().getCreationTime().until(m.getCreationTime(), ChronoUnit.MILLIS);
            m.editMessage("Ping: " + ping  + "ms | Websocket: " + event.getJDA().getPing() + "ms").queue();
        });
    }

}