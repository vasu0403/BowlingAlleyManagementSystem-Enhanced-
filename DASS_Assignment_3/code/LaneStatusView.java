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

	private JPanel jp;

	private JLabel curBowler, foul, pinsDown;
	private JButton viewLane;
	private JButton viewPinSetter, maintenance;

	private PinSetterView psv;
	private LaneView lv;
	private Lane lane;
	int laneNum;

	boolean laneShowing;
	boolean psShowing;

//	private void handlePinSetter() {
//		if ( psShowing == false ) {
//			psv.show();
//			psShowing=true;
//		} else if ( psShowing == true ) {
//			psv.hide();
//			psShowing=false;
//		}
//	}
//
//	private void handleLane() {
//		if ( laneShowing == false ) {
//			lv.show();
//			laneShowing=true;
//		} else if ( laneShowing == true ) {
//			lv.hide();
//			laneShowing=false;
//		}
//	}
//
//	private void handleMaintenance() {
//		lane.unPauseGame();
//		maintenance.setBackground( Color.GREEN );
//	}
	public LaneStatusView(Lane lane, int laneNum ) {

		this.lane = lane;
		this.laneNum = laneNum;

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
		foul = new JLabel( " " );
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
				} else if (psShowing == true) {
					psv.hide();
					psShowing = false;
				}
			}
			if (e.getSource().equals(viewLane)) {
				if (laneShowing == false) {
					lv.show();
					laneShowing = true;
				} else if (laneShowing == true) {
					lv.hide();
					laneShowing = false;
				}
			}
			if (e.getSource().equals(maintenance)) {
				lane.unPauseGame();
				maintenance.setBackground(Color.GREEN);
			}
		}
	}

	public void receiveLaneEvent(LaneEvent le) {
		curBowler.setText( ( (Bowler)le.getBowler()).getNickName() );
		if ( le.isMechanicalProblem() ) {
			maintenance.setBackground( Color.RED );
		}	
		if ( lane.isPartyAssigned() == false ) {
			viewLane.setEnabled( false );
			viewPinSetter.setEnabled( false );
		} else {
			viewLane.setEnabled( true );
			viewPinSetter.setEnabled( true );
		}
	}

	public void receivePinsetterEvent(PinsetterEvent pe) {
		pinsDown.setText( ( new Integer(pe.totalPinsDown()) ).toString() );
//		foul.setText( ( new Boolean(pe.isFoulCommited()) ).toString() );
		
	}

}
