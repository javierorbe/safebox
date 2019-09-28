package deusto.safebox.client.gui;

import javax.swing.*;

public abstract class MainWindow extends JFrame {

    protected MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 320);

        JPanel panel = new JPanel();

        JButton connectBtn = new JButton("Connect");
        JButton sendBtn = new JButton("Send");

        connectBtn.addActionListener(e -> connect());
        sendBtn.addActionListener(e -> send());

        panel.add(connectBtn);
        panel.add(sendBtn);

        getContentPane().add(panel);

        setVisible(true);
    }

    protected abstract void connect();

    protected abstract void send();
}
