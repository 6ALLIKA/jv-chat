package test;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ServerSocket> serverList = new LinkedList<>();
    public static Story story;

    public static void main(String[] args) throws IOException {
        java.net.ServerSocket server = new java.net.ServerSocket(PORT);
        story = new Story();
        System.out.println("Server Started");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSocket(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}
