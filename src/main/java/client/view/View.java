package client.view;

import client.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class View {

    private JFrame frame = new JFrame("Общая доска");
    private Board board = new Board();

    private Controller controller;

    public View(Controller controller) {
        this.controller = controller;
        initView();
    }

    private void initView() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        frame.add(board, BorderLayout.CENTER);
        frame.setVisible(true);

        board.setBackground(Color.WHITE);
        board.setSize(frame.getWidth(), frame.getHeight());

        controller.setMaxX(board.getWidth());
        controller.setMaxY(board.getHeight());

    }

    public class Board extends JPanel {

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setColor(Color.BLACK);

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            controller.drawLines(graphics2D);
        }
    }

    public String getServerAddress() {
        String serverAddress = JOptionPane.showInputDialog(
                frame,
                "Введите адрес сервера:",
                "Конфигурация клиента",
                JOptionPane.QUESTION_MESSAGE);
        if (serverAddress == null) {
            System.exit(0);
        }
        return serverAddress;
    }

    public int getServerPort() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    "Введите порт сервера:",
                    "Конфигурация клиента",
                    JOptionPane.QUESTION_MESSAGE);
            if (port == null) {
                System.exit(0);
            }
            try {
                return Integer.parseInt(port.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Был введен некорректный порт сервера. Попробуйте еще раз.",
                        "Конфигурация клиента",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {
        if (clientConnected) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Соединение с сервером установлено",
                    "Общая доска",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Клиент не подключен к серверу",
                    "Общая доска",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void update() {
        board.repaint();
    }
}
