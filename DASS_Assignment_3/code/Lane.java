
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 * 
 */

import org.json.simple.JSONObject;

import java.util.*;

public class Lane extends Thread implements PinsetterObserver {	
	public Party party;
	private Pinsetter setter;
	public HashMap scores;
	private Vector subscribers;

	private boolean gameIsHalted;
	private boolean gameIsPaused;

	public boolean partyAssigned;
	public boolean gameFinished;
	public Iterator bowlerIterator;																// might change this back
	public int ball;
	public int bowlIndex;
	public int frameNumber;
	public boolean tenthFrameStrike;															// might change this back

	private int[] curScores;
	public int[][] cumulScores;
	public boolean canThrowAgain;
	
	private int[][] finalScores;
	public int gameNumber;
	
	public Bowler currentThrower;			// = the thrower who just took a throw              // might change this back
	private Boolean makeFrameAgain;
	private String pausedParty;
	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane() {
		pausedParty = "";
		setter = new Pinsetter();
		scores = new HashMap();
		subscribers = new Vector();

		gameIsHalted = false;
		partyAssigned = false;
		makeFrameAgain = false;
		gameNumber = 0;

		setter.subscribe( this );

		this.start();
	}

	/** run()
	 *
	 * entry point for execution of this lane
	 */
	public void run() {
		
		while (true) {
			if (isPartyAssigned() && !gameFinished) {
				// we have a party on this lane,
				// so next bower can take a throw
				while (gameIsHalted || gameIsPaused) {
					try {
						sleep(10);
					} catch (Exception e) {}
				}
				Simulation.continueGame(gameIsPaused, finalScores, cumulScores, setter, this);
//				continueGame();
			} else if (isPartyAssigned() && gameFinished) {
				Simulation.endGame(this, scores, party, finalScores, gameNumber);
			}

			try {
				sleep(10);
			} catch (Exception e) {}
		}
	}
	
	/** recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 * 
	 * @param pe 		The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent pe) {
		
			if (pe.pinsDownOnThisThrow() >=  0) {			// this is a real throw
//				markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());
				Scoring.markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow(), this, bowlIndex);
				publish( lanePublish() );

				// next logic handles the ?: what conditions dont allow them another throw?
				// handle the case of 10th frame first
				if (frameNumber == 9) {
					if (pe.totalPinsDown() == 10) {
						setter.resetPins();
						if(pe.getThrowNumber() == 1) {
							tenthFrameStrike = true;
						}
					}
				
					if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && tenthFrameStrike == false)) {
						canThrowAgain = false;
						//publish( lanePublish() );
					}
				
					if (pe.getThrowNumber() == 3) {
						canThrowAgain = false;
						//publish( lanePublish() );
					}
				} else { // its not the 10th frame
			
					if (pe.pinsDownOnThisThrow() == 10) {		// threw a strike
						canThrowAgain = false;
						//publish( lanePublish() );
					} else if (pe.getThrowNumber() == 2) {
						canThrowAgain = false;
						//publish( lanePublish() );
					}
				}
			}
	}
	
	/** resetBowlerIterator()
	 * 
	 * sets the current bower iterator back to the first bowler
	 * 
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	public void resetBowlerIterator() {
		bowlerIterator = (party.getMembers()).iterator();
	}

	/** assignParty()
	 * 
	 * assigns a party to this lane
	 * 
	 * @pre none
	 * @post the party has been assigned to the lane
	 * 
	 * @param theParty		Party to be assigned
	 */
	private void initFields() {
		resetBowlerIterator();

		curScores = new int[party.getMembers().size()];
		cumulScores = new int[party.getMembers().size()][10];
		finalScores = new int[party.getMembers().size()][128]; //Hardcoding a max of 128 games, bite me.
		gameNumber = 0;
		bowlIndex = 0;

		Scoring.resetScores(this, scores);
	}
	public void assignParty( Party theParty ) {
		party = theParty;
		initFields();
		partyAssigned = true;
	}
	private String[] convertStringToArray(String conv) {
		int len = conv.length();
		conv = conv.substring(1, len - 1);
		return conv.split(", ");
	}
	public void resumeSavedGame(Object obj) {

		JSONObject game = (JSONObject)((JSONObject) obj).get("game");

		String partyString = (String) game.get("party");
		String[] partyArray = convertStringToArray(partyString);
		Vector<String> partyVector = new Vector<String>(Arrays.asList(partyArray));
		party = new Party(partyVector);
		initFields();

		JSONObject scoresobj = (JSONObject) game.get("score");

		Iterator bowlIt = (party.getMembers()).iterator();
		while ( bowlIt.hasNext() ) {
			Bowler curBowler = (Bowler) bowlIt.next();
			String bowlerNick = curBowler.getNick();
			String[] curScores = convertStringToArray((String) scoresobj.get(bowlerNick));
			int[] array = Arrays.stream(curScores).mapToInt(Integer::parseInt).toArray();
			scores.put(curBowler, array);
		}
		frameNumber =  ((Long) game.get("frameNumber")).intValue();
		partyAssigned = true;
		makeFrameAgain = true;
		publish(lanePublish());
		makeFrameAgain = false;

	}
	/** lanePublish()
	 *
	 * Method that creates and returns a newly created laneEvent
	 * 
	 * @return		The new lane event
	 */
	public LaneEvent lanePublish(  ) {

		LaneEvent laneEvent = new LaneEvent(party, bowlIndex, currentThrower, cumulScores, scores, frameNumber+1, ball, gameIsHalted, makeFrameAgain);
		return laneEvent;
	}

	/** isPartyAssigned()
	 * 
	 * checks if a party is assigned to this lane
	 * 
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	/** subscribe
	 * 
	 * Method that will add a subscriber
	 * 
	 * @param subscribe	Observer that is to be added
	 */

	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}

	/** publish
	 *
	 * Method that publishes an event to subscribers
	 * 
	 * @param event	Event that is to be published
	 */

	public void publish( LaneEvent event ) {
		if( subscribers.size() > 0 ) {
			Iterator eventIterator = subscribers.iterator();
			
			while ( eventIterator.hasNext() ) {
				( (LaneObserver) eventIterator.next()).receiveLaneEvent( event );
			}
		}
	}

	/**
	 * Accessor to get this Lane's pinsetter
	 * 
	 * @return		A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;	
	}

	/**
	 * Pause the execution of this game
	 */
	public void maintenance() {
		gameIsHalted = true;
		publish(lanePublish());
	}

	/**
	 * Resume the execution of this game
	 */
	public void stopMaintenance() {
		gameIsHalted = false;
		publish(lanePublish());
	}
	public void pauseGame() {
		gameIsPaused = true;
		try {
			Thread.sleep(40);
		} catch ( InterruptedException e ) {
			System.err.println( "Interrupted" );
		}
		pausedParty = PauseDB.add(party, cumulScores, gameNumber, bowlIndex, frameNumber, scores, ball);
	}
	public void resumeGame() {
		PauseDB.remData(pausedParty);
		gameIsPaused = false;
	}
}
