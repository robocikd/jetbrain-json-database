package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    @Parameter(names = "-t")
    String type;
    @Parameter(names = "-i")
    int index;
    @Parameter(names = "-m", variableArity = true)
    List<String> message = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }

    public void run() {
        String address = "127.0.0.1";
        int port = 23456;
        System.out.println("Client started!");
        String msgToServer = type + " " + index;
        for (int i = 0; i < message.size(); i++) {
            msgToServer += " " + message.get(i);
        }
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Sent: " + msgToServer);
            output.writeUTF(msgToServer);
            final String msgFromServer = input.readUTF();
            System.out.println("Received: " + msgFromServer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

