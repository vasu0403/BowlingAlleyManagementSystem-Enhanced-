/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;
import java.text.*;

public class EndGameReport implements ActionListener, ListSelectionListener {

	private JFrame win;
	private JButton printButton, finished;
	private JList memberList;
	private Vector myVector;
	private Vector retVal;

	private int result;

	private String selectedMember;

	public EndGameReport( String partyName, Party party ) {
	
		result =0;
		retVal = new Vector();
		win = Panels.window("End Game Report for " + partyName + "?");

		JPanel colPanel = Panels.gridPanel("", 1, 2);

		// Member Panel
		JPanel partyPanel = Panels.flowPanel("Party Members");
		
		Vector myVector = new Vector();
		Iterator iter = (party.getMembers()).iterator();
		while (iter.hasNext()){
			myVector.add( ((Bowler)iter.next()).getNick() );
		}	
		memberList = new JList(myVector);
		memberList.setFixedCellWidth(120);
		memberList.setVisibleRowCount(5);
		memberList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(memberList);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		partyPanel.add( memberList );

		// Button Panel
		JPanel buttonPanel = Panels.gridPanel("", 2, 1);

		printButton = Panels.button("Print Report", buttonPanel, this);
		finished = Panels.button("Finished", buttonPanel, this);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(buttonPanel);

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(printButton)) {		
			//Add selected to the vector.
			retVal.add(selectedMember);
		}
		if (e.getSource().equals(finished)) {		
			win.hide();
			result = 1;
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		selectedMember =
			((String) ((JList) e.getSource()).getSelectedValue());
	}

	public Vector getResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return retVal;	
	}
	
	public void destroy() {
		win.hide();
	}

}

