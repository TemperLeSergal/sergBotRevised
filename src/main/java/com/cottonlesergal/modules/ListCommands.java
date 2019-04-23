package com.cottonlesergal.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;


/**
 *
 * @author Cotton Le Sergal
 */
@CommandInfo(
        name = {"ListCommands"},
        description = "Posts a help info."
)
@Author("Cotton Le Sergal")
public class ListCommands extends Command {

    public ListCommands(){
        this.name = "commands";
        this.help = "posts help info";
        this.guildOnly = false;
        this.arguments = "<item> <item> ...";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }
    @Override
    protected void execute(CommandEvent event) {
        String[] items = event.getArgs().split("\\s+");
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(new Color(255, 153, 255));
        if(items.length > 0 && !event.getArgs().equals("")){
            builder.setAuthor("This is basic info for the " + items[0] + " command, " + event.getAuthor().getAsMention());
            switch(items[0]){
                case "about" : {
                    builder.setDescription("The about command informs users of the bot, it's creator, and the server itself.");
                    builder.addField("Command(s)", "owo.about", true);
                    builder.addField("Result", "-Informs user", true);
                    builder.addField("Permissions", "Everyone", true);
                    event.replyInDm(builder.build());
                }
                break;
                case "cat" : {
                    builder.setDescription("The cat command posts a random image of a cat.");
                    builder.addField("Command(s)", "owo.cat", true);
                    builder.addField("Result", "-posts a random image of a cat.", true);
                    builder.addField("Permissions", "Not Implemented", true);
                    event.replyInDm(builder.build());
                }
                break;
                case "guildlist" : {
                    builder.setDescription("The guildlist command lists all guild the bot is in.");
                    builder.addField("Command(s)", "owo.guild\nowo.guildlist", true);
                    builder.addField("Result", "-lists guilds bot is in\n-lists guilds bot is in", true);
                    builder.addField("Permissions", "Bot Owner only", true);
                    event.replyInDm(builder.build());
                }
                break;
                case "commands" : {
                    builder.setDescription("The help command informs users how to use the bot.");
                    builder.addField("Command(s)", "owo.commands\nowo.commands [command name]", true);
                    builder.addField("Result", "-informs the user of commands\n-informs user on how to use commands", true);
                    builder.addField("Permissions", "Everyone", true);
                    event.replyInDm(builder.build());
                }
                break;
                case "music" : {
                    builder.setDescription("The music command allows members to play music.");
                    builder.addField("Command(s)", "" +
                            "owo.music play" +
                            "\nowo.music pause" +
                            "\nowo.music leave" +
                            "\nowo.music skip" +
                            "\nowo.music current" +
                            "\nowo.music volume", true);
                    builder.addField("Result", "" +
                            "-Plays music. Type name of song or URL." +
                            "\n-Pauses music, if paused plays music." +
                            "\n-Ends song, and leaves VC." +
                            "\n-Skips the current song." +
                            "\n-Displays info about current song." +
                            "\n-Sets the volume of the bot, default is 10.", true);
                    builder.addField("Permissions", "Everyone", true);
                    event.replyInDm(builder.build());
                }
                break;
                case "ping" : {
                    builder.setDescription("The help command informs users how to use the bot.");
                    builder.addField("Command(s)", "owo.ping", true);
                    builder.addField("Result", "-Displays bot response time, and websocket time in ms.", true);
                    builder.addField("Permissions", "Everyone", true);
                    event.replyInDm(builder.build());
                }
                break;
                case "sergmeme" : {
                    builder.setDescription("The help command informs users how to use the bot.");
                    builder.addField("Command(s)", "" +
                            "owo.serg" +
                            "\nserg" +
                            "\ncheese" +
                            "\nsergal" +
                            "\n:cheese:", true);
                    builder.addField("Result", "" +
                            "-Command used to post sergal meme in sergal chat." +
                            "\n-5% chance to post serg meme."+
                            "\n-5% chance to post serg meme."+
                            "\n-5% chance to post serg meme."+
                            "\n-5% chance to post serg meme.", true);
                    builder.addField("Permissions", "Everyone", true);
                    event.replyInDm(builder.build());
                }
                break;
            }
        }else{
            event.reactSuccess();
            builder.setAuthor("This is basic info for the bot, " + event.getAuthor().getName());
            builder.setDescription("For more help with a command, please type owo.commands [command name]: owo.commands music. The following implemented commands are:");
            builder.addField("", "  **-about**\n  **-cat**\n  **-guildlist**", true);
            builder.addField("", "  **-commands**\n  **-music**\n  **-ping**", true);
            builder.addField("", "  **-sergmeme**\n  **-more to be implemented later**", true);
            event.replyInDm(builder.build());
        }
    }
}
