package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        String address = "127.0.0.1";
        int port = 23456;

        ServerSocket server = null;
        DataOutputStream output = null;
        DataInputStream input = null;
        Socket socket = null;
        try {
            server = new ServerSocket(port, 50, InetAddress.getByName(address));
            System.out.println("Server started!");
            socket = server.accept();
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            final String fromClient = input.readUTF();
            System.out.println("Received: " + fromClient);
            final String[] msgFromClient = fromClient.split(" ");
            String no = msgFromClient[msgFromClient.length - 1];
            final String msgToClient = "A record # " + no + " was sent!";
            output.writeUTF(msgToClient);
            System.out.println("Sent: " + msgToClient);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
                socket.close();
                server.close();
            } catch (IOException | NullPointerException ex) {
                ex.printStackTrace();
            }
        }
        Scanner scanner = new Scanner(System.in);

        String[] jsonDb = new String[1000];
        Arrays.fill(jsonDb, "");

        do {
            final String[] inputText = scanner.nextLine().trim().split(" ");
            switch (inputText[0]) {
                case "get":
                    int anInt = Integer.parseInt(inputText[1]);
                    if (anInt < 1 || anInt > 100 || jsonDb[anInt - 1].equals("")) {
                        System.out.println("ERROR");
                    } else {
                        System.out.println(jsonDb[anInt - 1]);
                    }
                    break;
                case "set":
                    anInt = Integer.parseInt(inputText[1]);
                    if (anInt < 1 || anInt > 100) {
                        System.out.println("ERROR");
                    } else {
                        String temp = "";
                        for (int i = 2; i < inputText.length; i++) {
                            temp += inputText[i] + " ";
                        }
                        jsonDb[anInt - 1] = temp.trim();
                        System.out.println("OK");
                    }
                    break;
                case "delete":
                    anInt = Integer.parseInt(inputText[1]);
                    if (anInt < 1 || anInt > 100) {
                        System.out.println("ERROR");
                    } else {
                        if (!jsonDb[anInt - 1].equals("")) {
                            jsonDb[anInt - 1] = "";
                        }
                        System.out.println("OK");
                    }
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Bad action.");

            }
        } while (true);
    }
}
