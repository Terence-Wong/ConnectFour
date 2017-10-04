public class MinMaxBot extends Player{
	static Main game;
    int playerType;
	boolean p1;
	static int cx = -1, cy = -1;
	static int columns = 7, rows = 6;
	
	byte depths = 4;
	String name = "Level " + depths + " Bot";
	
	
	public MinMaxBot(Main g, int p){
		game = g;
        playerType = p;
		if (p == 1){
			p1 = true;
		}else{
			p1 = false;
		}
    }
	
	//alterate, level select
	public MinMaxBot(Main g, int p, int level){
		depths = (byte)level;
		name = "Level " + depths + " Bot";
		
		game = g;
        playerType = p;
		if (p == 1){
			p1 = true;
		}else{
			p1 = false;
		}
    }
	
	@Override
    public void startTurn() throws InterruptedException{
		Board b = new Board();
		for(int x = 0; x < 7; x++){//horizontal
			for(int y = 0; y < 6; y++){//vertical
				b.changeCell(y,x,(byte)game.checkCell(x, y, playerType));
			}
		}
		game.placePiece(getMove(b,depths, p1 ), playerType);
    }
	@Override
    public String getName(){
        return name;
    }
	public byte getMove(Board board, byte depth, boolean p){
		
		double result = -Double.MAX_VALUE;
		byte xSelection = 0;
		double[] moveset = new double[7];
		
		for(byte x = 0; x < 7; x++){
			byte y = 5;
			
			while(y >= 0){
				if(board.getCell(y,x) == 0){
					break;
				}
				y--;
			}
			if(y != -1){
				board.changeCell(y,x,((p) ? (byte)1 : (byte)2));
				cx = x;
				cy = y;
				moveset[x] = minmax(board, (byte)(depth-1),!p);
				board.changeCell(y,x,(byte)0);
				
				if(moveset[x] > result){
					result = moveset[x];
					xSelection = x;
				}
			}else if(xSelection == x){
				xSelection++;
			}
		}
		//System.out.println("");
		//for(byte x = 0; x < 7; x++){
		//	System.out.print(moveset[x] + " ");
		//}
		//System.out.println(xSelection);
		return xSelection;
	}
	
	public double minmax(Board board, byte depth, boolean p){
		if(checkWon( ((p) ? 2 : 1) , board) ){
			if(p == p1){//you
				return -1.0;
			}else{//opponent
				return 1.0;
			}
		}
		if(depth == 0){
			return 0.0;
		}
		if(p == p1){//you, maximum
			double result = -Double.MAX_VALUE;
			double[] moveset = new double[7];
			for(byte x = 0; x < 7; x++){
				byte y = 5;
				while(y >= 0){
					if(board.getCell(y,x) == 0){
						break;
					}
					y--;
				}
				if(y != -1){
					board.changeCell(y,x,((p) ? (byte)1 : (byte)2));
					cx = x;
					cy = y;
					moveset[x] = minmax(board, (byte)(depth-1),!p);
					board.changeCell(y,x,(byte)0);
					result = moveset[x] > result ? moveset[x] : result; //max(result, move);
				}
			}
			return result;
		}else{//opponent, average
			double result = 0;
			double[] moveset = new double[7];
			byte moves = 0;
			for(byte x = 0; x < 7; x++){
				byte y = 5;
				while(y >= 0){
					if(board.getCell(y,x) == 0){
						break;
					}
					y--;
				}
				if(y != -1){
					board.changeCell(y,x,((p) ? (byte)1 : (byte)2));
					cx = x;
					cy = y;
					double move = minmax(board, (byte)(depth-1),!p);
					board.changeCell(y,x,(byte)0);
					result += move;
					moves++;
				}
			}
			result /= moves;
			return result;
		}
	}
	
	
	
	
	public static boolean checkWon(int player,Board board){
		if(cx == -1){
			return false;
		}
        //check horizontal
        int c = 0;
        for(int x = 0; x < columns; x++){
            if(board.getCell(cy,x) == player){
                c++;
                if(c == 4){
                    return true;
                }
            }else{
                c = 0;
            }
        }
        //check vertical
        c = 0;
        for(int y = 0; y < rows; y++){
            if(board.getCell(y,cx) == player){
                c++;
                if(c == 4){
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
                if(board.getCell(i,tx + i) == player){
                    c++;
                    if(c == 4){
                        return true;
                    }
                }else{
                    c = 0;
                }
            }
        }else{
            int ty = cy - cx;//tx represents x where y = 0, top of diagonal
            for(int i = 0; i + ty < rows; i++){
                if(board.getCell(ty + i,i) == player){
                    c++;
                    if(c == 4){
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
                if(board.getCell(ty - i,i) == player){
                    c++;
                    if(c == 4){
                        return true;
                    }
                }else{
                    c = 0;
                }
            }
        }else{
            int tx = cx + cy - rows + 1;
            for(int i = 0; i < columns - tx; i++){
                if(board.getCell(rows-1-i,tx+i) == player){
                    c++;
                    if(c == 4){
                        return true;
                    }
                }else{
                    c = 0;
                }
            }
        }
        return false;
    }
	public double max(double a, double b){
		if(a > b){
			return a;
		}
		return b;
	}
	public double min(double a, double b){
		if(a < b){
			return a;
		}
		return b;
	}
}
class Board{
	byte[][] b;
	public Board(){
		b = new byte[6][7];
	}
	public Board(Board bb){
		b = bb.b;
	}
	public void changeCell(int y, int x, byte i){
		b[y][x] = i;
	}
	public byte getCell(int y, int x){
		return b[y][x];
	}
}