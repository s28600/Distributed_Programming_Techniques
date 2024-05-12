package zad1;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class Configuration {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 50000;
    public static final int BUFFER_SIZE = 1024;
    public static final Charset CHARSET = StandardCharsets.UTF_8;
}
