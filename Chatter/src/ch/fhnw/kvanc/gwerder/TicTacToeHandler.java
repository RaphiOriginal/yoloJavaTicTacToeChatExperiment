package ch.fhnw.kvanc.gwerder;

import java.util.Scanner;

public class TicTacToeHandler implements CommandHandler{

	TCPCommunicator communicator;
	boolean runningGame = false;
	String previousRound = "          ";
	String player;
	
	public TicTacToeHandler(TCPCommunicator communicator) {
		this.communicator = communicator;
	}
	
	public void startGame(){
		runningGame = true;
		this.player = "x";
		processGame(null);	
	}

	public void sendTurn(String turn){
		String msg = "sb:";
		msg += winner(turn);
		msg += turn;
		communicator.send(msg);
		previousRound = msg.substring(3);
	}
	
	public void processGame(String game){
		if(game == null) {
			processTurn(previousRound);
		} else if(runningGame) {
			boolean isGameCorrupt = isGameCorrupt(previousRound, game);
			boolean isFinished = isGameFinished(previousRound, game);
			boolean hasWinner = hasWinner(game);
			if(!isFinished && hasWinner && !isGameCorrupt) {
				String winner = winner(game);
				if(winner.equals("" + game.charAt(0))){
					sendTurn(game.substring(1));
					runningGame = false;
					//winner output
				}
			} else if(isFinished){
				runningGame = false;
				//winner output
			} else if(isGameCorrupt){
				//shutdown everything
				runningGame = false;
				communicator.shutdown();
				System.out.println("Game is corrupt!");
			} else {
				//normal game round: Input new coordinates
				processTurn(game);
			}
			
		} else {
			runningGame = true;
			displayGameInfo("o");
		}
	}
	
	private void processTurn(String game) {
		Scanner scan = new Scanner(System.in);
		displayGameInfo(player);
		String input = scan.nextLine();
		String row = input.substring(0, 1);
		String column = input.substring(1);
		sendTurn(nextTurn(game, row, column));
	}
	
	private String nextTurn(String game, String row, String column) {
		int intRow = 0;
		switch(row){
		case "A":
			intRow = 1;
			break;
		case "B":
			intRow = 2;
			break;
		case "C":
			intRow = 3;
			break;
		default:
			//wrong input...
			break;
		}
		int intColumn = Integer.parseInt(column);
		int newPosition = (intRow - 1) * 3 + intColumn;
		
		return game.substring(0, newPosition - 1) + player + game.substring(newPosition + 1);
	}

	public boolean isGameCorrupt(String oldRound, String newRound) {
		if(oldRound != null && newRound != null) {
			for(int i = 0; i < oldRound.length(); i++){
				if(oldRound.charAt(i) != (' ') || oldRound.charAt(i) != newRound.charAt(i)){
					return true;
				}
			}
		}
		return false;
	}
	public boolean hasWinner(String game){
		return game.charAt(0) == 'x'|| game.charAt(0) == 'o';
	}
	
	public String winner(String gamefield){
		for(int i = 1; i <= 3; i++) {
			String winnerRow = winnerRow(i, gamefield);
			if(!winnerRow.equals(" ")) return winnerRow;
			String winnerColumn = winnerColumn(i, gamefield);
			if(!winnerColumn.equals(" ")) return winnerColumn;
		}
		String winnerDia = winnerDiagonal(gamefield);
		if(!winnerDia.equals(" ")) return winnerDia;
		return " ";
	}
	private String winnerRow(int row, String game){
		return (game.charAt((row - 1) * 3) == game.charAt((row - 1) * 3 + 1) 
				&& game.charAt((row - 1) * 3) == game.charAt((row - 1) * 3 + 2))
				? "" + game.charAt((row - 1) * 3) : " " ;
	}
	private String winnerColumn(int column, String game){
		return (game.charAt(column - 1) == game.charAt(column - 1 + 3) 
				&& game.charAt(column - 1 + 6) == game.charAt(column - 1))
				? "" + game.charAt(column - 1) : " " ;
	}
	private String winnerDiagonal(String game){
		String winnerA = (game.charAt(0) == game.charAt(4) 
				&& game.charAt(0) == game.charAt(8))
				? "" + game.charAt(4) : " ";
		String winnerB = (game.charAt(2) == game.charAt(4) 
				&& game.charAt(4) == game.charAt(6))
				? "" + game.charAt(4) : " ";
		String winner = (winnerA.equals(" "))? winnerB : winnerA;
		return winner;
	}
	
	public boolean isGameFinished(String oldRound, String newRound) {
		return oldRound.equals(newRound);
	}

	@Override
	public void processCommand(String txt) {
		String command = txt.substring(0, 2);
		System.out.println("Incomming Message: " + txt.replace(' ', '$') + " " + txt.length());
		switch(command){
			case "sb":
				processGame(txt.substring(3));
				break;
			default:
				//nothing
				break;
		}
	}
	private void displayGameInfo(String player) {
		this.player = player;
		System.out.println("You are Player " + player +"!");
		System.out.println("Enter Coordinates with A-C and 1-3");
	}
}
