package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        String address = "127.0.0.1";
        int port = 23456;
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        Socket socket = server.accept();
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        Scanner scanner = new Scanner(System.in);

        String[] jsonDb = new String[100];
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
