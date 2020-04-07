import java.util.HashMap;
import java.util.Iterator;

public class Scoring {
    /** resetScores()
     *
     * resets the scoring mechanism, must be called before scoring starts
     *
     * @pre the party has been assigned
     * @post scoring system is initialized
     */
    public static void resetScores(Lane lane, HashMap scores) {
        Party curParty = lane.party;
        Iterator bowlIt = (curParty.getMembers()).iterator();

        while ( bowlIt.hasNext() ) {
            int[] toPut = new int[25];
            for ( int i = 0; i != 25; i++){
                toPut[i] = -1;
            }
            scores.put( bowlIt.next(), toPut );
        }
        lane.gameFinished = false;
        lane.frameNumber = 0;
    }

    /** markScore()
     *
     * Method that marks a bowlers score on the board.
     *
     * @param Cur		The current bowler
     * @param frame	The frame that bowler is on
     * @param ball		The ball the bowler is on
     * @param score	The bowler's score
     */
    public static void markScore( Bowler Cur, int frame, int ball, int score, Lane lane, int bowlIndex){
        System.out.println(Cur.getNickName());
        int[] curScore;
        int index =  ( (frame - 1) * 2 + ball);

        curScore = (int[]) lane.scores.get(Cur);


        curScore[ index - 1] = score;
        lane.scores.put(Cur, curScore);
        getScore( Cur, frame, lane, bowlIndex);
    }

    /** getScore()
     *
     * Method that calculates a bowlers score
     *
     * @param Cur		The bowler that is currently up
     * @param frame	The frame the current bowler is on
     *
     * @return			The bowlers total score
     */
    private static int getScore( Bowler Cur, int frame, Lane lane, int bowlIndex) {
        int[] curScore;
        int totalScore = 0;
        curScore = (int[]) lane.scores.get(Cur);
        for (int i = 0; i != 10; i++){
            lane.cumulScores[bowlIndex][i] = 0;
        }
        int current = 2*(frame - 1)+lane.ball-1;
        //Iterate through each ball until the current one.
        for (int i = 0; i != current+2; i++){
            if( i%2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1 && i < 19){
                //Spare:
                Throws.spareThrow(lane, bowlIndex, curScore, i);
            } else if( i < current && i%2 == 0 && curScore[i] == 10  && i < 18){
                //This is a strike
                if(Throws.strikeThrow(lane, curScore, bowlIndex, i)) {
                    break;
                };
            }else {
                //We're dealing with a normal throw, add it and be on our way.
                Throws.normalThrow(lane, curScore, bowlIndex, i);
            }
        }
        return totalScore;
    }
}
