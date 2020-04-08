public class Throws {
    public static void spareThrow(Lane lane, int bowlIndex, int[] curScore, int i) {
        //This ball was a the second of a spare.
        //Also, we're not on the current ball.
        //Add the next ball to the ith one in cumul.
        lane.cumulScores[bowlIndex][(i/2)] += curScore[i+1] + curScore[i];
        if (i > 1) {
            //cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 -1];
        }
    }
    public static boolean strikeThrow(Lane lane, int[] curScore, int bowlIndex, int i) {
        //This ball is the first ball, and was a strike.
        //If we can get 2 balls after it, good add them to cumul.
        int strikeballs = 0;
        if (curScore[i+2] != -1) {
            strikeballs = 1;
            if(curScore[i+3] != -1) {
                //Still got em.
                strikeballs = 2;
            } else if(curScore[i+4] != -1) {
                //Ok, got it.
                strikeballs = 2;
            }
        }
        if (strikeballs == 2){
            //Add up the strike.
            //Add the next two balls to the current cumulscore.
            lane.cumulScores[bowlIndex][i/2] += 10;
            if(curScore[i+1] != -1) {
                System.out.println(i);
                lane.cumulScores[bowlIndex][i/2] += curScore[i+1] + lane.cumulScores[bowlIndex][(i/2)-1];
                if (curScore[i+2] != -1){
                    if( curScore[i+2] != -2){
                        lane.cumulScores[bowlIndex][(i/2)] += curScore[i+2];
                    }
                } else {
                    if( curScore[i+3] != -2){
                        lane.cumulScores[bowlIndex][(i/2)] += curScore[i+3];
                    }
                }
            } else {
                if ( i/2 > 0 ){
                    lane.cumulScores[bowlIndex][i/2] += curScore[i+2] + lane.cumulScores[bowlIndex][(i/2)-1];
                } else {
                    lane.cumulScores[bowlIndex][i/2] += curScore[i+2];
                }
                if (curScore[i+3] != -1){
                    if( curScore[i+3] != -2){
                        lane.cumulScores[bowlIndex][(i/2)] += curScore[i+3];
                    }
                } else {
                    lane.cumulScores[bowlIndex][(i/2)] += curScore[i+4];
                }
            }
        } else {
            return true;
        }
        return false;
    }

    public static void normalThrow(Lane lane, int[] curScore, int bowlIndex, int i) {
        if( i%2 == 0 && i < 18){
            if ( i/2 == 0 ) {
                //First frame, first ball.  Set his cumul score to the first ball
                if(curScore[i] != -2){
                    lane.cumulScores[bowlIndex][i/2] += curScore[i];
                }
            } else if (i/2 != 9){
                //add his last frame's cumul to this ball, make it this frame's cumul.
                if(curScore[i] != -2){
                    lane.cumulScores[bowlIndex][i/2] += lane.cumulScores[bowlIndex][i/2 - 1] + curScore[i];
                } else {
                    lane.cumulScores[bowlIndex][i/2] += lane.cumulScores[bowlIndex][i/2 - 1];
                }
            }
        } else if (i < 18){
            if(curScore[i] != -1 && i > 2){
                if(curScore[i] != -2){
                    lane.cumulScores[bowlIndex][i/2] += curScore[i];
                }
            }
        }
        if (i/2 == 9){
            if (i == 18){
                lane.cumulScores[bowlIndex][9] += lane.cumulScores[bowlIndex][8];
            }
            if(curScore[i] != -2){
                lane.cumulScores[bowlIndex][9] += curScore[i];
            }
        } else if (i/2 == 10) {
            if(curScore[i] != -2){
                lane.cumulScores[bowlIndex][9] += curScore[i];
            }
        }
    }
}
