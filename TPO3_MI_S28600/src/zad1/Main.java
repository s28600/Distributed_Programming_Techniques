package zad1;

public class Main {
    public static void main(String[] args) {
        MainServer mainServer = new MainServer();
        LangServer enServer = new LangServer("EN");
        mainServer.addLangServer(enServer.langCode, enServer.address, enServer.port);
        mainServer.start();
        enServer.start();
        enServer.addTranslation("dom", "house");
        Client client = new Client(mainServer.address, mainServer.port);

        UI ui = new UI(client, mainServer);
    }
}
