package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import messages.JsonMsg;
import messages.ResponseJson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static final String ERROR = "ERROR";
    public static final String OK = "OK";
    public static final String NO_SUCH_KEY = "No such key";

    public static void main(String[] args) {

        String address = "127.0.0.1";
        int port = 23456;

        JsonObject jsonDB = new JsonObject();

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            do {
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                    final String fromClient = input.readUTF();
                    System.out.println("Received: " + fromClient);

                    Gson gson = new Gson();
                    JsonMsg jsonMsg = gson.fromJson(fromClient, JsonMsg.class);

                    String response = null;
                    String value = null;
                    String reason = null;
                    String key = jsonMsg.getKey();

                    switch (jsonMsg.getType()) {
                        case "get":
                            if (!jsonDB.has(key)) {
                                response = ERROR;
                                reason = NO_SUCH_KEY;
                            } else {
                                response = OK;
                                value = jsonDB.get(key).getAsString();
                            }
                            break;
                        case "set":
                            jsonDB.addProperty(jsonMsg.getKey(), jsonMsg.getValue());
                            response = OK;
                            break;
                        case "delete":
                            if (!jsonDB.has(key)) {
                                response = ERROR;
                                reason = NO_SUCH_KEY;
                            } else {
                                jsonDB.remove(jsonMsg.getKey());
                                response = OK;
                            }
                            break;
                        case "exit":
                            return;
                        default:
                            System.out.println("Bad action.");
                    }
                    ResponseJson rj = new ResponseJson(response, value, reason);
                    String msgToClient = gson.toJson(rj);
                    output.writeUTF(msgToClient);
                    System.out.println("Sent: " + msgToClient);
                }
            } while (true);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}

