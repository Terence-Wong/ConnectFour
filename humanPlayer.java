import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Terence Wong, MAC CompSci Club(Senior) President
 * June 2017
 */
public class humanPlayer extends Player {
    String name = "Human";
    Main game;
    int playerType;
    int clickx;
    boolean hasTurn = false;
    public humanPlayer(Main g, int p) {
        game = g;
        playerType = p;
        game.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                clickx = (int) Math.floor((e.getX()-game.BOARDXOFFSET)/(game.cellsize+game.padding));
                if(hasTurn){
                    System.out.println(clickx);
                    synchronized (humanPlayer.this) {
                        humanPlayer.this.notifyAll();
                    }
                }
            }
        });
        game.setFocusable(true);
    }
    @Override
    public synchronized void startTurn(){
        hasTurn = true;
        try {
            wait();
        } catch (InterruptedException e) {}
        hasTurn = false;
        game.placePiece(clickx,playerType);
    }
    @Override
    public String getName(){//used by driver class, MUST HAVE
        return name;
    }

}
