package net.toastynetworks;

import net.dv8tion.jda.core.JDA;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

public class CheckUpdates {

    HashMap<Integer, Integer> newestUpdates = new HashMap<Integer, Integer>();
    List<Integer> modpackList = asList(227724, 256183);
    ModpackAnnouncer modpackAnnouncer = new ModpackAnnouncer();


    public void getModpackFileData(int modpackId) throws Exception {
        AddDefaultValueInMapIfNonExists(modpackId);
        URL modpackURL = new URL("https://cursemeta.dries007.net/api/v2/direct/GetAllFilesForAddOn/" + modpackId);
        HttpURLConnection httpRequest = (HttpURLConnection) modpackURL.openConnection();
        httpRequest.setRequestMethod("GET");
        httpRequest.connect();
        String jsonResponse = convertStreamToString(httpRequest.getInputStream());
        JSONArray jsonArray = new JSONArray(jsonResponse);
        for (int i = 0; i < jsonArray.length(); i++) {
            int fileId = jsonArray.getJSONObject(i).getInt("Id");
            String release = jsonArray.getJSONObject(i).getString("ReleaseType");
            IsModpackUpdated(release, fileId, modpackId);
        }
    }


    public void AddDefaultValueInMapIfNonExists(int modpackId) {
        if (!newestUpdates.containsKey(modpackId)) {
            newestUpdates.put(modpackId, null);
        }
    }

    public boolean IsReleaseTypeReleased(String release) {
        return release.equals("Release");
    }

    private void IsModpackUpdated(String releaseType, int fileId, int modpackId) {
        if (IsReleaseTypeReleased(releaseType)) {
            if (newestUpdates.get(modpackId) == null) {
                newestUpdates.put(modpackId, fileId);
                modpackAnnouncer.sendAnnouncement();
                System.out.println("Map was null, so I added the correct fileId");
            } else if (newestUpdates.get(modpackId) != fileId) {
                newestUpdates.replace(modpackId, fileId);

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
