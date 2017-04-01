package ch.fhnw.kvanc.gwerder;


public class TicTacToe {
	
	TCPCommunicator communicator;
	TicTacToeHandler handler;
	
	public TicTacToe() {
		communicator = new TCPCommunicator();
		handler = new TicTacToeHandler(communicator);
		communicator.setCommandHandler(handler);
	}
	
	public TicTacToe(String ipAdress) {
		communicator = new TCPCommunicator(ipAdress);
		handler = new TicTacToeHandler(communicator);
		communicator.setCommandHandler(handler);
	}

	public static void main(String[] args) {
		TicTacToe tictactoe;
		if(args.length > 0) {
			tictactoe = new TicTacToe(args[0]);
			tictactoe.startGame();
		} else {
			tictactoe = new TicTacToe();
		}
		
	}
	
	public void startGame() {
		handler.startGame();
	}

}
