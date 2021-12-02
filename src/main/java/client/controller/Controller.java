package client.controller;

import client.model.Model;
import client.model.Point;
import client.view.View;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Controller {

    protected Connection connection;

    private Model model = new Model();
    private View view = new View(this);

    public void run() {
        try {
            Socket socket = new Socket(getServerAddress(), getServerPort());
            connection = new Connection(socket);
            notifyConnectionStatusChanged(true);
            clientMainLoop();
        } catch (Exception e) {
            notifyConnectionStatusChanged(false);
            run();
        }
    }

    protected void notifyConnectionStatusChanged(boolean clientConnected) {
        view.notifyConnectionStatusChanged(clientConnected);
    }

    protected void clientMainLoop() throws IOException, ClassNotFoundException {
        while (true) {
            String message = connection.receive();
            if (isValidMessage(message)) {
                processIncomingMessage(message);
            }
        }
    }

    protected void processIncomingMessage(String message) {
        String[] parts = message.split(";");
        if (parts[1].equals("start")) {
            Point point = new Point(Double.parseDouble(parts[2]) * model.getMaxX(),
                    Double.parseDouble(parts[3]) * model.getMaxY());
            CopyOnWriteArrayList<Point> newPointList = new CopyOnWriteArrayList<>();
            newPointList.add(point);
            model.getAllPointLists().add(newPointList);
        } else if (parts[1].equals("move")) {
            client.model.Point point = new Point(Double.parseDouble(parts[2]) * model.getMaxX(),
                    Double.parseDouble(parts[3]) * model.getMaxY());
            model.getLastPointList().add(point);
            view.update();
        }
    }

    protected boolean isValidMessage(String message) {
        if (message != null &&
                message.matches("([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2};(move|start);((0[.,][0-9]+|1[.,]0+);){2}-?[0-9]+")) {
            return true;
        }
        return false;
    }

    protected String getServerAddress() {
        return view.getServerAddress();
    }

    protected int getServerPort() {
        return view.getServerPort();
    }

    public void setMaxX(int maxX) {
        model.setMaxX(maxX);
    }

    public void setMaxY(int maxY) {
        model.setMaxY(maxY);
    }

    public void drawLines(Graphics2D graphics2D) {
        model.drawLines(graphics2D);
    }
}
