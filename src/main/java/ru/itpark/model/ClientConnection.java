package ru.itpark.model;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private static int port = 9876;
    private String name;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ClientConnection(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
