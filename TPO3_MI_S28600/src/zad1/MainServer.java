package zad1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class MainServer extends Thread{
    final String address;
    final int port;
    ServerSocket serverSocket;
    final HashMap<String, String[]> langServers;

    public MainServer() {
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            address = serverSocket.getInetAddress().getHostAddress();
            System.out.println("Main server started on port " + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        langServers = new HashMap<>();
    }

    public void addLangServer(String langCode, String address, int port){
        if (!langServers.containsKey(langCode))
            langServers.put(langCode, new String[]{address, Integer.toString(port)});
        System.out.println(langCode + " language server connection added to main server.");
    }

    @Override
    public void run() {
        if (langServers.isEmpty()) {
            System.out.println("No language servers available. Aborting...");
            return;
        }

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
            System.out.println("Main server ClientHandler started.");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // [{word}, {langCode}, {clientPort}]
                String[] input = reader.readLine().split(" ");
                System.out.println("MainServer ClientHandler got " + Arrays.toString(input));

                if (!langServers.containsKey(input[1])){
                    Socket client = new Socket(socket.getInetAddress().getHostAddress(), Integer.parseInt(input[2]));
                    PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                    writer.println("Language is not supported.");
                    return;
                }

                System.out.println(Arrays.toString(langServers.get(input[1])));
                Socket langServer = new Socket(langServers.get(input[1])[0], Integer.parseInt(langServers.get(input[1])[1]));
                PrintWriter writer = new PrintWriter(langServer.getOutputStream(), true);
                writer.println(input[0] + " " + socket.getInetAddress().getHostAddress() + " " + input[2]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
