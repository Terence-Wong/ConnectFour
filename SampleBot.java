package com.company;

import java.util.Random;
/**
 * Created by Terence on 2017-06-02.
 */
public class SampleBot extends Player {
    String name = "SampleBot";//your name, MUST HAVE/WATCH
    static Random rand = new Random();
    static Main game;
    int playerType;
    public SampleBot(Main g, int p){//constructor, MUST HAVE
        game = g;
        playerType = p;
        //playerType = whether youre blue(1) or red(2)

    }
    @Override
    public void startTurn(){//driver class calls this, MUST HAVE
        //example stupid algorithm
        //game.checkCell checks a coordinate
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
        * */


        int x = rand.nextInt(7);
        while (game.checkCell(x, 0, playerType) != 0) {
            x++;
            if (x == 7) {
                x = 0;
            }
        }
        game.placePiece(x, playerType);

    }
    @Override
    public String getName(){//used by driver class, MUST HAVE
        return name;
    }
}
