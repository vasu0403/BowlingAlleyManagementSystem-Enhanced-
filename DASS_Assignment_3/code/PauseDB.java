import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PauseDB {
    private static final String db_file = "PAUSED_GAMES.json";
    public static void add(Party party, int[][] cumulScores, int gameNumber, int bowlIndex, int frameNumber, HashMap scores) {
//        for (Object mapElement : scores.entrySet()) {
//            System.out.println(mapElement);
//        }
        JSONObject saveScores = new JSONObject();
        Iterator iterator = scores.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)iterator.next();
            String nick = ((Bowler)mapElement.getKey()).getNick();
            int[] nickScore = (int[] )scores.get((Bowler)mapElement.getKey());
            saveScores.put(nick, Arrays.toString(nickScore));
        }
        JSONArray previousData;
        JSONParser jsonParser = new JSONParser();
        Vector<Bowler> playingBowlers = party.getMembers();
        Vector<String> playingBowlerNames = new Vector<String>();
        for(Bowler curBowler: playingBowlers) {
            playingBowlerNames.add(curBowler.getNick());
        }
        JSONObject gameDetails = new JSONObject();
        gameDetails.put("party", playingBowlerNames.toString());
        gameDetails.put("cumulScores", Arrays.deepToString(cumulScores));
        gameDetails.put("gameNumber", gameNumber);
        gameDetails.put("bowlIndex", bowlIndex);
        gameDetails.put("frameNumber", frameNumber);
        gameDetails.put("score", saveScores);

        JSONObject gameObject = new JSONObject();
        gameObject.put("game", gameDetails);

        try (FileReader reader = new FileReader(db_file)) {
            Object obj = jsonParser.parse(reader);
            previousData = (JSONArray) obj;

            previousData.add(gameObject);

            try (FileWriter file = new FileWriter(db_file)) {

                file.write(previousData.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {					// Make the file if file not found
            previousData = new JSONArray();
            previousData.add(gameObject);
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
}
