package com.cottonlesergal.modules;


import com.cottonlesergal.libraries.GuildMusicManager;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MusicCommand extends Command {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicCommand(){
        this.name = "music";
        this.help = "control music functions";
        this.arguments = "<item> <item> ...";
        this.guildOnly = false;
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Music");
        getGuildAudioPlayer(event.getGuild()).player.setVolume(10);
        if(event.getArgs().isEmpty())
        {
            event.reply("args empty");
        }
        else
        {
            // split the choices on all whitespace
            String[] items = event.getArgs().split("\\s+");
            String currentVoiceChannel = event.getMember().getVoiceState().getChannel().getName();

            // if there is only one option, have a special reply
            if(items.length==1 && items[0].equals("play"))
                event.replyWarning("Please specify actions for: `"+items[0]+"`");

                // otherwise, pick a random response
            else
            {
                switch(items[0]){
                    case "play" : {
                        System.out.println("PLaying");
                        if(items[1].substring(0,3).equals("http")){
                            loadAndPlay(event.getTextChannel(), items[1], event);
                        }else{
                            String youtube = "https://www.youtube.com";
                            StringBuilder URLBuilder = new StringBuilder(youtube);
                            String queryString = "/results?search_query=";
                            StringBuilder query = new StringBuilder(URLBuilder).append(queryString);
                            for (int i = 1; i < items.length; i++) {
                                query.append(items[i] + "+");
                            }
                            query.replace(query.length(),query.length(),"");
                            Document youtubeSearch = null;
                            try {
                                youtubeSearch = Jsoup.connect(query.toString()).get();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Elements videos = youtubeSearch.select("a[href]");
                            for (Element video : videos){
                                if(video.attr("href").contains("/watch?")){
                                    String songURL = URLBuilder
                                            .replace(0, URLBuilder.length(), youtube)
                                            .append(video.attr("href"))
                                            .toString();
                                    loadAndPlay(event.getTextChannel(), songURL, event);
                                    event.reply("video link: " + youtube + videos.attr("href"));
                                    event.reply("website link: " + youtubeSearch.location());
                                    break;
                                }
                            }
                        }
                    }
                    break;
                    case "pause" : {
                        AudioPlayer player = getGuildAudioPlayer(event.getGuild()).player;
                        if(player.isPaused()){
                            player.setPaused(false);
                        }else{
                            player.setPaused(true);
                        }
                    }
                    break;
                    case "leave" : {
                        AudioPlayer player = getGuildAudioPlayer(event.getGuild()).player;
                        try{
                            player.getPlayingTrack().stop();
                        }catch(NullPointerException e){

                        }
                        try{
                            event.getGuild().getAudioManager().closeAudioConnection();
                        }catch(Exception e){

                        }
                        try{
                            getGuildAudioPlayer(event.getGuild()).player.destroy();
                        }catch(Exception e){

                        }
                        leaveVoiceChannel(event.getGuild().getAudioManager(), event);
                    }
                    break;
                    case "skip" : {
                        skipTrack(event.getTextChannel());
                    }
                    break;
                    case "queue" : {
                        if(items.length > 1){
                            if(items[1].equals("clear")){
                                getGuildAudioPlayer(event.getGuild()).player.destroy();
                            }
                        }
                        getGuildAudioPlayer(event.getGuild()).scheduler.getQueue().forEach(audioTrack -> {
                            event.reply(audioTrack.getInfo().author);
                            event.reply(audioTrack.getInfo().title);
                            event.reply(String.valueOf((audioTrack.getInfo().length/60)));
                        });
                    }
                    break;
                    case "current" : {
                        event.reply(getGuildAudioPlayer(event.getGuild()).player.getPlayingTrack().getInfo().title);
                        event.reply(getGuildAudioPlayer(event.getGuild()).player.getPlayingTrack().getInfo().author);
                        event.reply(String.valueOf(getGuildAudioPlayer(event.getGuild()).player.getPlayingTrack().getInfo().length/60));
                    }
                    break;
                    case "volume" : {
                        try {
                            getGuildAudioPlayer(event.getGuild()).player.setVolume(Integer.parseInt(items[1]));
                        }catch(Exception e){
                            event.replyWarning("Please provide a number.");
                        }
                    }
                }
            }
        }
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl, CommandEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(channel.getGuild(), musicManager, track, event);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

                play(channel.getGuild(), musicManager, firstTrack, event);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, CommandEvent event) {
        connectToFirstVoiceChannel(guild.getAudioManager(), event);

        musicManager.scheduler.queue(track);
    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

    private static void leaveVoiceChannel(AudioManager audioManager, CommandEvent event){
        if (audioManager.isConnected() && audioManager.isAttemptingToConnect()) {
            audioManager.closeAudioConnection();
        }
    }

    private static void connectToFirstVoiceChannel(AudioManager audioManager, CommandEvent event) {
        System.out.println("Joining the call");
        event.getAuthor().getJDA().getVoiceChannels().forEach(voiceChannel -> System.out.println(voiceChannel.getName()));
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                if(voiceChannel.getName().equals(event.getMember().getVoiceState().getChannel().getName())) {
                    audioManager.openAudioConnection(voiceChannel);
                    break;
                }
            }
        }else{
            System.out.println("Already in the call or am already trying to connect.");
        }
    }
}
