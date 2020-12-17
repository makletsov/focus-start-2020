package ru.makletsov.focusstart.s.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ChatClient {

	private static final ThreadFactory factory = Executors.defaultThreadFactory();

	private String hostname;
	private int port;
	private String userName;

	public ChatClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void execute() {
		try {
			Socket socket = new Socket(hostname, port);

			System.out.println("Connected to the chat server");

			Thread writerThread = factory.newThread(new ServerWriter(socket, this));
			Thread readerThread = factory.newThread(new ServerReader(socket, this));

			writerThread.start();
			readerThread.start();
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O Error: " + ex.getMessage());
		} catch (IllegalStateException ex) {
			System.out.println(ex.getMessage());
		}
	}

	void setUserName(String userName) {
		this.userName = userName;
	}

	String getUserName() {
		return this.userName;
	}
}