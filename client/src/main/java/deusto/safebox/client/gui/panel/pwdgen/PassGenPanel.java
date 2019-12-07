package deusto.safebox.client.gui.panel.pwdgen;

import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;

import deusto.safebox.common.gui.GridBagBuilder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class PassGenPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();
    final BotPassGenPanel botPassGenPanel = new BotPassGenPanel();
    final TopPassGenPanel topPassGenPanel = new TopPassGenPanel();

    private ArrayList<Character> mayus = new ArrayList<>(Arrays.asList(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));

    private ArrayList<Character> minus= new ArrayList<>(Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

    private ArrayList<Character> nums= new ArrayList<>(Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

    private ArrayList<Character> symbols= new ArrayList<>(Arrays.asList(
            '?', '_', '-', '*' ,'^', '{', '}'));

    HashMap<Integer, ArrayList<Character>> mapChar = new HashMap<>();

    PassGenPanel() {

        super(new GridBagLayout());


        //TODO: Modify structure of listeners
        topPassGenPanel.passwordField.setText(generatePassword());

        botPassGenPanel.optionsPanel.regenerate.addActionListener(
                e -> topPassGenPanel.passwordField.setText(generatePassword()));

        botPassGenPanel.passPanel.sPassLength.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                topPassGenPanel.passwordField.setText(generatePassword());
            }
        });

        botPassGenPanel.optionsPanel.copy.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(
                    String.valueOf(topPassGenPanel.passwordField.getPassword()));

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(Fill.BOTH, Anchor.CENTER);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(topPassGenPanel);
        put(botPassGenPanel);
    }

    String generatePassword() {
        StringBuilder password = new StringBuilder();
        int k = 0;

        if (botPassGenPanel.passPanel.charTypesPan.lower.isSelected()) {
            mapChar.put(k++,  minus);
        }
        if (botPassGenPanel.passPanel.charTypesPan.upper.isSelected()) {
            mapChar.put(k++,  mayus);
        }
        if (botPassGenPanel.passPanel.charTypesPan.numbers.isSelected()) {
            mapChar.put(k++,  nums);
        }
        if (botPassGenPanel.passPanel.charTypesPan.symbols.isSelected()) {
            mapChar.put(k++,  symbols);
        }

        for (int i = 0; i < Integer.parseInt(botPassGenPanel.passPanel.pLength.getText()) && k != 0; i++) {
            ArrayList<Character> aux = mapChar.get((int)Math.floor(Math.random() * k));
            password.append(aux.get((int)Math.floor(Math.random() * aux.size())));
        }

        if (k == 0) {
            JOptionPane.showMessageDialog(this, "You must select password features",
                    "No password features selected", JOptionPane.ERROR_MESSAGE);
        }

        return password.toString();
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
