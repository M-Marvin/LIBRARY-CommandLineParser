package de.m_marvin.commandlineparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class CommandLineReader {
	
	protected final BufferedReader reader;
	protected final Thread readerThread;
	protected final Deque<String> inputBuffer;
	protected boolean closeThread = false;
	
	public CommandLineReader(InputStream inputStream) {
		this(new BufferedReader(new InputStreamReader(inputStream)));
	}
	
	public CommandLineReader(BufferedReader reader) {
		this.reader = reader;
		this.inputBuffer = new ArrayDeque<>();
		this.readerThread = new Thread(this::handleRead, "CommandLineReader");
		this.readerThread.setDaemon(true);
		this.readerThread.start();
	}
	
	public void close() throws IOException {
		this.closeThread = true;
	}
	
	public void handleRead() {
		try {
			try {
				while (!this.closeThread) {
					String inputLine = this.reader.readLine();
					if (inputLine == null) close();
					this.inputBuffer.add(inputLine);
				}
			} catch (IOException e) {
				System.err.println("An IOException occured while reading from the InputSteam!");
				e.printStackTrace();
			}
			this.reader.close();
		} catch (IOException e) {
			System.err.println("IOException while trying to close input stream!");
			e.printStackTrace();
		}
	}
	
	public Optional<String> readLine() {
		if (this.inputBuffer.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(this.inputBuffer.pop());
		}
	}
	
}
