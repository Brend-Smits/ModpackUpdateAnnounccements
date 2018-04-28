package net.toastynetworks;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ModpackAnnouncer {

    private static JDA jda;
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public static void main(String[] arguments) throws Exception
    {
        jda = new JDABuilder(AccountType.BOT).setToken("NDM4Nzc0Mjc2NDYwNjQyMzA0.DcJf-Q.qVc_ND5Hbr9GIAStjF6Lsu-GBac").buildAsync();
        jda.addEventListener(new EventListener());
        scheduler.scheduleWithFixedDelay(() -> {
            callForUpdates();
                }, 0L,1L, TimeUnit.MINUTES);

    }


    public static void callForUpdates() {
        CheckUpdates checkUpdates = new CheckUpdates();
        try {
            for (int modpackId:checkUpdates.modpackList) {
                checkUpdates.getModpackFileData(modpackId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAnnouncement() {
        Message msg = new MessageBuilder().append("Announcement announcement announcement!").build();
        List<TextChannel> channelList = jda.getTextChannelsByName("html", true);
        for (TextChannel channel: channelList) {
            System.out.println(channel.getIdLong());
            System.out.println(channel.toString());
        }
        if (!channelList.isEmpty()) {
            TextChannel channel = channelList.get(0);
            System.out.println(channel.canTalk());
            System.out.println(channel.getIdLong());
            System.out.println(channel.toString());
            channel.sendMessage(msg);
        } else {
            System.out.println("No channels found");
        }
    }
    }
