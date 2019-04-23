package com.cottonlesergal.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Cotton Le Sergal
 */
@CommandInfo(
        name = {"SergMeme",""},
        description = "Posts a sergal meme."
)
@Author("Cotton Le Sergal")

public class SergMemeCommand extends Command {

    public ArrayList<String> images = new ArrayList<>(){{
        add("https://memestatic.fjcdn.com/large/pictures/c7/76/c77638_6681927.png");
        add("https://cdn.weasyl.com/static/media/f2/2e/55/f22e5532397bdd0f9f265c3932aa59414a96c79e364b18cc91d512e03a77a016.png");
        add("https://fellies.social/system/media_attachments/files/000/027/565/original/dce8682e1b969926.jpg");
        add("https://i.imgur.com/MB9IPzu.jpg");
        add("http://pm1.narvii.com/7087/02b847e3ae7cac3b8932ac3a349498ff9f5108cdr1-236-177v2_00.jpg");
        add("https://www.pinclipart.com/picdir/middle/189-1891201_cheese-wedge-png-png-free-download-sergal-cheese.png");
        add("https://i.redd.it/cjmlmaagcuiz.png");
        add("https://memestatic.fjcdn.com/gifs/The+wege+is+notamused_91b2fe_6854829.gif");
        add("https://ayoqq.org/images/sergal-lineart-cheese-head-6.jpg");
        add("https://media.discordapp.net/attachments/569720528361750556/569723511376183298/1540731507_tumblr_pa7qzv7xBO1v7l0vmo1_500.gif");
        add("https://orig00.deviantart.net/474e/f/2017/162/c/5/cheesey_sergal_by_scuterr-dbcbv3j.png");
        add("https://i.redditmedia.com/_odOaw5r0-wgIXNc2x_mtZsE0UpQ8JiPWnJomkHWvOw.jpg?s=40cf8e9a3be074cd527aa6b820e3480c");
        add("https://media.discordapp.net/attachments/569720528361750556/569724131273080920/lol.png");
        add("https://media.discordapp.net/attachments/569720528361750556/569724365214580746/9k.png");
        add("https://pbs.twimg.com/media/DXOOpmmV4AA-oVH.jpg");
        add("https://pm1.narvii.com/6335/f9eb5ea6d8234f41ccfce2594fa10e4b1c650a0e_hq.jpg");
        add("https://i.imgur.com/cp7Gmgk.jpg");
        add("https://media.discordapp.net/attachments/569720528361750556/569724934385827841/38424203_256543144986000_4596765413710233600_n.png");
        add("https://media.discordapp.net/attachments/569720528361750556/569725066414260244/1503871666.png");
        add("https://nocens.ru/i/4687/4687-59981bdd06a6f7bedaea3c24832a8b64665c468a.jpg");
        add("https://i.ytimg.com/vi/guzrlo4yt5s/maxresdefault.jpg");
        add("https://cdn.discordapp.com/attachments/569720528361750556/569725489690705942/ifeiURTZ26BH2bFE9YczLDwQYImGb_zCOHmLx5CXSRI.png");
        add("https://gifimage.net/wp-content/uploads/2018/11/sergal-gif-1.gif");
        add("https://pbs.twimg.com/media/DGANGQFUMAA-Iwl.jpg");
        add("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/intermediary/f/809a7e00-8b1b-4bcb-8120-1fa239b06ef8/d33kdhi-4212a1b0-c9b8-4296-b5c2-253411546304.png");
        add("https://i.redd.it/yiblp3lprc121.gif");
        add("https://d.facdn.net/art/felisrandomis/1535036482/1535036482.felisrandomis_slappy_bandrik.gif");
        add("https://media.discordapp.net/attachments/541039448704417793/569714425427263488/D4sBovgUEAAaKeh.png");
        add("https://media.discordapp.net/attachments/541039448704417793/569714446750973952/D4sBovjU8AA_Tp1.png");
        add("https://media.discordapp.net/attachments/541039448704417793/569714470432276482/D4sBovdUIAAVZVD.png");
        add("https://media.discordapp.net/attachments/541039448704417793/569714485741223950/D4sBovfUYAAQ_9R.png");
    }};

    public SergMemeCommand()
    {
        this.name = "serg";
        this.aliases = new String[]{"sergal","sergy"};
        this.help = "posts a sergal meme";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    protected void execute(CommandEvent event) {
        System.out.println("SERGMEME TIME!!!!!!");
        if(event.getTextChannel().getName().equals("sergal-chat"))
            event.reply(sergMemeBuilder(event).build());
    }

    public EmbedBuilder sergMemeBuilder(CommandEvent event){
        File file = null;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild().getSelfMember().getColor());
        builder.setAuthor("Sergal Memes by " + event.getSelfUser().getName() + "!", null, event.getSelfUser().getAvatarUrl());

        Random rand = new Random();
        builder.setImage(images.get(rand.nextInt(images.size())));
        builder.setTimestamp(ZonedDateTime.now());
        return builder;
    }

    public EmbedBuilder sergMemeBuilder(MessageReceivedEvent event){
        File file = null;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild().getSelfMember().getColor());
        builder.setAuthor("Sergal Memes by " + event.getJDA().getSelfUser().getName() + "!", null, event.getJDA().getSelfUser().getAvatarUrl());

        Random rand = new Random();
        builder.setImage(images.get(rand.nextInt(images.size())));
        builder.setTimestamp(ZonedDateTime.now());
        return builder;
    }
}
