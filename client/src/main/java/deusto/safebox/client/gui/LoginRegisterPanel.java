package deusto.safebox.client.gui;

import deusto.safebox.client.gui.panel.LoginPanel;
import deusto.safebox.client.gui.panel.RegisterPanel;

import java.awt.*;
import javax.swing.*;

public class LoginRegisterPanel extends JFrame {

    private static int WIDTH_WINDOW = 500;
    private static int HEIGHT_WINDOW = 450;

    LoginRegisterPanel() {
        //Window's features
        setPreferredSize(new Dimension(WIDTH_WINDOW, HEIGHT_WINDOW));
        setResizable(false);
        setMinimumSize(new Dimension(WIDTH_WINDOW, HEIGHT_WINDOW));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Initialize visible components
        Container contentPane = getContentPane();
        JTabbedPane tabbedPane = new JTabbedPane();

        LoginPanel pLog = new LoginPanel();
        RegisterPanel pReg = new RegisterPanel();
        //JTabbedPane
        tabbedPane.addTab("Login", pLog);
        tabbedPane.addTab("Register", pReg);
        contentPane.add(tabbedPane);
    }

    public static void main(String[] args) {
        LoginRegisterPanel loginWindow = new LoginRegisterPanel();
        loginWindow.setVisible(true);

        System.out.println();
    }
}
