package ch.fhnw.kvanc.gwerder;

public class Receiver {
	
	Listener messageReceiver;
	Discovery discover = new Discovery();
	
	public static void main(String[] args) {
		Receiver me = new Receiver();
		me.listenToDiscovery();
		me.discover.run();
	}
	
	public void listenToDiscovery() {
		messageReceiver = new Listener(null, 7777);
		messageReceiver.setCommandHandler(new SendChat());
	}
}
