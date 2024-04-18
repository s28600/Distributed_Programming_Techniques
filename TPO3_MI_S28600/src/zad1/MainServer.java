package zad1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MainServer extends Thread{
    final String address = "localhost";
    final int port;
    ServerSocket serverSocket;
    final HashMap<String, String[]> langServers;

    public MainServer() {
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        port = serverSocket.getLocalPort();
        langServers = new HashMap<>();
    }

    @Override
    public void run() {
        System.out.println("Main server started on port " + port);
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
            System.out.println("ClientHandler started.");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = reader.readLine();
                System.out.println("ClientHandler got " + line);

                Socket client = new Socket(socket.getInetAddress().getHostAddress(), Integer.parseInt(line.split(" ")[2]));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                writer.println(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
