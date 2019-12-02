package ru.itpark.client;

import java.io.*;
import java.net.Socket;

public class ClientMessenger {
    public static void main(String[] args) throws IOException {
        final Socket socket = new Socket("localhost", 9876);
        try (
                final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                final BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            final Thread thread = new Thread(() -> {
                String input;
                try {
                    while ((input = reader.readLine()) != null) {
                        if (Thread.currentThread().isInterrupted()){
                            break;
                        }
                        socketWriter.write(input);
                        socketWriter.newLine();
                        socketWriter.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            String line;
            while((line = socketReader.readLine()) != null){
                if (line.equals("#exitCommencing!")){
                    break;
                }
                System.out.println(line);
            }
            thread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
