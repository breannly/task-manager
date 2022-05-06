package server;

import controller.manager.FileBackedTaskManager;

import java.io.IOException;
import java.net.URI;

public class HTTPTaskManager extends FileBackedTaskManager implements  {
    KVTaskClient client;

    HTTPTaskManager(URI uri) throws IOException, InterruptedException {
        super(uri);
        client = new KVTaskClient(uri);
    }
}
