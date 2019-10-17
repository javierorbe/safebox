package deusto.safebox.client.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginRegisterWindow extends JFrame {

    private static int WIDTH_WINDOW = 500;
    private static int HEIGHT_WINDOW = 450;
    private static int SHIFT_X_EAST = WIDTH_WINDOW/10;
    private static int SHIFT_Y_NORTH = HEIGHT_WINDOW/10;

    LoginRegisterWindow() {
        //Window's features
        setPreferredSize(new Dimension(WIDTH_WINDOW, HEIGHT_WINDOW));
        setResizable(false);
        setMinimumSize(new Dimension(WIDTH_WINDOW, HEIGHT_WINDOW));
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Initialize visible components
        Container contentPane = getContentPane();
        JPanel login = new JPanel();
        JPanel register = new JPanel();

        Label loginUsername = new Label("Username:");
        Label loginPassword = new Label("Password:");
        JTextField tfLoginUsername = new JTextField("Introduce your LoginUsername",20);
        JTextField tfLoginPassword = new JTextField("Introduce your LoginPassword",20);
        JButton loginButton = new JButton("Login");

        Label registerUsername = new Label("Username:");
        Label registerName = new Label("Name:");
        Label registerPassword = new Label("Password:");
        Label registerRepeatPassword = new Label("RepeatPassword:");
        JTextField tfRegisterUsername = new JTextField("Introduce your RegisterUsername",20);
        JTextField tfRegisterName = new JTextField("Introduce your RegisterName",20);
        JTextField tfRegisterPassword = new JTextField("Introduce your RegisterPassword",20);
        JTextField tfRegisterRepeatPassword = new JTextField("Introduce your RegisterRepeatPassword",20);
        JButton registerButton = new JButton("Create Account");

        JTabbedPane tabbedPane = new JTabbedPane();

        //Size components Login
        loginUsername.setPreferredSize(new Dimension(100, 35));
        loginPassword.setPreferredSize(new Dimension(100,35));

        tfLoginUsername.setHorizontalAlignment(JTextField.CENTER);
        tfLoginPassword.setHorizontalAlignment(JTextField.CENTER);

        JPanel panelLoginUser = new JPanel(new FlowLayout());
        JPanel panelLoginPassword = new JPanel(new FlowLayout());

        panelLoginUser.add(loginUsername);
        panelLoginUser.add(tfLoginUsername);
        panelLoginPassword.add(loginPassword);
        panelLoginPassword.add(tfLoginPassword);

        //Size components Register
        registerName.setPreferredSize(new Dimension(100, 35));
        registerUsername.setPreferredSize(new Dimension(100, 35));
        registerPassword.setPreferredSize(new Dimension(100,35));
        registerRepeatPassword.setPreferredSize(new Dimension(100,35));

        tfRegisterName.setHorizontalAlignment(JTextField.CENTER);
        tfRegisterUsername.setHorizontalAlignment(JTextField.CENTER);
        tfRegisterPassword.setHorizontalAlignment(JTextField.CENTER);
        tfRegisterRepeatPassword.setHorizontalAlignment(JTextField.CENTER);

        JPanel panelRegisterName = new JPanel(new FlowLayout());
        JPanel panelRegisterUser = new JPanel(new FlowLayout());
        JPanel panelRegisterPassword = new JPanel(new FlowLayout());
        JPanel panelRegisterRepeatPassword = new JPanel(new FlowLayout());

        panelRegisterName.add(registerName);
        panelRegisterName.add(tfRegisterName);
        panelRegisterUser.add(registerUsername);
        panelRegisterUser.add(tfRegisterUsername);
        panelRegisterPassword.add(registerPassword);
        panelRegisterPassword.add(tfRegisterPassword);
        panelRegisterRepeatPassword.add(registerRepeatPassword);
        panelRegisterRepeatPassword.add(tfRegisterRepeatPassword);

        //JTabbedPane
        tabbedPane.addTab("Login", login);
        tabbedPane.addTab("Register", register);

        SpringLayout sLayout = new SpringLayout();

        //REGISTER
        {
            register.setLayout(sLayout);

            sLayout.putConstraint(SpringLayout.WEST, panelRegisterName, SHIFT_X_EAST, SpringLayout.WEST, register);
            sLayout.putConstraint(SpringLayout.EAST, panelRegisterName, -SHIFT_X_EAST, SpringLayout.EAST, register);
            sLayout.putConstraint(SpringLayout.NORTH, panelRegisterName, SHIFT_Y_NORTH, SpringLayout.NORTH, register);

            sLayout.putConstraint(SpringLayout.WEST, panelRegisterUser, SHIFT_X_EAST, SpringLayout.WEST, register);
            sLayout.putConstraint(SpringLayout.EAST, panelRegisterUser, -SHIFT_X_EAST, SpringLayout.EAST, register);
            sLayout.putConstraint(SpringLayout.NORTH, panelRegisterUser, 2*SHIFT_Y_NORTH, SpringLayout.NORTH, register);

            sLayout.putConstraint(SpringLayout.WEST, panelRegisterPassword, SHIFT_X_EAST, SpringLayout.WEST, register);
            sLayout.putConstraint(SpringLayout.EAST, panelRegisterPassword, -SHIFT_X_EAST, SpringLayout.EAST, register);
            sLayout.putConstraint(SpringLayout.NORTH, panelRegisterPassword, 3*SHIFT_Y_NORTH, SpringLayout.NORTH, register);

            sLayout.putConstraint(SpringLayout.WEST, panelRegisterRepeatPassword, SHIFT_X_EAST, SpringLayout.WEST, register);
            sLayout.putConstraint(SpringLayout.EAST, panelRegisterRepeatPassword, -SHIFT_X_EAST, SpringLayout.EAST, register);
            sLayout.putConstraint(SpringLayout.NORTH, panelRegisterRepeatPassword, 4*SHIFT_Y_NORTH, SpringLayout.NORTH, register);

            sLayout.putConstraint(SpringLayout.WEST, registerButton, 3*SHIFT_X_EAST, SpringLayout.WEST, register);
            sLayout.putConstraint(SpringLayout.EAST, registerButton, 3*-SHIFT_X_EAST, SpringLayout.EAST, register);
            sLayout.putConstraint(SpringLayout.NORTH, registerButton, 6*SHIFT_Y_NORTH, SpringLayout.NORTH, register);

            register.add(panelRegisterName);
            register.add(panelRegisterUser);
            register.add(panelRegisterPassword);
            register.add(panelRegisterRepeatPassword);
            register.add(registerButton);
        }

        //LOGIN
        {
            login.setLayout(sLayout);

            sLayout.putConstraint(SpringLayout.WEST, panelLoginUser, SHIFT_X_EAST, SpringLayout.WEST, login);
            sLayout.putConstraint(SpringLayout.EAST, panelLoginUser, -SHIFT_X_EAST, SpringLayout.EAST, login);
            sLayout.putConstraint(SpringLayout.NORTH, panelLoginUser, 2*SHIFT_Y_NORTH, SpringLayout.NORTH, login);

            sLayout.putConstraint(SpringLayout.WEST, panelLoginPassword, SHIFT_X_EAST, SpringLayout.WEST, login);
            sLayout.putConstraint(SpringLayout.EAST, panelLoginPassword, -SHIFT_X_EAST, SpringLayout.EAST, login);
            sLayout.putConstraint(SpringLayout.NORTH, panelLoginPassword, 3*SHIFT_Y_NORTH, SpringLayout.NORTH, login);

            sLayout.putConstraint(SpringLayout.WEST, loginButton, 3*SHIFT_X_EAST, SpringLayout.WEST, login);
            sLayout.putConstraint(SpringLayout.EAST, loginButton, 3*-SHIFT_X_EAST, SpringLayout.EAST, login);
            sLayout.putConstraint(SpringLayout.NORTH, loginButton, 6*SHIFT_Y_NORTH, SpringLayout.NORTH, login);

            login.add(panelLoginUser);
            login.add(panelLoginPassword);
            login.add(loginButton);
        }


        contentPane.add(tabbedPane);
    }

    public static void main(String[] args) {
        LoginRegisterWindow loginWindow = new LoginRegisterWindow();
        loginWindow.setVisible(true);
    }
}
