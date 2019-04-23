package com.cottonlesergal;

import com.cottonlesergal.modules.SergMemeCommand;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.Route;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Controller extends ListenerAdapter {
    static JDA bot;
    public void onReady(ReadyEvent event) {
        bot = event.getJDA();
        System.out.println("Ready!");
        JDA.ShardInfo shard = event.getJDA().getShardInfo();
        List<Guild> activeGuilds = event.getJDA().getGuilds();
        activeGuilds.forEach(guild -> {
            guild.getSelfMember().getRoles().forEach(role -> {
                if(role.getName().equals("NuBot")) {
                    role.getManager().setColor(new Color(255, 153, 255));
                }
            });
        });
        //event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE, Game.playing(shard.getShardString() + " || 'owo.help' for help"), false);
        event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE, Game.playing("'owo.commands' for help"), false);
    }

    public void onMessageReactionAdd(MessageReactionAddEvent event){

    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {

    }

    public void onMessageReceived(MessageReceivedEvent event) {
        Random rand = new Random();
        int random = rand.nextInt(100)+1;
        System.out.println(random);
        System.out.println(random > 95);
        Message message = event.getMessage();
        String msg = message.getContentDisplay();
        if((msg.toLowerCase().contains("cheese") || msg.toLowerCase().contains("sergal") || msg.contains("\uD83E\uDDC0"))
                && (!event.getTextChannel().getName().contains("log"))
                && random > 95){
            System.out.println("sergal meme incoming!!!!");
            event.getTextChannel().sendMessage(new SergMemeCommand().sergMemeBuilder(event).build()).queue();
        }else{
            System.out.println("not sending sergal meme, to low of a val.");
        }
        if(!event.getAuthor().isBot() || !event.getAuthor().isFake()) {
            String[] command = event.getMessage().getContentRaw().split(" ");
            User author = event.getAuthor();                //The user that sent the message
                    //The message that was received.
            MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
            //  This could be a TextChannel, PrivateChannel, or Group!

            if (event.isFromType(ChannelType.TEXT)) {
                Guild guild = event.getGuild();             //The Guild that this message was sent in. (note, in the API, Guilds are Servers)
                TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
                Member member = event.getMember();          //This Member that sent the message. Contains Guild specific information about the User!

                String name;
                if (message.isWebhookMessage())
                {
                    name = author.getName();                //If this is a Webhook message, then there is no Member associated
                }                                           // with the User, thus we default to the author for name.
                else
                {
                    name = member.getEffectiveName();       //This will either use the Member's nickname if they have one,
                }                                           // otherwise it will default to their username. (User#getName())
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd || HH:mm:ss");
                Date date = new Date();
                DateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date logDate = new Date();
                StringBuilder sb = new StringBuilder();
                sb.append("(" + guild.getName() + ") |" + dateFormat.format(date) + "| [" + textChannel.getName() + "]<" + name + ">: " + msg);
                if(event.getMessage().getAttachments().size() > 0){



                    for (Message.Attachment attch : event.getMessage().getAttachments()){
                        sb.append(attch.getProxyUrl());
                    }
                }
                System.out.println(sb.toString());
                /*System.out.println("Saving: " + sb.toString() + " |||| To: " + guild.getName() + " (" +
                        logDateFormat.format(logDate) + ").txt");
                manageFile.append(guild.getName() + " (" + logDateFormat.format(logDate) + ").txt", sb.toString());

                if(message.getContentDisplay().contains(Ref.prefix)) {
                    event.getChannel().sendMessage("Is that a command that I see?").queue();
                    new ModuleLoader().load(event, command);
                }*/
            }
            if (event.isFromType(ChannelType.PRIVATE)) {

            }
            if (event.isFromType(ChannelType.GROUP)) {

            }
        }
    }
}
