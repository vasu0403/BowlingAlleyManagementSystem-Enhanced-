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

    private static void writeInFile( JSONArray data) {
        try (FileWriter file = new FileWriter(db_file)) {

            file.write(data.toJSONString());
            file.flush();

        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public static String add(Party party, int[][] cumulScores, int gameNumber, int bowlIndex, int frameNumber, HashMap scores, int ball) {
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
        gameDetails.put("ball", ball);

        JSONObject gameObject = new JSONObject();
        gameObject.put("game", gameDetails);

        try (FileReader reader = new FileReader(db_file)) {
            Object obj = jsonParser.parse(reader);
            previousData = (JSONArray) obj;

            previousData.add(gameObject);
            writeInFile(previousData);
        } catch (FileNotFoundException e) {					// Make the file if file not found
            previousData = new JSONArray();
            previousData.add(gameObject);
            writeInFile(previousData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return playingBowlerNames.toString();
    }
    public static Vector get() {
        JSONArray storedData;
        JSONParser jsonParser = new JSONParser();
        Vector retData = new Vector();
        try (FileReader reader = new FileReader(db_file)) {
            Object obj = jsonParser.parse(reader);
            storedData = (JSONArray) obj;
            for(Object obj2: storedData) {
                JSONObject curGame = (JSONObject)((JSONObject) obj2).get("game");
                String curParty = curGame.get("party").toString();
                retData.add(curParty);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retData;
    }
    public static boolean resumeGame(String party, ControlDeskView controlDeskView) {
        JSONArray storedData;
        JSONArray newData = new JSONArray();
        JSONParser jsonParser = new JSONParser();
        boolean flag = false;
        try (FileReader reader = new FileReader(db_file)) {
            Object obj = jsonParser.parse(reader);
            storedData = (JSONArray) obj;
            for(Object obj2: storedData) {
                JSONObject curGame = (JSONObject)((JSONObject) obj2).get("game");
                String curParty = curGame.get("party").toString();
                if(party.equals(curParty) && !flag) {
                    boolean valid = controlDeskView.notifyControlDesk(obj2);
                    if(!valid) {
                        newData.add(obj2);
                    } else {
                        flag = true;
                    }
                } else {
                    newData.add(obj2);
                }
            }
            writeInFile(newData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }
    public static void remData(String pausedParty) {
        JSONArray storedData;
        JSONArray newData = new JSONArray();
        JSONParser jsonParser = new JSONParser();
        boolean flag = true;
        try (FileReader reader = new FileReader(db_file)) {
            Object obj = jsonParser.parse(reader);
            storedData = (JSONArray) obj;
            for(Object obj2: storedData) {
                JSONObject curGame = (JSONObject)((JSONObject) obj2).get("game");
                String curParty = curGame.get("party").toString();
                if(pausedParty.equals(curParty) && flag) {
                    flag = false;
                } else {
                    newData.add(obj2);
                }
            }
            writeInFile(newData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
