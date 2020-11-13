package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final DataBase dataBase;
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;

    public Server(String filePath, int port) throws IOException {
        this.dataBase = new DataBase(filePath);
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newFixedThreadPool(100);
    }

    @Override
    public void run() {
        System.out.println("Server started!");
        try {
            while (true) {
                executorService.submit(new Task(serverSocket.accept(), dataBase));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
