package seedu.address.commons.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import org.json.JSONObject;
//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import seedu.address.commons.core.LogsCenter;

public class GitHubUtil {
    private static final Logger logger = LogsCenter.getLogger(GitHubUtil.class);
    private static final String URL_PREFIX = "https://api.github.com/users/";
    private final Image defaultUserProfilePicture = new Image(this.getClass().getResourceAsStream("/images/profile.png"));
    private static int responseCode;
    private static URL url;

    private GitHubUtil(String userName) {
        String userUrl = URL_PREFIX + userName;

        try {
            url = new URL(userUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            responseCode = conn.getResponseCode();
        } catch (MalformedURLException e) {
            logger.severe("Improper URL Format.");
        } catch (IOException e) {
            logger.severe("A Connection with the server could not be established.");
        }
    }

    public static JSONObject getProfile(String userName) throws RuntimeException {
        assert userName != null || userName != "" : "No UserName Found";

        GitHubUtil g = new GitHubUtil(userName);
        JSONObject data = null;

        if (responseCode != 200) {
            logger.severe("Server responded with error code " + responseCode);
            throw new RuntimeException("Data could not be obtained.");
        } else {
            String inline = "";

            try {
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                scanner.close();
            } catch (IOException e) {
                logger.severe("Data could not be obtained from the URL.");
            }

            try {
                JSONParser jsonParser = new JSONParser();
                data = (JSONObject) jsonParser.parse(inline);
            } catch (ParseException e) {
                logger.severe("Data obtained could not be parsed into JSON Format.");
            }
        }
        return data;
    }

    public static Image getProfilePicture(String userName) {
        assert userName != null || userName != "" : "No UserName Found";
        JSONObject jsonObject = null;

        try {
            jsonObject = getProfile(userName);

            if (responseCode != 200) {
                return this.getClass().getResourceAsStream("/images/profile.png";
            }
        } catch (RuntimeException e) {
            logger.severe("Profile Picture Could not be obtained. Using default.");
        }

        assert jsonObject != null;

        String userProfileUrl = jsonObject.getString("avatar_url");
        Image image = new Image(userProfileUrl);

        return image;
    }

    public static int getCommits(String userName) {
        return 0;
    }
}
