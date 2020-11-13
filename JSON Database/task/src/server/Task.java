package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Task implements Runnable {

    private Socket socket;
    private DataBase dataBase;

    public Task(Socket socket, DataBase dataBase) {
        this.socket = socket;
        this.dataBase = dataBase;
    }

    @Override
    public void run() {
        synchronized (this) {
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                final String fromClient = input.readUTF();
                System.out.println("Received: " + fromClient);
                String msgToClient = dataBase.processData(fromClient);
                System.out.println("Sent: " + msgToClient);
                output.writeUTF(msgToClient);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
