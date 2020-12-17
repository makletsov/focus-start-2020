package ru.makletsov.focusstart.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ChatClient {
	private static final Logger LOG = LoggerFactory.getLogger(ChatClient.class.getSimpleName());

	private final String hostname;
	private final int port;
	private final PrintStream userOutput;
	private final InputStream userInput;
	private final ExecutorService executor;

	private String userName;

	public ChatClient(String hostname, int port, InputStream userInput, PrintStream userOutput) {
		this.hostname = hostname;
		this.port = port;

		this.userOutput = userOutput;
		this.userInput = userInput;

		executor = Executors.newFixedThreadPool(2);
	}

	public void execute() {
		try {
            Socket socket = new Socket(hostname, port);

			userOutput.println("Connected to the chat server.");

			executor.execute(new ServerWriter(socket, userInput, userOutput));
			executor.execute(new ServerReader(socket, userOutput));
		} catch (UnknownHostException ex) {
		    userOutput.println("Server not found: " + hostname + ":" + port);
			LOG.error("Server not found: ", ex);
		} catch (IOException ex) {
			LOG.error("I/O Error: ", ex);
		}
	}
}