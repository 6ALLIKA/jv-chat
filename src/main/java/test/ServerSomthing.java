package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerSomthing extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;
    private BufferedReader inputServer;

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        inputServer = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Server.story.printStory(out);
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            word = in.readLine();
            try {
                out.write(word + "\n");
                out.flush();
            } catch (IOException ignored) {
                System.out.println("Error");
            }
            try {
                while (true) {
                    time = new Date();
                    dt1 = new SimpleDateFormat("HH:mm:ss");
                    dtime = dt1.format(time);
                    String serverMsg = inputServer.readLine();
                    for (ServerSomthing vr : Server.serverList) {
                        vr.send("(" + dtime + ") " + "Server" + ": " + serverMsg);
                    }
                    word = in.readLine();
                    if (word.equals("stop")) {
                        this.downService();
                        break;
                    }
                    System.out.println("Echoing: " + word);
                    Server.story.addStoryEl(word);
                    for (ServerSomthing vr : Server.serverList) {
                        vr.send(word);
                    }
                }
            } catch (NullPointerException ignored) {
                System.out.println("Error");
            }
        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
            System.out.println("Error");
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : Server.serverList) {
                    if (vr.equals(this)) {
                        vr.interrupt();
                    }
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {
            System.out.println("Error");
        }
    }
}
