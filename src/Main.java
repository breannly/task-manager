import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args)
            throws IOException, InterruptedException {
        new KVServer().start();
        new HttpTaskServer().start();
    }
}

