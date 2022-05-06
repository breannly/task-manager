import controller.manager.HTTPTaskManager;
import controller.utility.Managers;
import server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args)
            throws IOException {
        new KVServer().start();
    }
}

