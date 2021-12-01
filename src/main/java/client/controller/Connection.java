package client.controller;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Connection implements Closeable {

    private final Socket socket;
    private final ObjectInputStream in;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public String receive() throws IOException, ClassNotFoundException {
        return (String) in.readObject();
    }

    public void close() throws IOException {
        in.close();
        socket.close();
    }
}
