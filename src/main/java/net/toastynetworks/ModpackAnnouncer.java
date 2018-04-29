package net.toastynetworks;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class ModpackAnnouncer {

    private static JDA jda;
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static HashMap<Integer, Integer> newestUpdates = new HashMap<Integer, Integer>();
    private static List<Integer> modpackList = asList(227724, 256183);



    public static void main(String[] arguments) throws Exception
    {
        jda = new JDABuilder(AccountType.BOT).setToken("NDM4Nzc0Mjc2NDYwNjQyMzA0.DcJf-Q.qVc_ND5Hbr9GIAStjF6Lsu-GBac").buildAsync();
        scheduler.scheduleWithFixedDelay(() -> {
            callForUpdates();
                }, 0L,1L, TimeUnit.MINUTES);
    }


    public static void callForUpdates() {
        try {
            for (int modpackId: modpackList) {
                getModpackFileData(modpackId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendAnnouncement() {
        Message msg = new MessageBuilder().append("Announcement announcement announcement!").build();
        List<TextChannel> channelList = jda.getTextChannelsByName("html", true);
        if (!channelList.isEmpty()) {
            TextChannel channel = channelList.get(0);
            channel.sendMessage(msg).submit();
        } else {
            System.out.println("No channels found");
        }
    }

    public static void getModpackFileData(int modpackId) throws Exception {
        URL modpackURL = new URL("https://cursemeta.dries007.net/api/v2/direct/GetAllFilesForAddOn/" + modpackId);
        HttpURLConnection httpRequest = (HttpURLConnection) modpackURL.openConnection();
        httpRequest.setRequestMethod("GET");
        httpRequest.connect();
        String jsonResponse = convertStreamToString(httpRequest.getInputStream());
        JSONArray jsonArray = new JSONArray(jsonResponse);
        for (int i = 0; i < jsonArray.length(); i++) {
            int fileId = jsonArray.getJSONObject(i).getInt("Id");
            String release = jsonArray.getJSONObject(i).getString("ReleaseType");
            isModpackUpdated(release, fileId, modpackId);
        }
    }

    public static boolean isReleaseTypeReleased(String release) {
        return release.equals("Release");
    }

    private static void isModpackUpdated(String releaseType, int fileId, int modpackId) {
        if (!newestUpdates.containsKey(modpackId)) {
            newestUpdates.put(modpackId, fileId);
            System.out.println("Added default value of " + modpackId +  " because map was empty");
        } else if (isReleaseTypeReleased(releaseType)){
            if(newestUpdates.get(modpackId) != fileId) {
                newestUpdates.replace(modpackId, fileId);
                sendAnnouncement();

                System.out.println("Map was not null, new fileId was different than old one so I replaced the value in the hashmap");
            }
        }
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }
    }
