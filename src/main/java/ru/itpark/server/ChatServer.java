package ru.itpark.server;

import ru.itpark.model.ClientConnection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static int count = 0;
    private static int port = 9876;
    private static List<ClientConnection> clientConnectionList = new ArrayList<>();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connections: " + ++count);

                final ClientConnection clientConnection = new ClientConnection("client" + count, socket);
                clientConnectionList.add(clientConnection);

                new Thread(() -> {
                    String line;
                    try {
                        clientConnection.sendMessage("Please enter your name.");
                        if (((line = clientConnection.receiveMessage()) != null) && !line.isEmpty()) {
                            clientConnection.sendMessage("Your name is set to " + line + "\nYou are now in chat room. " +
                                    "Enter this command to exit: #exit!");
                            clientConnection.setName(line);
                        } else if (line.isEmpty()) {
                            clientConnection.sendMessage("You have provided no name, it is set to " +
                                    clientConnection.getName() + "\nYou are now in chat room.");
                        }

                        while ((line = clientConnection.receiveMessage()) != null) {
                            String finalLine = line;
                            if (finalLine.equals("#exit!")) {
                                clientConnection.sendMessage("#exitCommencing!");
                                clientConnectionList.remove(clientConnection);
                                socket.close();
                                System.out.println("Connections: " + --count);
                                Thread.currentThread().interrupt();
                                break;
                            }
                            clientConnectionList.forEach(o -> {
                                try {
                                    if (!o.equals(clientConnection)) {
                                        o.sendMessage(clientConnection.getName() + ":" + finalLine);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
