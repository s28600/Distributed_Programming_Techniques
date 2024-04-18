package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    String mainServerAddress;
    int mainServerPort;
    int port;

    public Client(String mainServerAddress, int mainServerPort) {
        this.mainServerAddress = mainServerAddress;
        this.mainServerPort = mainServerPort;
    }

    public String getTranslation(String word, String langCode) {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = serverSocket.getLocalPort();
            Socket socket = new Socket(mainServerAddress, mainServerPort);
            new PrintWriter(socket.getOutputStream(), true).println(word + " " + langCode + " " + port);
            socket.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(serverSocket.accept().getInputStream()));
            String s = reader.readLine();
            System.out.println("Client received: " + s);
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
