package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import messages.JsonMsg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    @Parameter(names = "-t")
    String type;
    @Parameter(names = "-k")
    String key;
    @Parameter(names = "-v")
    String value;

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

        JsonMsg jcm = new JsonMsg(type, key, value);

        Gson gson = new Gson();
        String jsonMsg = gson.toJson(jcm);


        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Sent: " + jsonMsg);
            output.writeUTF(jsonMsg);
            final String msgFromServer = input.readUTF();
            System.out.println("Received: " + msgFromServer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

