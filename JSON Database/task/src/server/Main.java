package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {

    public static final int MAX_DB_INDEX = 1000;
    public static final String ERROR = "ERROR";
    public static final String OK = "OK";

    public static void main(String[] args) throws IOException {

        String address = "127.0.0.1";
        int port = 23456;

        String[] jsonDb = new String[1000];
        Arrays.fill(jsonDb, "");

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            do {
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                    final String fromClient = input.readUTF();
                    System.out.println("Received: " + fromClient);
                    final String[] msgFromClient = fromClient.trim().split(" ");

                    String msgToClient = "";

                    switch (msgFromClient[0]) {
                        case "get":
                            int anInt = Integer.parseInt(msgFromClient[1]);
                            if (anInt < 1 || anInt > MAX_DB_INDEX || jsonDb[anInt - 1].equals("")) {
                                msgToClient += ERROR;
                            } else {
                                String valFromDb = jsonDb[anInt - 1];
                                msgToClient += valFromDb;
                            }
                            break;
                        case "set":
                            anInt = Integer.parseInt(msgFromClient[1]);
                            if (anInt < 1 || anInt > MAX_DB_INDEX) {
                                msgToClient += ERROR;
                            } else {
                                String temp = "";
                                for (int i = 2; i < msgFromClient.length; i++) {
                                    temp += msgFromClient[i] + " ";
                                }
                                jsonDb[anInt - 1] = temp.trim();
                                msgToClient += OK;
                            }
                            break;
                        case "delete":
                            anInt = Integer.parseInt(msgFromClient[1]);
                            if (anInt < 1 || anInt > MAX_DB_INDEX) {
                                msgToClient += ERROR;
                            } else {
                                if (!jsonDb[anInt - 1].equals("")) {
                                    jsonDb[anInt - 1] = "";
                                }
                                msgToClient += OK;
                            }
                            break;
                        case "exit":
                            return;
                        default:
                            System.out.println("Bad action.");
                    }
                    output.writeUTF(msgToClient);
                    System.out.println("Sent: " + msgToClient);
                }
            } while (true);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}

