package ch.fhnw.kvanc.gwerder;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Discovery extends Thread {

	Listener discoveryReceiver;
	
	public static void discover() {
		try {
			Sender.send(InetAddress.getByName("255.255.255.255"), 6666, "dx:");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		discoveryReceiver = new Listener(null, 6666);
		discoveryReceiver.setCommandHandler(new SendChat());
	}
}
