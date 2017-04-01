package ch.fhnw.kvanc.gwerder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPCommunicator {
	ServerSocket srvr;
	Socket skt;
	boolean shutdown = false;
	TCPListener listener;
	
	public TCPCommunicator(){
		try {
			srvr = new ServerSocket(10000);
			skt = srvr.accept();
			listener = new TCPListener(skt);
			listener.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public TCPCommunicator(String ipAdress) {
		try {
			skt = new Socket(ipAdress, 10000);
			listener = new TCPListener(skt);
			listener.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(String msg){
		System.out.println(msg.replace(' ', '$') + " " + msg.length());
		try {
			skt.getOutputStream().write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void shutdown(){
		shutdown = true;
		listener.shutdown();
		try {
			if(srvr != null) srvr.close();
			skt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setCommandHandler(CommandHandler commandHandler) {
		listener.setCommandHandler(commandHandler);
	}
}
