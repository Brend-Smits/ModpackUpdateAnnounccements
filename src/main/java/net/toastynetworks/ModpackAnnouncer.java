package net.toastynetworks;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ModpackAnnouncer {

    private static JDA jda;
    private static ScheduledExecutorService scheduler;


    public static void main(String[] arguments) throws Exception
    {
        Executors.newSingleThreadScheduledExecutor();
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
        TextChannel channel = jda.getTextChannelById(366923238283935755L);
        channel.sendMessage(msg);
    }
    }