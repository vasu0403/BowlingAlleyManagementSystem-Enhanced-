import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class db {
    private static final String db_file = "SCORE_HISTORY.json";
    public static void adddata(String nick, String date, String score) {

        JSONArray previousData;
        JSONParser jsonParser = new JSONParser();

        JSONObject playerDetails = new JSONObject();
        playerDetails.put("nick", nick);
        playerDetails.put("date", date);
        playerDetails.put("score", score);

        JSONObject playerObject = new JSONObject();
        playerObject.put("player", playerDetails);

        try (FileReader reader = new FileReader(db_file)) {
            Object obj = jsonParser.parse(reader);
            previousData = (JSONArray) obj;

            previousData.add(playerObject);

            try (FileWriter file = new FileWriter(db_file)) {

                file.write(previousData.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {					// Make the file if file not found
            previousData = new JSONArray();
            previousData.add(playerObject);
            try (FileWriter file = new FileWriter(db_file)) {

                file.write(previousData.toJSONString());
                file.flush();

            } catch (IOException err) {
                err.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public static JSONArray getData() {
        JSONArray storedData;
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(db_file)) {
            Object obj = jsonParser.parse(reader);
            storedData = (JSONArray) obj;
            return storedData;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        storedData = new JSONArray();
        return storedData;
    }
}
