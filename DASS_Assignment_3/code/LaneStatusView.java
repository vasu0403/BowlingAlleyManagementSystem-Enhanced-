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

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

	private final JPanel jp;

	private final JLabel curBowler;
	private final JLabel pinsDown;
	private final JButton viewLane;
	private final JButton viewPinSetter;
	private final JButton maintenance;

	private final PinSetterView psv;
	private final LaneView lv;
	private final Lane lane;

	boolean laneShowing;
	boolean psShowing;
	public LaneStatusView(Lane lane, int laneNum ) {
		this.lane = lane;

		laneShowing=false;
		psShowing=false;

		psv = new PinSetterView( laneNum );
		Pinsetter ps = lane.getPinsetter();
		ps.subscribe(psv);

		lv = new LaneView( lane, laneNum );
		lane.subscribe(lv);

		jp = Panels.flowPanel("");
		JLabel cLabel = new JLabel( "Now Bowling: " );
		curBowler = new JLabel( "(no one)" );
		JLabel foul = new JLabel(" ");
		JLabel pdLabel = new JLabel( "Pins Down: " );
		pinsDown = new JLabel( "0" );

		// Button Panel
		JPanel buttonPanel = Panels.flowPanel("");

		viewLane = Panels.button("View Lane", buttonPanel, this);
		viewPinSetter = Panels.button("Pinsetter", buttonPanel, this);
		maintenance = Panels.button("     ", buttonPanel, this);
		maintenance.setBackground( Color.GREEN );

		viewLane.setEnabled( false );
		viewPinSetter.setEnabled( false );

		jp.add( cLabel );
		jp.add( curBowler );
		jp.add( pdLabel );
		jp.add( pinsDown );
		
		jp.add(buttonPanel);

	}

	public JPanel showLane() {
		return jp;
	}

	public void actionPerformed( ActionEvent e ) {
		if ( lane.isPartyAssigned() ) {
			if (e.getSource().equals(viewPinSetter)) {
				if (psShowing == false) {
					psv.show();
					psShowing = true;
				} else {
					psv.hide();
					psShowing = false;
				}
			}
			if (e.getSource().equals(viewLane)) {
				if (laneShowing == false) {
					lv.show();
					laneShowing = true;
				} else {
					lv.hide();
					laneShowing = false;
				}
			}
			if (e.getSource().equals(maintenance)) {
				lane.stopMaintenance();
				maintenance.setBackground(Color.GREEN);
			}
		}

	}

	public void receiveLaneEvent(LaneEvent le) {
		if(le.getMakeFrameAgain() == true) {
			return;
		}
		curBowler.setText( ( (Bowler)le.getBowler()).getNickName() );
		if ( le.isMechanicalProblem() ) {
			maintenance.setBackground( Color.RED );
		}	
		if ( lane.isPartyAssigned() == false ) {
			pinsDown.setText("0");
			curBowler.setText("(no one)");
			viewLane.setEnabled( false );
			viewPinSetter.setEnabled( false );
		} else {
			viewLane.setEnabled( true );
			viewPinSetter.setEnabled( true );
		}
	}

	public void receivePinsetterEvent(PinsetterEvent pe) {
		pinsDown.setText((Integer.valueOf(pe.totalPinsDown())).toString() );
//		foul.setText( ( new Boolean(pe.isFoulCommited()) ).toString() );
		
	}

}
