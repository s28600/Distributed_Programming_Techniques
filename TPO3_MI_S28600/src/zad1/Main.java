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

        String output = client.getTranslation("mokry", "EN");
        System.out.println("\nClient side output:");
        System.out.println(output);
    }
}
