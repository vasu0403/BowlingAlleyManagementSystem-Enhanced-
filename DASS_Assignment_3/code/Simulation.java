import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Simulation {
    public static void endGame(Lane lane, HashMap scores, Party party, int[][] finalScores, int gameNumber) {
        EndGamePrompt egp = new EndGamePrompt( ((Bowler) party.getMembers().get(0)).getNickName() + "'s Party" );
        int result = egp.getResult();
        egp.distroy();
        egp = null;


        System.out.println("result was: " + result);
        // TODO: send record of scores to control desk
        if (result == 1) {					// yes, want to play again
            Scoring.resetScores(lane, scores);
            lane.resetBowlerIterator();

        } else if (result == 2) {// no, dont want to play another game
            Vector printVector;
            EndGameReport egr = new EndGameReport( ((Bowler)party.getMembers().get(0)).getNickName() + "'s Party", party);
            printVector = egr.getResult();
            lane.partyAssigned = false;
            Iterator scoreIt = party.getMembers().iterator();
            party = null;

            lane.publish(lane.lanePublish());

            int myIndex = 0;
            while (scoreIt.hasNext()){
                Bowler thisBowler = (Bowler)scoreIt.next();
                ScoreReport sr = new ScoreReport( thisBowler, finalScores[myIndex++], gameNumber );
//				sr.sendEmail(thisBowler.getEmail());
                Iterator printIt = printVector.iterator();
                while (printIt.hasNext()){
                    if (thisBowler.getNick() == (String)printIt.next()){
                        System.out.println("Printing " + thisBowler.getNick());
                        sr.sendPrintout();
                    }
                }

            }
        }
    }

    public static void continueGame(boolean gameIsPaused, int[][] finalScores, int[][] cumulScores, Pinsetter setter, Lane lane) {


        if (lane.bowlerIterator.hasNext()) {
            lane.currentThrower = (Bowler)lane.bowlerIterator.next();

            lane.canThrowAgain = true;
            lane.tenthFrameStrike = false;
            lane.ball = 0;
            while (lane.canThrowAgain && !gameIsPaused) {
                setter.ballThrown();		// simulate the thrower's ball hiting
                lane.ball++;
            }

            if (lane.frameNumber == 9){
                finalScores[lane.bowlIndex][lane.gameNumber] = cumulScores[lane.bowlIndex][9];
                try{
                    Date date = new Date();
                    String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
                    ScoreHistoryFile.addScore(lane.currentThrower.getNick(), dateString, new Integer(cumulScores[lane.bowlIndex][9]).toString());
                } catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
            }


            setter.reset();
            lane.bowlIndex++;

        } else {
            lane.frameNumber++;
            lane.resetBowlerIterator();
            lane.bowlIndex = 0;
            if (lane.frameNumber > 9) {
                lane.gameFinished = true;
                lane.gameNumber++;
            }
        }
    }

}
