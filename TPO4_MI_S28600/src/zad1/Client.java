package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
	SocketChannel channel;
	Charset charset = Configuration.CHARSET;
	ByteBuffer inBuf = ByteBuffer.allocateDirect(Configuration.BUFFER_SIZE);

	public Client(){
		try {
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress(Configuration.SERVER_ADDRESS, Configuration.SERVER_PORT));

			System.out.print("Client: Connecting to server...");

			while (!channel.finishConnect()) {
				System.out.print(".");
			}

			System.out.println();
		} catch (UnknownHostException exc) {
			System.err.println("Unknown host " + Configuration.SERVER_ADDRESS);
		} catch (Exception exc){
			throw new RuntimeException(exc);
		}

		System.out.println("Client: Connected.");
	}

	public String getTopics() {
		try {
			System.out.println("\nClient: Sending request for available topics...");
			channel.write(charset.encode("TOPICS\n"));
			return readResponse();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void subscribe(String topic) {
        try {
			System.out.println("\nClient: Sending request to subscribe to topic: " + topic);
            channel.write(charset.encode("SUBSCRIBE " + topic + "\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public void unsubscribe(String topic) {
		try {
			System.out.println("\nClient: Sending request to unsubscribe from topic: " + topic);
			channel.write(charset.encode("UNSUBSCRIBE " + topic + "\n"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getSubscriptions() {
		try {
			System.out.println("\nClient: Sending request for subscribed topics...");
			channel.write(charset.encode("SUBSCRIPTIONS\n"));
			return readResponse();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String readResponse(){
		try {
			CharBuffer cbuf = CharBuffer.wrap("");
			while (true) {
				inBuf.clear();	// opróżnienie bufora wejściowego
				int readBytes = channel.read(inBuf); // czytanie nieblokujące
				// natychmiast zwraca liczbę
				// przeczytanych bajtów

				// System.out.println("readBytes =  " + readBytes);

				if (readBytes == -1) { // kanał zamknięty po stronie serwera
					// dalsze czytanie niemożlwe
					// ...
					break;
				}
				else if (readBytes != 0) {		// dane dostępne w buforze
					//System.out.println("coś jest od serwera");

					inBuf.flip();	// przestawienie bufora

					// pobranie danych z bufora
					// ew. decyzje o tym czy mamy komplet danych - wtedy break
					// czy też mamy jeszcze coś do odebrania z serwera - kontynuacja
					cbuf = charset.decode(inBuf);

					String odSerwera = cbuf.toString();

					System.out.println("Client: Server response: " + odSerwera);
					cbuf.clear();

					break;
				}
			}
			return cbuf.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
