import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HistoryView implements ActionListener{
    private final JFrame win;
    private final JButton topPlayer;
    private final JButton highestScore;
    private final JButton lowestScore;
    private TextArea result;

    public HistoryView() {
        win = Panels.window("History");

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        JPanel queryPanel = Panels.gridPanel("Query", 3, 1);
        topPlayer = Panels.button("Top Player", queryPanel, this);
        highestScore = Panels.button("Highest Score", queryPanel, this);
        lowestScore = Panels.button("Lowest Score", queryPanel, this);

        JPanel resultPanel = Panels.flowPanel("Result");
        result = new TextArea("Select a query");
        result.setBounds(10,30, 300,300);
        result.setEditable(false);
        resultPanel.add(result);

        colPanel.add(queryPanel, "Center");
        colPanel.add(resultPanel, "East");

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }
    public void actionPerformed( ActionEvent e ) {
        String queryResult;
        if (e.getSource().equals(topPlayer)) {
            queryResult = Queries.topPlayer();
            result.setText(queryResult);

        }
        if (e.getSource().equals(highestScore)) {
            queryResult = Queries.highestScore();
            result.setText(queryResult);

        }
        if (e.getSource().equals(lowestScore)) {
            queryResult = Queries.lowestScore();
            result.setText(queryResult);

        }
    }
}
