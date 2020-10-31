package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        String address = "127.0.0.1";
        int port = 23456;


        Socket socket = null;
        DataInputStream input = null;
        DataOutputStream output = null;
        System.out.println("Client started!");
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            String msg = "Give me a record # 12";
            System.out.println("Sent: " + msg);
            output.writeUTF(msg);
            final String msgFromServer = input.readUTF();
            System.out.println("Received: " + msgFromServer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
