import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class HistoryView implements ActionListener{
    private final JFrame win;
    private final JButton mostFrequentPlayer;
    private final JButton highestScore;
    private final JButton lowestScore;
    private TextArea result;

    private void setHighMinScore(String description, JSONArray queryResult) {
        if(queryResult.size() > 0) {
            int maxscore = 0;
            String details = "";
            for(Object obj: queryResult) {
                JSONObject curPlayer = (JSONObject)((JSONObject) obj).get("player");
                maxscore = Integer.parseInt(curPlayer.get("score").toString());
                details += curPlayer.get("nick").toString() + " on " + curPlayer.get("date") + "\n";
            }
            result.setText(description + maxscore + " ...Scored by the following players:\n");
            result.append(details);
        } else {
            result.setText("You need to run sum simulations first :(");
        }
    }
    public HistoryView() {
        win = Panels.window("History");

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        JPanel queryPanel = Panels.gridPanel("Query", 3, 1);
        mostFrequentPlayer = Panels.button("Most Frequent Player", queryPanel, this);
        highestScore = Panels.button("Highest Score", queryPanel, this);
        lowestScore = Panels.button("Lowest Score", queryPanel, this);

        JPanel resultPanel = Panels.flowPanel("Result");
        result = new TextArea("Select a query");
        result.setBounds(10,30, 250,300);
        result.setEditable(false);
        resultPanel.add(result);

        colPanel.add(queryPanel, "Center");
        colPanel.add(resultPanel, "East");

        Panels.showWin(win, colPanel);
    }
    public void actionPerformed( ActionEvent e ) {
        if (e.getSource().equals(mostFrequentPlayer)) {
            Vector<String> queryResult;
            queryResult = Queries.mostFrequentPlayer();
            if(queryResult.size() == 0) {
                result.setText("You need to run some simulations first :(");
            } {
                result.setText("Following are the most frequent players:\n");
                for(String nicks: queryResult) {
                    result.append(nicks + "\n");
                }
            }
        }
        if (e.getSource().equals(highestScore)) {
            JSONArray queryResult;
            queryResult = Queries.highestScore();
            setHighMinScore("Max Score: ", queryResult);
        }
        if (e.getSource().equals(lowestScore)) {
            JSONArray queryResult;
            queryResult = Queries.lowestScore();
            setHighMinScore("Min Score: ", queryResult);

        }
    }
}
