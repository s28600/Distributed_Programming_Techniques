package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {
    String mainServerAddress;
    int mainServerPort;
    int port;

    public Client(String mainServerAddress, int mainServerPort, int port) {
        this.mainServerAddress = mainServerAddress;
        this.mainServerPort = mainServerPort;
        this.port = port;
    }

    public String getTranslation(String word, String langCode) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = new Socket(mainServerAddress, mainServerPort);
            new PrintWriter(socket.getOutputStream(), true).println(word + " " + langCode + " " + port);
            socket.close();

            serverSocket.setSoTimeout(5000);
            Socket incoming = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            String s = reader.readLine();
            System.out.println("Client received: " + s);
            return s;
        }catch (SocketTimeoutException e){
            return "TIMEOUT";
        } catch (IOException e) {
            return "CONNECTION TO THE MAIN SERVER FAILED";
        }
    }
}
