/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 * 		
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 * 		
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

	private final int maxSize;

	private final JFrame win;
	private final JButton addPatron;
	private final JButton newPatron;
	private final JButton remPatron;
	private final JButton finished;
	private final JList partyList;
	private final JList allBowlers;
	private final Vector party;
	private Vector bowlerdb;
	private Integer lock;

	private final ControlDeskView controlDesk;

	private String selectedNick, selectedMember;

	public AddPartyView(ControlDeskView controlDesk, int max) {

		this.controlDesk = controlDesk;
		maxSize = max;

		win = Panels.window("Add Party");

		JPanel colPanel = Panels.gridPanel("", 1, 3);

		// Party Panel
		JPanel partyPanel = Panels.flowPanel("Your Party");

		party = new Vector();
		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = Panels.flowPanel("Bowler Database");

		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e) {
			System.err.println("File Error");
			bowlerdb = new Vector();
		}
		allBowlers = new JList(bowlerdb);
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = Panels.gridPanel("", 4, 1);

		addPatron = Panels.button("Add to Party", buttonPanel, this);
		remPatron = Panels.button("Remove Member", buttonPanel, this);
		newPatron = Panels.button("New Patron", buttonPanel, this);
		finished = Panels.button("Finished", buttonPanel, this);


		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);

		Panels.showWin(win, colPanel);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPatron)) {
			if (selectedNick != null && party.size() < maxSize) {
				if (party.contains(selectedNick)) {
					System.err.println("Member already in Party");
				} else {
					party.add(selectedNick);
					partyList.setListData(party);
				}
			}
		}
		if (e.getSource().equals(remPatron)) {
			if (selectedMember != null) {
				party.removeElement(selectedMember);
				partyList.setListData(party);
			}
		}
		if (e.getSource().equals(newPatron)) {
			NewPatronView newPatron = new NewPatronView( this );
		}
		if (e.getSource().equals(finished)) {
			if ( party != null && party.size() > 0) {
				controlDesk.updateAddParty( this );

			}
			win.setVisible(false);
		}

	}

/**
 * Handler for List actions
 * @param e the ListActionEvent that triggered the handler
 */

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick =
				((String) ((JList) e.getSource()).getSelectedValue());
		}
		if (e.getSource().equals(partyList)) {
			selectedMember =
				((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

/**
 * Accessor for Party
 */

	public Vector getNames() {
		return party;
	}

/**
 * Called by NewPatronView to notify AddPartyView to update
 * 
 * @param newPatron the NewPatronView that called this method
 */

	public void updateNewPatron(NewPatronView newPatron) {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNick() );
			if ( checkBowler == null ) {
				BowlerFile.putBowlerInfo(
					newPatron.getNick(),
					newPatron.getFull(),
					newPatron.getEmail());
				bowlerdb = new Vector(BowlerFile.getBowlers());
				allBowlers.setListData(bowlerdb);
				party.add(newPatron.getNick());
				partyList.setListData(party);
			} else {
				System.err.println( "A Bowler with that name already exists." );
			}
		} catch (Exception e2) {
			System.err.println("File I/O Error");
		}
	}

/**
 * Accessor for Party
 */

	public Vector getParty() {
		return party;
	}

}
