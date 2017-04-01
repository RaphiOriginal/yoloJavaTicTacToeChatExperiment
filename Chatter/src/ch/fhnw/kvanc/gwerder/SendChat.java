package ch.fhnw.kvanc.gwerder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

public class SendChat implements CommandHandler{
	
	String nickname = "Jasmin";
	Set<String> participants = new HashSet<>();
	
	public static void main(String[] args) {
		Discovery.discover();
		SendChat sender = new SendChat();
		sender.sendMsg(args[0]);
		
	}

	@Override
	public void processCommand(String txt) {
		String command = txt.substring(0, 2);
		switch(command){
			case "dx":
				sendNickname();
				break;
			case "ux":
				processParticipant(txt.substring(3));
				break;
			case "mx":
				displayMessage(txt.substring(3));
				break;
			default:
				//nothing
				break;
		}
		
	}
	
	public void sendNickname() {
		String msg = "ux:";
		byte[] nickAsByteArray = nickname.getBytes();
		msg += nickAsByteArray.length;
		msg += nickname;
		try {
			Sender.send(InetAddress.getByName("255.255.255.255"), 6666, msg);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processParticipant(String inputString) {
		String participant = inputString.substring(1);
		participants.add(participant);
		//System.out.println(participants.toString());
	}
	
	public void displayMessage(String msgInput) {
		String nicknameLengthString = msgInput.substring(0,1);
		int nicknameLength = Integer.parseInt(nicknameLengthString);
		String sender = msgInput.substring(1, ++nicknameLength);
		String msg = msgInput.substring(nicknameLength);
		System.out.println(sender + ": " + msg);
	}
	
	public void sendMsg(String message) {
		String msg = "mx:";
		byte[] nickAsByteArray = nickname.getBytes();
		msg += nickAsByteArray.length;
		msg += nickname;
		msg += message;
		try {
			Sender.send(InetAddress.getByName("255.255.255.255"), 7777, msg);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
