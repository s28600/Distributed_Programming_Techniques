package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;


public class Server {
	boolean running = true;
	HashMap<String, Set<SocketChannel>> topicsClients;

	public static void main(String[] args) throws IOException {
		new Server();
	}

	public Server() throws IOException {
		topicsClients = new HashMap<>();
		topicsClients.put("Automotive", new HashSet<>());
		topicsClients.put("Science", new HashSet<>());
		topicsClients.put("Sport", new HashSet<>());
		topicsClients.put("Gaming", new HashSet<>());
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.socket().bind(new InetSocketAddress(Configuration.SERVER_ADDRESS, Configuration.SERVER_PORT));
		serverChannel.configureBlocking(false);
		System.out.println("Server initiated.");

		Selector selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		System.out.println("Waiting...");

		while (running) {

					// Selekcja gotowej operacji
			  		// To wywolanie jest blokujące
			  		// Czeka aż selektor powiadomi o gotowości jakiejś operacji na jakimś kanale
			selector.select();

			  		// Teraz jakieś operacje są gotowe do wykonania
			  		// Zbiór kluczy opisuje te operacje (i kanały)
			 Set<SelectionKey> keys = selector.selectedKeys();

			  		// Przeglądamy "gotowe" klucze
			 Iterator<SelectionKey> iter = keys.iterator();

			 while(iter.hasNext()) {

				  	// pobranie klucza
				 SelectionKey key = iter.next();

				  	// musi być usunięty ze zbioru (nie ma autonatycznego usuwania)
				  	// w przeciwnym razie w kolejnym kroku pętli "obsłużony" klucz
				  	// dostalibyśmy do ponownej obsługi
				 iter.remove();

			    		// Wykonanie operacji opisywanej przez klucz
				 if (key.isAcceptable()) { // połaczenie klienta gotowe do akceptacji

					 System.out.println("Server: New connection - accepting... ");
			    		// Uzyskanie kanału do komunikacji z klientem
			    		// accept jest nieblokujące, bo już klient czeka
					 SocketChannel cc = serverChannel.accept();

			    		// Kanał nieblokujący, bo będzie rejestrowany u selektora
					 cc.configureBlocking(false);

			    		// rejestrujemy kanał komunikacji z klientem
			    		// do monitorowania przez ten sam selektor
					 cc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

					 continue;
				  }

				  if (key.isReadable()) {  // któryś z kanałów gotowy do czytania

			    			// Uzyskanie kanału na którym czekają dane do odczytania
					  SocketChannel cc = (SocketChannel) key.channel();

					  serviceRequest(cc);

			    			// obsługa zleceń klienta
			    			// ...
					  continue;
				  }
				  if (key.isWritable()) {  // któryś z kanałów gotowy do pisania

					  		// Uzyskanie kanału
					  //SocketChannel cc = (SocketChannel) key.channel();

			    			// pisanie do kanału
			    			// ...
					  continue;
				  }

			 }
		}

	}

	
		// Strona kodowa do kodowania/dekodowania buforów
	private static Charset charset  = Configuration.CHARSET;
	private static final int BSIZE = 1024;

	  	// Bufor bajtowy - do niego są wczytywane dane z kanału
	private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);

	  	// Tu będzie zlecenie do pezetworzenia
	private StringBuffer reqString = new StringBuffer();
	
	
	private void serviceRequest(SocketChannel sc) {
		if (!sc.isOpen()) return; // jeżeli kanał zamknięty
	 
		System.out.print("\nServer: Service request: ");
		// Odczytanie zlecenia
		reqString.setLength(0);
	    bbuf.clear();
	    
	    try {
			readLoop:                    // Czytanie jest nieblokujące
	    	while (true) {               // kontynujemy je dopóki
	    		int n = sc.read(bbuf);   // nie natrafimy na koniec wiersza
	    		if (n > 0) {
	    			bbuf.flip();
	    			CharBuffer cbuf = charset.decode(bbuf);
	    			while(cbuf.hasRemaining()) {
	    				char c = cbuf.get();
	    				//System.out.println(c);
	    				if (c == '\r' || c == '\n') break readLoop;
	    				else {
	    					//System.out.println(c);
	    				    reqString.append(c);
	    				}
	    			}
	    		}
			}

			String cmd = reqString.toString();
		    System.out.println(reqString);
			String[] request = cmd.split(" ");

		    if (request[0].equals("TOPICS")) {
				StringBuilder stringBuilder = new StringBuilder();
				for (var topic : topicsClients.keySet()){
					stringBuilder.append(topic).append(" ");
				}
				System.out.print("Server: Available topics are: ");
				System.out.println(topicsClients.keySet());
				System.out.println("Server: Sending topics to client...");
				sc.write(charset.encode(CharBuffer.wrap(stringBuilder.toString())));
			}
			else if (request[0].equals("SUBSCRIPTIONS")) {
				StringBuilder stringBuilder = new StringBuilder();
				for (var entry : topicsClients.entrySet()){
					if (entry.getValue().contains(sc)) stringBuilder.append(entry.getKey()).append(" ");
				}
				if (stringBuilder.isEmpty()) stringBuilder.append("NONE");
				System.out.print("Server: Subscribed topics are: ");
				System.out.println(Arrays.toString(stringBuilder.toString().split(" ")));
				System.out.println("Server: Sending topics to client...");
				sc.write(charset.encode(CharBuffer.wrap(stringBuilder.toString())));
			}
			else if (request[0].equals("SUBSCRIBE")) {
				topicsClients.get(request[1]).add(sc);
				System.out.println("Server: Client subscribed to topic: " + request[1]);
			}
			else if (request[0].equals("UNSUBSCRIBE")) {
				topicsClients.get(request[1]).remove(sc);
				System.out.println("Server: Client unsubscribed from topic: " + request[1]);
			}
	 
	    } catch (Exception exc) { // przerwane polączenie?
	        try {
				sc.close();
				sc.socket().close();
				System.out.println("\nConnection closed.");
	        } catch (Exception e) {}
	    }
	    
	}

}
