import java.util.Vector;
import java.io.*;

public class drive {

	public static void main(String[] args) {

		int numLanes = 3;
		int maxPatronsPerParty=5;

		PlayingParties playingParties = new PlayingParties();
		Alley a = new Alley( numLanes, playingParties );
		ControlDesk controlDesk = a.getControlDesk();

		ControlDeskView cdv = new ControlDeskView( controlDesk, maxPatronsPerParty, playingParties);
		controlDesk.subscribe( cdv );
	}
}
