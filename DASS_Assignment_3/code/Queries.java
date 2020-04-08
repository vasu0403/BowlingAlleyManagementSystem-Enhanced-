import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Queries {
    private static JSONArray findMatchingPlayer(int required, JSONArray storedData) {
        JSONArray retData = new JSONArray();
        for(Object obj: storedData) {
            JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
            int curScore = Integer.parseInt(curPlayer.get("score").toString());
            if(curScore == required) {
                retData.add(((JSONObject) obj));
            }
        }
        return retData;
    }

    public static Vector<String> mostFrequentPlayer() {
        JSONArray storedData = db.getData();
        int maxFreq = 0;
        HashMap<String, Integer> playerCountMap = new HashMap<String, Integer>();
        for(Object obj: storedData) {
            JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
            String nick = curPlayer.get("nick").toString();
            if(playerCountMap.containsKey(nick)) {
                playerCountMap.put(nick, playerCountMap.get(nick) + 1);
            } else {
                playerCountMap.put(nick, 1);
            }
            maxFreq = Math.max(maxFreq, playerCountMap.get(nick));
        }
        Vector<String> retVector = new Vector<String>();
        for (Map.Entry mapElement : playerCountMap.entrySet()) {
            String playerNick = (String)mapElement.getKey();
            int frequency = (int)mapElement.getValue();
            if(frequency == maxFreq) {
                retVector.add(playerNick);
            }
        }
        return retVector;
    }

    public static JSONArray highestScore() {
        JSONArray storedData = db.getData();
        int maxScore = -1;
        for(Object obj: storedData) {
            JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
            int curScore = Integer.parseInt(curPlayer.get("score").toString());
            if(maxScore < curScore) {
                maxScore = curScore;
            }
        }
        return findMatchingPlayer(maxScore, storedData);
    }

    public static JSONArray lowestScore() {
        JSONArray storedData = db.getData();
        int minScore = 10000;
        for(Object obj: storedData) {
            JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
            int curScore = Integer.parseInt(curPlayer.get("score").toString());
            if(minScore > curScore) {
                minScore = curScore;
            }
        }
        return findMatchingPlayer(minScore, storedData);
    }
}
