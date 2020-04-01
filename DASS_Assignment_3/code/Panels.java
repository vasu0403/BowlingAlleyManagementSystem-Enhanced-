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
}
