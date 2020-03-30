/*
 * Party.java
 *
 * Version:
 *   $Id$
 *
 * Revisions:
 *   $Log: Party.java,v $
 *   Revision 1.3  2003/02/09 21:21:31  ???
 *   Added lots of comments
 *
 *   Revision 1.2  2003/01/12 22:23:32  ???
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/01/12 19:09:12  ???
 *   Adding Party, Lane, Bowler, and Alley.
 *
 */

/**
 *  Container that holds bowlers
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Party {

	/** Vector of bowlers in this party */	
    private Vector myBowlers;

	/**
	 * Retrieves a matching Bowler from the bowler database.
	 *
	 * @param nickName	The NickName of the Bowler
	 *
	 * @return a Bowler object.
	 *
	 */

	private Bowler registerPatron(String nickName) {
		Bowler patron = null;

		try {
			// only one patron / nick.... no dupes, no checks

			patron = BowlerFile.getBowlerInfo(nickName);

		} catch (FileNotFoundException e) {
			System.err.println("Error..." + e);
		} catch (IOException e) {
			System.err.println("Error..." + e);
		}

		return patron;
	}

	/**
	 * Constructor for a Party
	 *
	 * @param bowlers	Vector of bowlers that are in this party
	 */

    public Party( Vector partyNicks ) {
		Vector partyBowlers = new Vector();
		for (int i = 0; i < partyNicks.size(); i++) {
			Bowler newBowler = registerPatron(((String) partyNicks.get(i)));
			partyBowlers.add(newBowler);
		}
		myBowlers = new Vector(partyBowlers);
    }

	/**
	 * Accessor for members in this party
	 *
	 * @return 	A vector of the bowlers in this party
	 */

    public Vector getMembers() {
		return myBowlers;
    }

	public String getFirstMemberNickName() {
		String firstMemberNickName = ((Bowler)((Vector) this.getMembers()).get(0)).getNickName();
		return firstMemberNickName;
	}
}
