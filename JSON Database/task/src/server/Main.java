package server;

import java.io.IOException;

public class Main {

    public static final int PORT = 23456;
    public static final String HOST_ADDRESS = "localhost";
    public static final String FILE_PATH = "./JSON Database/task/src/server/data/db.json";
    public static Server server;

    public static void main(String[] args) throws IOException {

        server = new Server(FILE_PATH, PORT);
        server.run();

    }
}

