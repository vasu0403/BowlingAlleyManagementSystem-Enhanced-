/* ControlDesk.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: ControlDesk.java,v $
 * 		Revision 1.13  2003/02/02 23:26:32  ???
 * 		ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 * 		
 * 		Revision 1.12  2003/02/02 20:46:13  ???
 * 		Added " 's Party" to party names.
 * 		
 * 		Revision 1.11  2003/02/02 20:43:25  ???
 * 		misc cleanup
 * 		
 * 		Revision 1.10  2003/02/02 17:49:10  ???
 * 		Fixed problem in getPartyQueue that was returning the first element as every element.
 * 		
 * 		Revision 1.9  2003/02/02 17:39:48  ???
 * 		Added accessor for lanes.
 * 		
 * 		Revision 1.8  2003/02/02 16:53:59  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.7  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 		Revision 1.6  2003/02/02 06:09:39  ???
 * 		Updated many classes to support the ControlDeskView.
 * 		
 * 		Revision 1.5  2003/01/26 23:16:10  ???
 * 		Improved thread handeling in lane/controldesk
 * 		
 * 
 */

/**
 * Class that represents control desk
 *
 */

import java.util.*;

class ControlDesk extends Thread {

	/** The collection of Lanes */
	private final HashSet lanes;

	private final PlayingParties playingParties;

	/** The number of lanes represented */
	private final int numLanes;

	/** The collection of subscribers */
	private final Vector subscribers;

    /**
     * Constructor for the ControlDesk class
     *
     * @param numlanes	the numbler of lanes to be represented
     *
     */

	public ControlDesk(int numLanes, PlayingParties playingParties) {
		this.numLanes = numLanes;
		subscribers = new Vector();
		lanes = new HashSet(numLanes);
		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane());
		}
		this.playingParties = playingParties;
		this.start();

	}
	
	/**
	 * Main loop for ControlDesk's thread
	 * 
	 */
	public void run() {
		while (true) {
			
			assignLane(false, null);
			
			try {
				sleep(250);
			} catch (Exception e) {}
		}
	}


    /**
     * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
     *
     */
	public boolean assignLane(boolean resumeGame, Object obj) {
		Iterator it = getLanes().iterator();

		while (it.hasNext()) {
			Lane curLane = (Lane) it.next();

			if (curLane.isPartyAssigned() == false) {
				if(resumeGame == false) {
					if(playingParties.getQueue().hasMoreElements()) {
						System.out.println("ok... assigning this party");
						curLane.assignParty(((Party) playingParties.getQueue().next()));
					}
				} else {
					curLane.resumeSavedGame(obj);
					return true;
				}
			}
		}
		publish(new ControlDeskEvent(playingParties.getPartyQueue()));
		return false;
	}

//	public boolean findLane(Object obj) {
//		Iterator it = getLanes().iterator();
//
//		while (it.hasNext()) {
//			Lane curLane = (Lane) it.next();
//			if(curLane.isPartyAssigned() == false) {
//				System.out.println("ok.....resuming game");
//				curLane.resumeSavedGame(obj);
//				return true;
//			}
//		}
//		return false;
//	}
    /**
     */



    /**
     * Accessor for the number of lanes represented by the ControlDesk
     * 
     * @return an int containing the number of lanes represented
     *
     */

	public int getNumLanes() {
		return numLanes;
	}

    /**
     * Allows objects to subscribe as observers
     * 
     * @param adding	the ControlDeskObserver that will be subscribed
     *
     */

	public void subscribe(ControlDeskObserver adding) {
		subscribers.add(adding);
	}

    /**
     * Broadcast an event to subscribing objects.
     * 
     * @param event	the ControlDeskEvent to broadcast
     *
     */

	public void publish(ControlDeskEvent event) {
		Iterator eventIterator = subscribers.iterator();
		while (eventIterator.hasNext()) {
			(
				(ControlDeskObserver) eventIterator
					.next())
					.receiveControlDeskEvent(
				event);
		}
	}

    /**
     * Accessor method for lanes
     * 
     * @return a HashSet of Lanes
     *
     */

	public HashSet getLanes() {
		return lanes;
	}
}
