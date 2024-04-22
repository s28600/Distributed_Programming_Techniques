package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class LangServer extends Thread{
    final String address;
    final String langCode;
    HashMap<String, String> dictionary;
    final int port;
    ServerSocket serverSocket;
    public LangServer(String langCode) {
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            address = serverSocket.getInetAddress().getHostAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.langCode = langCode;
        dictionary = new HashMap<>();
        System.out.println(langCode + " language server started on port " + port);
    }

    public void addTranslation(String word, String translation){
        dictionary.put(word, translation);
    }

    @Override
    public void run() {
        try {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private class ClientHandler extends Thread{
        Socket socket;

        public ClientHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Lang server ClientHandler started.");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // [{word}, {clientAddress}, {clientPort}]
                String[] input = reader.readLine().split(" ");
                System.out.println("LangServer ClientHandler got " + Arrays.toString(input));

                Socket client = new Socket(input[1], Integer.parseInt(input[2]));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

                writer.println(dictionary.getOrDefault(input[0], "Translation is not available."));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
