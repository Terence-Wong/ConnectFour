/**
 * Created by YOU!
 */
public class NothingBot extends Player {
    String name = "NothingBot";//your name, MUST HAVE/WATCH
    static Main game;
    int playerType;
    public NothingBot(Main g, int p){//constructor method
        game = g;
        playerType = p;
        //playerType = whether youre blue(1) or red(2)
    }
    @Override
    public void startTurn(){//driver class calls this everyturn
       /*we use
        * 0 -to represent empty cell
        * 1 -to represent com.company.Player 1's piece
        * 2 -to represent com.company.Player 2's piece
        *
        * -1 represents error code (e.g. checkCell(-1,520) = -1)
        *
        * coordinates are like
        *
        *   0 1 2 3 4 5 6
        * 0[][][][][][][]
        * 1[][][][][][][]
        * 2[][][][][][][]
        * 3[][][][][][][]
        * 4[][][][][][][]
        * 5[][][][][][][]
        *
        *
        * if your program places a piece at an index that
		* is out of the board, or in a column that is 
		* already full, you will be disqualified!
		*/
		int x = game.checkCell(/*column index*/, /*row index*/, playerType); 
		// x = 1 for player 1, 2 for player 2, 0 for empty
        game.placePiece(/*your column index here*/, playerType);
    }
    @Override
    public String getName(){//used by driver class, MUST HAVE
        return name;
    }
}
