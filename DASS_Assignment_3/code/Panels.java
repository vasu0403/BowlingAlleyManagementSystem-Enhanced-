import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Panels {
    public static JButton button(String title, JPanel panel, ActionListener view) {
        JButton button = new JButton(title);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        button.addActionListener(view);
        buttonPanel.add(button);
        panel.add(buttonPanel);
        return button;
    }

    public static JPanel gridPanel(String title, int rows, int cols) {
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridLayout(rows, cols));
        newPanel.setBorder(new TitledBorder(title));
        return newPanel;
    }

    public static JFrame window(String title) {
        JFrame win = new JFrame(title);
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);
        return win;
    }

    public static JPanel flowPanel(String title) {
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout());
        newPanel.setBorder(new TitledBorder(title));
        return newPanel;
    }

    public static JTextField textField(String title, JPanel panel ,int colSize) {
        JTextField retField;
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout());
        JLabel newLabel = new JLabel(title);
        retField = new JTextField("",  colSize);
        newPanel.add(newLabel);
        newPanel.add(retField);
        panel.add(newPanel);
        return retField;
    }

    public static void showWin(JFrame win, JPanel colPanel) {
        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }
}
