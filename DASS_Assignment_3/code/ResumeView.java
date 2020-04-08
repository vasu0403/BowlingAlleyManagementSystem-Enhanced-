import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ResumeView implements ActionListener, ListSelectionListener {

    private final JButton resume;
    private final JFrame win;
    private Vector partydb;
    private final JList allparties;
    private String selectedParty;
    private ControlDeskView controlDeskView;

    public ResumeView(ControlDeskView controlDeskView) {
        this.controlDeskView = controlDeskView;
        win = Panels.window("Resume Game");

        JPanel colPanel = Panels.gridPanel("", 1, 2);

        JPanel savedPanel = Panels.flowPanel("Paused Games");

        try {
            partydb = new Vector(PauseDB.get());
        } catch (Exception e) {
            System.err.println("File Error");
            partydb = new Vector();
        }
        allparties = new JList(partydb);
        allparties.setVisibleRowCount(8);
        allparties.setFixedCellWidth(350);
        JScrollPane savedPane = new JScrollPane(allparties);
        savedPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allparties.addListSelectionListener(this);
        savedPanel.add(savedPane);

        JPanel buttonPanel = Panels.gridPanel("", 1, 1);
        resume = Panels.button("Resume Game", buttonPanel, this);

        colPanel.add(savedPanel);
        colPanel.add(buttonPanel);

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(resume)) {
            if(selectedParty != null) {
                PauseDB.resumeGame(selectedParty, controlDeskView);
            }

        }
    }
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allparties)) {
            selectedParty = ((String) ((JList) e.getSource()).getSelectedValue());
        }
    }
}
