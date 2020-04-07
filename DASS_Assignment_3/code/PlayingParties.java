import java.util.*;
public class PlayingParties {
    private Queue partyQueue;
    public PlayingParties() {
        partyQueue = new Queue();
    }

	/**
	 * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
	 *
	 * @param partyNicks	A Vector of NickNames
	 *
	 */
    public void addPartyQueue(Vector partyNicks) {
		Party newParty = new Party(partyNicks);
		getQueue().add(newParty);
	}

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
	 *
     * @return a Vecotr of Strings
     *
     */
	public Vector getPartyQueue() {
		Vector displayPartyQueue = new Vector();
		for ( int i=0; i < ( (Vector)getQueue().asVector()).size(); i++ ) {
			String nextParty = ((Party) getQueue().asVector().get(i)).getFirstMemberNickName() + "'s Party";
			displayPartyQueue.addElement(nextParty);
		}
		return displayPartyQueue;
	}
    public Queue getQueue() { return partyQueue; }
}
