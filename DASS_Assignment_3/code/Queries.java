import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Queries {

    public static String topPlayer() {
        JSONArray storedData = db.getData();
        storedData.forEach(obj -> {
            JSONObject curPlayer = (JSONObject) ((JSONObject) obj).get("player");
            System.out.println(curPlayer.get("nick"));
        });
        String ret = "Searching for top player";
        return ret;
    }

    public static JSONArray highestScore() {
        JSONArray storedData = db.getData();
        JSONArray retData = new JSONArray();

        int maxScore = -1;
        for(Object obj: storedData) {
            JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
            int curScore = Integer.parseInt(curPlayer.get("score").toString());
            if(maxScore < curScore) {
                maxScore = curScore;
            }
        }
        for(Object obj: storedData) {
            JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
            int curScore = Integer.parseInt(curPlayer.get("score").toString());
            if(curScore == maxScore) {
                retData.add(((JSONObject) obj));
            }
        }
        return retData;
    }

    public static JSONArray lowestScore() {
        JSONArray storedData = db.getData();
        JSONArray retData = new JSONArray();

        int minScore = 10000;
        for(Object obj: storedData) {
            JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
            int curScore = Integer.parseInt(curPlayer.get("score").toString());
            if(minScore > curScore) {
                minScore = curScore;
            }
        }
        for(Object obj: storedData) {
            JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
            int curScore = Integer.parseInt(curPlayer.get("score").toString());
            if(curScore == minScore) {
                retData.add(((JSONObject) obj));
            }
        }
        return retData;
    }
}
