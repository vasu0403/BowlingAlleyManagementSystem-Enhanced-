import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;
public class Panels {
    public static JButton addButton(String title, JPanel panel, ActionListener view) {
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
}
