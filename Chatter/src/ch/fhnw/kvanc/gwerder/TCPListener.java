package ch.fhnw.kvanc.gwerder;

import java.io.IOException;
import java.net.Socket;

public class TCPListener extends Thread {
	

    private static int BUFFER_SIZE  = 1024;

	CommandHandler commandHandler = null;
	Socket skt;
	boolean shutdown = false;
	
	public TCPListener(Socket skt){
		this.skt = skt;
	}
	
	public void run(){
		System.out.println("TCPListener started!");
		try {
			while(!shutdown){
				if(skt.isConnected()) {
					byte[] buf = new byte[BUFFER_SIZE];
					skt.getInputStream().read(buf);
					String input =  new String(buf, 0, 13);
					//System.out.println("read: " + input);
					if(commandHandler != null){
						commandHandler.processCommand(input);
					} else {
						System.out.println("ignored message:" + input);
					}
				} else {
					System.out.println("Lost connection!");
					shutdown = true;
				}
			}
			skt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setCommandHandler(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	public void shutdown(){
		this.shutdown = true;
	}
}
