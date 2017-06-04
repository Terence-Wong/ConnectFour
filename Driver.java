package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Main extends JPanel{
    //window settings
    static JFrame frame = new JFrame("(AI) Connect Four");
    static int columns = 7, rows = 6;
    static int WIDTH = 1000, HEIGHT = 650;
    static Color p1Col = Color.blue, p2Col = Color.red, boardCol = Color.orange,backCol = Color.white,winCol = Color.green;
    static int cellsize = 70;//radius of piece
    static int padding = 15;//space between pieces
    static final int BOARDXOFFSET = 200, BOARDYOFFSET = 40;
    //game settings
    static int sleepTime = 50;
    //game variables
    static Player p1;
    static Player p2;
    static int boardw = columns * (cellsize+padding) + padding;
    static int boardh = rows * (cellsize+padding) + padding;
    static int[][] board = new int[rows][columns];//7 columns 6 rows,remember arrays are 0-based!   //
    static boolean gameOver = false;    //
    static boolean draw = false;    //
    static int swx,swy,fwx,fwy; //coords for winning combo  //
    /*we use
    * 0 -to represent empty cell
    * 1 -to represent com.company.Player 1's piece
    * 2 -to represent com.company.Player 2's piece
    *
    * -1 represents error code (e.g. checkCell(-1,520) = -1)
    * */
    static boolean p1turn = true;
    static String p1Name;
    static String p2Name;
    //variables for animation
    boolean checkedCell = false;
    int checkX = 0;
    int checkY = 0;

    public void Main(){

    }
    @Override
    public void paint(Graphics g){
        super.paint(g);//clears screen
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        //draw the game board, with the cell types
        g.setColor(backCol);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(boardCol);
        g.fillRect(BOARDXOFFSET,BOARDYOFFSET,boardw,boardh);
        for(int y = 0; y < rows; y++){
            for(int x = 0; x < columns; x++){
                switch(board[y][x]){
                    case 0:
                        g.setColor(backCol);
                        break;
                    case 1:
                        g.setColor(p1Col);
                        break;
                    case 2:
                        g.setColor(p2Col);
                        break;
                }
                g.fillOval(BOARDXOFFSET + padding + x *(padding+cellsize),
                        BOARDYOFFSET + padding + y *(padding+cellsize),cellsize,cellsize);
            }
        }
        if(checkedCell){
            if(p1turn){
                g.setColor(p1Col);
            }else{
                g.setColor(p2Col);
            }
            g.drawOval(BOARDXOFFSET + checkX *(padding+cellsize),
                    BOARDYOFFSET + checkY *(padding+cellsize),cellsize+ padding*2,cellsize+ padding*2);
        }
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        if(!p1turn){
            g.setColor(p1Col);
            g.drawString(p1Name, 70, 100);
            g.setColor(Color.black);
            g.drawString(p2Name, 850, 100);
        }else{
            g.setColor(Color.black);
            g.drawString(p1Name, 70, 100);
            g.setColor(p2Col);
            g.drawString(p2Name, 850, 100);
        }
        if(gameOver && !draw){
            g.setColor(winCol);
            int xdir = (fwx - swx)/3;
            int ydir = (fwy - swy)/3;
            for(int i = 0; i < 4; i++){
                g.drawOval(BOARDXOFFSET + padding + (swx + (i*xdir)) *(padding+cellsize),
                        BOARDYOFFSET + padding + (swy + (i*ydir)) *(padding+cellsize),cellsize,cellsize);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        frame.setSize(WIDTH,HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main project = new Main();
        frame.add(project);
        frame.repaint();

        p1 = new humanPlayer(project,1);
        //p1 = new SampleBot(project,1);
        //p2 = new SampleBot(project,2);
        p2 = new humanPlayer(project,2);
        p1Name = p1.getName();
        p2Name = p2.getName();
        //start the game
        startGame();
    }

    public static void startGame() throws InterruptedException{
        //start the game
        do{
            if(p1turn){
                p1.startTurn();
            }else {
                p2.startTurn();
            }
            p1turn = !p1turn;
        }while(!checkGameOver());
        frame.repaint();
    }

    ///output data to client method
    public int checkCell(int x, int y, int player) throws InterruptedException {
        if(x < 0 || x >= columns){
            disqualified(player);
            return -1;//error code
        }else if(y < 0 || y >= rows){
            disqualified(player);
            return -1;//error code
        }
        //show algorithm 'thinking' animation
        checkedCell = true;
        checkX = x;
        checkY = y;
        frame.repaint();
        Thread.sleep(sleepTime);
        checkedCell = false;
        frame.repaint();
        //send the actual data
        return board[y][x];
    }
    //input data from client method
    public void placePiece(int x,int player){
        //manual 'catch' of bad data
        if(x < 0 || x >= columns){//x is out of board bounds
            disqualified(player);
        }else{
            for(int y = rows -1; y >= 0; y--){
                if(board[y][x] == 0){
                    board[y][x] = player;
                    frame.repaint();
                    try{
                        Thread.sleep(sleepTime);
                    }catch(InterruptedException e){

                    }
                    setGameOver(x,y,player);
                    return;
                }
            }
            disqualified(player);//column already full of pieces
            return;
        }
    }

    /// Game rule methods
    public static void disqualified(int badPlayer){
        //players AI tried to make an illegal move
        if(badPlayer == 1){
            gameOverBox(4);
        }else{
            gameOverBox(3);
        }
        gameOver = true;
    }
    public static boolean checkGameOver(){
        if (gameOver){
            return true;
        }
        return false;
    }
    public static void setGameOver(int x, int y, int player) {//coords for last piece placed
        if(checkWon(x,y,player)) {//p1
            frame.repaint();
            gameOver = true;
            gameOverBox(player);
            return;
        }
        //check filled
        boolean result = true;
        out:
        for(int yy = 0; yy < rows; yy++){
            for(int xx = 0; xx < columns; xx++){
                if(board[yy][xx] == 0){
                    result = false;
                    break out;
                }
            }
        }
        if(result){//draw
            gameOverBox(0);
            draw = true;
            gameOver = true;
        }
        return;
    }
    public static boolean checkWon(int cx, int cy, int player){
        //check horizontal
        int c = 0;
        for(int x = 0; x < columns; x++){
            if(board[cy][x] == player){
                if(c == 0){
                    swx = x;
                    swy = cy;
                }
                c++;
                if(c == 4){
                    fwx = x;
                    fwy = cy;
                    return true;
                }
            }else{
                c = 0;
            }
        }
        //check vertical
        c = 0;
        for(int y = 0; y < rows; y++){
            if(board[y][cx] == player){
                if(c == 0){
                    swx = cx;
                    swy = y;
                }
                c++;
                if(c == 4){
                    fwx = cx;
                    fwy = y;
                    return true;
                }
            }else{
                c = 0;
            }
        }
        //check left diagonal //HARDCODED VALUES
        c = 0;
        if(cx > cy){
            int tx = cx - cy;//tx represents x where y = 0, top of diagonal
            for(int i = 0; i + tx < columns; i++){
                if(board[i][tx + i] == player){
                    if(c == 0){
                        swx = tx + i;
                        swy = i;
                    }
                    c++;
                    if(c == 4){
                        fwx = tx + i;
                        fwy = i;
                        return true;
                    }
                }else{
                    c = 0;
                }
            }
        }else{
            int ty = cy - cx;//tx represents x where y = 0, top of diagonal
            for(int i = 0; i + ty < rows; i++){
                if(board[ty + i][i] == player){
                    if(c == 0){
                        swx = i;
                        swy = ty + i;
                    }
                    c++;
                    if(c == 4){
                        fwx = i;
                        fwy = ty + i;
                        return true;
                    }
                }else{
                    c = 0;
                }
            }
        }
        //check right diagonal //HARDCODED VALUES
        c = 0;
        if(cx + cy < 6){
            int ty = cx + cy;
            for(int i = 0; i < ty + 1; i++){
                if(board[ty - i][i] == player){
                    if(c == 0){
                        swx = i;
                        swy = ty - i;
                    }
                    c++;
                    if(c == 4){
                        fwx = i;
                        fwy = ty - i;
                        return true;
                    }
                }else{
                    c = 0;
                }
            }
        }else{
            int tx = cx + cy - rows + 1;
            for(int i = 0; i < columns - tx; i++){
                if(board[rows - 1 - i][tx + i] == player){
                    if(c == 0){
                        swx = tx + i;
                        swy = rows - 1 - i;
                    }
                    c++;
                    if(c == 4){
                        fwx = tx + i;
                        fwy = rows - 1 - i;
                        return true;
                    }
                }else{
                    c = 0;
                }
            }
        }
        return false;
    }
    public static void gameOverBox(int code){
        JPanel myPanel = new JPanel();
        String message = "";
        switch(code){
            case 0://draw
                message = "DRAW, the board is filled";
                break;
            case 1://Player 1 wins
                message = "("+ p1Name +") Player 1 wins!";
                break;
            case 2://Player 2 wins
                message = "("+ p2Name +") Player 2 wins!";
                break;
            case 3://Player 1 wins, p2 disqualified
                message = "("+ p1Name +") Player 1 wins, p2 disqualified for illegal move";
                break;
            case 4://Player 2 wins, p1 disqualifed
                message = "("+ p2Name +") Player 2 wins, p1 disqualified for illegal move";
                break;
        }
        myPanel.add(new JLabel(message + " Would you like to play again?"));
        int response = JOptionPane.showConfirmDialog(null, myPanel,
                "Game Over", JOptionPane.OK_OPTION);
        if(response == 0){
            restart();
        }
    }
    public static void restart(){
        board = new int[rows][columns];//
        gameOver = false;    //
        draw = false;    //
        swx = 0;
        swy = 0;
        fwx = 0;
        fwy = 0;
        try {
            startGame();
        }catch(InterruptedException e){

        }
    }
}
