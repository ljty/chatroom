package com.bozhi.chatroom;

public class ChatRoom {
	public static void main(String args[]) {
	final	SendMessage send = new SendMessage();
	final	ReceiveMessage receive = new ReceiveMessage();
		
		new Thread(new Runnable(){

			public void run() {
				send.send();
			}
			
		}).start();
		new Thread(new Runnable(){

			public void run() {
				receive.receive();
			}
			
		}).start();
	}
}
