package zad1;

public class Main {
    public static void main(String[] args) {
        MainServer mainServer = new MainServer();
        mainServer.start();

        Client client1 = new Client(mainServer.address, mainServer.port, 50001);
        client1.getTranslation("test", "EN");
    }
}
