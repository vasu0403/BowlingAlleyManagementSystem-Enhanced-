import org.json.simple.JSONArray;

public class Queries {

    public static String topPlayer() {
        JSONArray storedData = db.getData();
        System.out.println(storedData);
        String ret = "Searching for top player";
        return ret;
    }

    public static String highestScore() {
        JSONArray storedData = db.getData();
        System.out.println(storedData);
        String ret = "Searching for highest score";
        return ret;
    }

    public static String lowestScore() {
        JSONArray storedData = db.getData();
        System.out.println(storedData);
        String ret = "Searching for lowest score";
        return ret;
    }
}
