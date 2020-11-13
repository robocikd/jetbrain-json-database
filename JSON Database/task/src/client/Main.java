package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import messages.JsonMsg;

import java.io.*;
import java.net.Socket;

public class Main {

    @Parameter(names = "-in")
    String input;
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
        String address = "localhost";
        int port = 23456;
        System.out.println("Client started!");
        Gson gson = new Gson();

        if (input != null) {
            String filePath = "./JSON Database/task/src/client/data/" + input;
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
                String line = fileReader.readLine();
                JsonMsg jsonMsg = gson.fromJson(line, JsonMsg.class);
                type = jsonMsg.getType();
                key = jsonMsg.getKey();
                value = jsonMsg.getValue();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonMsg jcm = new JsonMsg(type, key, value);
        String jsonMsg = gson.toJson(jcm);

        try (Socket socket = new Socket(address, port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Sent: " + jsonMsg);
            output.writeUTF(jsonMsg);
            System.out.println("Received: " + input.readUTF());
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
    }
}

