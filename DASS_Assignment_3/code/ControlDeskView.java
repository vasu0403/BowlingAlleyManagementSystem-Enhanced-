/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */

/**
 * Class for representation of the control desk
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

public class ControlDeskView implements ActionListener, ControlDeskObserver {

	private final JButton addParty;
	private final JButton finished;
	private final JButton history;
	private final JButton resume;

	private final JFrame win;
	private final JList partyList;
	
	/** The maximum  number of members in a party */
	private final int maxMembers;
	
	private final ControlDesk controlDesk;
	private final PlayingParties playingParties;
	/**
	 * Displays a GUI representation of the ControlDesk
	 *
	 */

	public ControlDeskView(ControlDesk controlDesk, int maxMembers, PlayingParties playingParties) {

		this.controlDesk = controlDesk;
		this.playingParties = playingParties;
		this.maxMembers = maxMembers;
		int numLanes = controlDesk.getNumLanes();

		win = Panels.window("Control Desk");

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		JPanel controlsPanel = Panels.gridPanel("Controls", 4, 1);
		addParty = Panels.button("Add Party", controlsPanel, this);
		finished = Panels.button("Finished", controlsPanel, this);
		history = Panels.button("History", controlsPanel, this);
		resume = Panels.button("Resume", controlsPanel, this);

		JPanel laneStatusPanel = Panels.gridPanel("Lane Status", numLanes, 1);

		HashSet lanes=controlDesk.getLanes();
		Iterator it = lanes.iterator();
		int laneCount=0;
		while (it.hasNext()) {
			Lane curLane = (Lane) it.next();
			LaneStatusView laneStat = new LaneStatusView(curLane,(laneCount+1));
			curLane.subscribe(laneStat);
			((Pinsetter)curLane.getPinsetter()).subscribe(laneStat);
			JPanel lanePanel = laneStat.showLane();
			lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		JPanel partyPanel = Panels.flowPanel("Party Queue");

		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(10);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);
		//		partyPanel.add(partyList);


		// Clean up main panel
		colPanel.add(controlsPanel, "East");
		colPanel.add(laneStatusPanel, "Center");
		colPanel.add(partyPanel, "West");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.setVisible(true);

	}

	/**
	 * Handler for actionEvents
	 *
	 * @param e	the ActionEvent that triggered the handler
	 *
	 */

	@SuppressWarnings("unused")
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addParty)) {
			AddPartyView addPartyWin = new AddPartyView(this, maxMembers);
		}
		if (e.getSource().equals(history)) {
			HistoryView historyView = new HistoryView();
		}
		if (e.getSource().equals(finished)) {
			win.setVisible(false);
			System.exit(0);
		}
		if(e.getSource().equals(resume)) {
			ResumeView resumeView = new ResumeView(this);
		}
	}

	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the AddPartyView that is providing a new party
	 *
	 */

	public void updateAddParty(AddPartyView addPartyView) {
		playingParties.addPartyQueue(addPartyView.getParty());
		controlDesk.publish(new ControlDeskEvent(playingParties.getPartyQueue()));
	}

	public boolean notifyControlDesk(Object obj) {
		boolean found = controlDesk.findLane(obj);
		return found;
	}
	/**
	 * Receive a broadcast from a ControlDesk
	 *
	 * @param ce	the ControlDeskEvent that triggered the handler
	 *
	 */

	public void receiveControlDeskEvent(ControlDeskEvent ce) {
		partyList.setListData(((Vector) ce.getPartyQueue()));
	}
}
