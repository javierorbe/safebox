package deusto.safebox.client.gui.panel.pwdgen;

import static deusto.safebox.client.gui.panel.pwdgen.PassGenPanel.TopPassGenPanel.passwordField;
import static deusto.safebox.client.gui.panel.pwdgen.PassGenPanel.TopPassGenPanel.progressBar;
import static deusto.safebox.client.gui.panel.pwdgen.PassGenPanel.BotPassGenPanel.PassPanel.CharacterTypesPanel.upper;
import static deusto.safebox.client.gui.panel.pwdgen.PassGenPanel.BotPassGenPanel.PassPanel.CharacterTypesPanel.lower;
import static deusto.safebox.client.gui.panel.pwdgen.PassGenPanel.BotPassGenPanel.PassPanel.CharacterTypesPanel.numbers;
import static deusto.safebox.client.gui.panel.pwdgen.PassGenPanel.BotPassGenPanel.PassPanel.CharacterTypesPanel.symbol;
import static deusto.safebox.client.gui.panel.pwdgen.PassGenPanel.BotPassGenPanel.PassPanel.pLength;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;

import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.gui.GridBagBuilder;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class PassGenPanel extends JPanel {

    static final BotPassGenPanel botPassGenPanel = new BotPassGenPanel();
    static final TopPassGenPanel topPassGenPanel = new TopPassGenPanel();

    static private ArrayList<Character> mayus = new ArrayList<>(Arrays.asList(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));

    static private ArrayList<Character> minus= new ArrayList<>(Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

    static private ArrayList<Character> nums= new ArrayList<>(Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

    static private ArrayList<Character> symbols= new ArrayList<>(Arrays.asList(
            '?', '_', '-', '*' ,'^', '{', '}'));

    static HashMap<Integer, ArrayList<Character>> mapChar = new HashMap<>();

    PassGenPanel() {
        super(new GridBagLayout());

        GridBagBuilder gbb = new GridBagBuilder();
        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(Fill.BOTH, Anchor.CENTER);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(this, topPassGenPanel, gbb);
        put(this, botPassGenPanel, gbb);

        passwordField.setText(generatePassword());
    }

    static class TopPassGenPanel extends JPanel {

        static final PasswordField passwordField = new PasswordField(100, false);
        static final JLabel quality = new JLabel("Quality: ");
        static final JLabel entropy = new JLabel("Entropy: ");
        static final JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        static final JProgressBar progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 30);

        TopPassGenPanel() {
            super(new GridBagLayout());

            final ChangingToggleButton showPasswordBtn = new ChangingToggleButton(
                    IconType.EYE,
                    IconType.EYE_CLOSED,
                    false,
                    passwordField::showPassword,
                    passwordField::hidePassword
            );

            progressBar.setValue(15);

            passwordField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    progressBar.setValue(passwordField.getPassword().length);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    progressBar.setValue(passwordField.getPassword().length);
                }
            });

            //TODO: Adds listeners of quality and entropy to change their values

            GridBagBuilder gbb = new GridBagBuilder();

            gbb.setInsets(4, 4, 4, 4)
                    .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

            gbb.setGridWidthAndWeightX(1, 0);

            put(this, new JLabel("Password: "), gbb);
            gbb.setGridWidthAndWeightX(2, 1);
            put(this, passwordField, gbb);
            gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
            put(this, showPasswordBtn, gbb);

            gbb.setInsetTop(-10)
                    .setGridX(1)
                    .setGridWidthAndWeightX(2,0)
                    .setAnchor(Anchor.NORTH);
            put(this, progressBar, gbb);

            gbb.setInsetTop(4)
                    .setGridX(1)
                    .setGridWidthAndWeightX(1, 1);
            put(this, quality, gbb);
            p.add(entropy);
            gbb.setGridX(2)
                    .setGridWidthAndWeightX(1,0);
            put(this, p, gbb);
        }
    }

    static class BotPassGenPanel extends JPanel{

        static final PassOptionsPanel optionsPanel = new PassOptionsPanel();
        static final PassPanel passPanel = new PassPanel();
        static final PassphrasePanel passphrasePanel = new PassphrasePanel();

        // TODO:  Adds PassphrasePanel's functionality

        BotPassGenPanel() {
            super(new GridBagLayout());

            final JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("Password", passPanel);
            tabbedPane.addTab("Passphrase", passphrasePanel);

            GridBagBuilder gbb = new GridBagBuilder();

            gbb.setInsets(6,6,6,6);

            gbb.setGridX(0).setGridY(0);
            gbb.setGridWidthAndWeightX(1, 1);
            gbb.setGridHeight(3);
            gbb.setFillAndAnchor(GridBagBuilder.Fill.BOTH, GridBagBuilder.Anchor.CENTER);
            put(this, tabbedPane, gbb);

            gbb.setGridX(1);
            gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
            put(this, optionsPanel, gbb);
        }

        static class PassphrasePanel extends JPanel {

            static JSlider sWordCount = new JSlider(JSlider.HORIZONTAL, 8, 100, 15);

            PassphrasePanel() {

                super(new GridBagLayout());

                JLabel wCountLength = new JLabel(String.valueOf(sWordCount.getValue()));
                JTextField wSep = new JTextField();

                sWordCount.addChangeListener(e -> {
                    wCountLength.setText(String.valueOf(sWordCount.getValue()));
                    progressBar.setValue(sWordCount.getValue());
                });

                GridBagBuilder gbb = new GridBagBuilder();
                gbb.setInsets(6,6,6,6);
                gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

                gbb.setGridWidthAndWeightX(1, 0);
                put(this, new JLabel("Word Count: "), gbb);
                gbb.setGridWidthAndWeightX(GridBagConstraints.RELATIVE, 1);
                put(this, sWordCount, gbb);
                gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
                put(this, wCountLength, gbb);
                gbb.setGridWidthAndWeightX(1, 0);
                put(this, new JLabel("Word Separator: "), gbb);
                gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
                put(this, wSep, gbb);
            }
        }


        static class PassOptionsPanel extends JPanel {

            static final JButton close = new JButton("Close");
            static final JButton regenerate = new JButton("Regenerate");
            static final JButton copy = new JButton("Copy");

            PassOptionsPanel() {
                super(new GridBagLayout());

                regenerate.addActionListener(e -> passwordField.setText(generatePassword()));
                copy.addActionListener(e -> {
                    StringSelection stringSelection = new StringSelection(
                            String.valueOf(passwordField.getPassword()));

                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                });

                GridBagBuilder gbb = new GridBagBuilder();

                gbb.setInsets(6,6,6,6);

                gbb.setGridWidthAndWeightX(0, 0);
                gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.EAST);
                put(this, regenerate, gbb);
                gbb.setGridY(1);
                put(this, copy, gbb);
                gbb.setGridY(2);
                put(this, close, gbb);
            }
        }

        static class PassPanel extends JPanel {

            static final JSlider sPassLength = new JSlider(JSlider.HORIZONTAL, 8, 100, 15);
            static final JLabel pLength = new JLabel(String.valueOf(sPassLength.getValue()));
            static final CharacterTypesPanel charTypesPan = new CharacterTypesPanel();

            PassPanel() {
                super(new GridBagLayout());

                sPassLength.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        passwordField.setText(generatePassword());
                        progressBar.setValue(sPassLength.getValue());
                    }
                });

                sPassLength.addChangeListener(e -> pLength.setText(String.valueOf(sPassLength.getValue())));
                GridBagBuilder gbb = new GridBagBuilder();
                gbb.setInsets(6,6,6,6);
                gbb.setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);

                gbb.setGridWidthAndWeightX(1, 0);
                put(this, new JLabel("Length: "), gbb);
                gbb.setGridWidthAndWeightX(GridBagConstraints.RELATIVE, 1);
                put(this, sPassLength, gbb);
                gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
                put(this, pLength, gbb);
                put(this,  charTypesPan, gbb);
            }

            static class CharacterTypesPanel extends JPanel {

                static JToggleButton upper = new JToggleButton("A-Z");
                static JToggleButton lower = new JToggleButton("a-z");
                static JToggleButton numbers = new JToggleButton("0-9");
                static JToggleButton symbol = new JToggleButton("/*_...");

                CharacterTypesPanel() {
                    super(new GridBagLayout());

                    Border border = BorderFactory.createTitledBorder("Character Types");
                    setBorder(border);

                    upper.setSelected(true);
                    lower.setSelected(true);
                    numbers.setSelected(true);

                    GridBagBuilder gbb = new GridBagBuilder();
                    gbb.setInsets(6,6,6,6);
                    put(this, upper, gbb);
                    put(this, lower, gbb);
                    put(this,  numbers, gbb);
                    put(this, symbol, gbb);
                }
            }
        }
    }

    static String generatePassword() {
        StringBuilder password = new StringBuilder();
        int k = 0;

        if (lower.isSelected()) {
            mapChar.put(k++,  minus);
        }
        if (upper.isSelected()) {
            mapChar.put(k++,  mayus);
        }
        if (numbers.isSelected()) {
            mapChar.put(k++,  nums);
        }
        if (symbol.isSelected()) {
            mapChar.put(k++,  symbols);
        }

        for (int i = 0; i < Integer.parseInt(pLength.getText()) && k != 0; i++) {
            ArrayList<Character> aux = mapChar.get((int)Math.floor(Math.random() * k));
            password.append(aux.get((int)Math.floor(Math.random() * aux.size())));
        }

        if (k == 0) {
            JOptionPane.showMessageDialog(null, "You must select password features",
                    "No password features selected", JOptionPane.ERROR_MESSAGE);
        }

        return password.toString();
    }

    public static void put(JPanel panel, JComponent component, GridBagBuilder gbb) {
        panel.add(component, gbb.getConstraints());
    }
}
