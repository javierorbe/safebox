package deusto.safebox.client.gui.panel.pwdgen;

import static deusto.safebox.client.util.PasswordGenerator.generatePassphrase;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.client.util.PasswordGenerator.generatePassword;

import deusto.safebox.client.gui.component.ToggleButton;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.gui.GridBagBuilder;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
import java.awt.event.*;

public class PassGenPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    static final BotPassGenPanel botPassGenPanel = new BotPassGenPanel();
    static final TopPassGenPanel topPassGenPanel = new TopPassGenPanel();

    static final Runnable runnablePass = () -> {
        if (BotPassGenPanel.tabbedPane.getSelectedIndex() == 0) {
            TopPassGenPanel.passwordField.setText(generatePassword(
                    PassPanel.sPassLength.getValue(),
                    CharacterTypesPanel.buttonNum.isSelected(),
                    CharacterTypesPanel.buttonSymbols.isSelected(),
                    CharacterTypesPanel.buttonLower.isSelected(),
                    CharacterTypesPanel.buttonUpper.isSelected()
            ));
        } else {
            TopPassGenPanel.passwordField.setText(generatePassphrase(
                    PassphrasePanel.sWordCount.getValue(),
                    PassphrasePanel.wSep.getText()
            ));
        }

    };

    PassGenPanel() {
        super(new GridBagLayout());

        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(Fill.BOTH, Anchor.CENTER);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(topPassGenPanel);
        put(botPassGenPanel);

        runnablePass.run();
    }

    public void put(JComponent component) {
        this.add(component, gbb.getConstraints());
    }
}

class TopPassGenPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    static final PasswordField passwordField = new PasswordField(200, false);
    static final JLabel quality = new JLabel("Quality: ");
    static final JLabel entropy = new JLabel("Entropy: ");
    static final JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    static final JProgressBar progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 30);

    TopPassGenPanel() {
        super(new GridBagLayout());

        final ToggleButton showPasswordBtn = new ToggleButton(
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

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);

        put(new JLabel("Password: "));
        gbb.setGridWidthAndWeightX(2, 1);
        put(passwordField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(showPasswordBtn);

        gbb.setInsetTop(-10)
                .setGridX(1)
                .setGridWidthAndWeightX(2,0)
                .setAnchor(Anchor.NORTH);
        put(progressBar);

        gbb.setInsetTop(4)
                .setGridX(1)
                .setGridWidthAndWeightX(1, 1);
        put(quality);
        p.add(entropy);
        gbb.setGridX(2)
                .setGridWidthAndWeightX(1,0);
        put(p);
    }

    public void put(JComponent component) {
        this.add(component, gbb.getConstraints());
    }
}

class BotPassGenPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    static final PassOptionsPanel optionsPanel = new PassOptionsPanel();
    static final PassPanel passPanel = new PassPanel();
    static final PassphrasePanel passphrasePanel = new PassphrasePanel();
    static final JTabbedPane tabbedPane = new JTabbedPane();

    BotPassGenPanel() {
        super(new GridBagLayout());

        tabbedPane.addTab("Password", passPanel);
        tabbedPane.addTab("Passphrase", passphrasePanel);

        gbb.setInsets(6, 6, 6, 6);

        gbb.setGridX(0).setGridY(0);
        gbb.setGridWidthAndWeightX(1, 1);
        gbb.setGridHeight(3);
        gbb.setFillAndAnchor(GridBagBuilder.Fill.BOTH, GridBagBuilder.Anchor.CENTER);
        put(tabbedPane);

        gbb.setGridX(1);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(optionsPanel);
    }

    public void put(JComponent component) {
        this.add(component, gbb.getConstraints());
    }
}

class PassphrasePanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    static final JSlider sWordCount = new JSlider(JSlider.HORIZONTAL, 8, 20, 10);
    static final JLabel wCountLength = new JLabel(String.valueOf(sWordCount.getValue()));
    static final JTextField wSep = new JTextField();

    PassphrasePanel() {
        super(new GridBagLayout());

        sWordCount.addChangeListener(e -> {
            wCountLength.setText(String.valueOf(sWordCount.getValue()));
            TopPassGenPanel.progressBar.setValue(sWordCount.getValue());
        });

        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Word Count: "));
        gbb.setGridWidthAndWeightX(GridBagConstraints.RELATIVE, 1);
        put(sWordCount);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(wCountLength);
        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Word Separator: "));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(wSep);
    }

    public void put(JComponent component) {
        this.add(component, gbb.getConstraints());
    }
}

class CharacterTypesPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    static final JToggleButton buttonUpper = new JToggleButton("A-Z");
    static final JToggleButton buttonLower = new JToggleButton("a-z");
    static final JToggleButton buttonNum = new JToggleButton("0-9");
    static final JToggleButton buttonSymbols = new JToggleButton("/*_...");

    CharacterTypesPanel() {
        super(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Character Types");
        setBorder(border);

        buttonUpper.setSelected(true);
        buttonLower.setSelected(true);
        buttonNum.setSelected(true);

        gbb.setInsets(6, 6, 6, 6);
        put(buttonUpper);
        put(buttonLower);
        put(buttonNum);
        put(buttonSymbols);
    }

    public void put(JComponent component) {
        this.add(component, gbb.getConstraints());
    }
}

class PassPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    static final JSlider sPassLength = new JSlider(JSlider.HORIZONTAL, 8, 100, 15);
    static final JLabel pLength = new JLabel(String.valueOf(sPassLength.getValue()));
    static final CharacterTypesPanel charTypesPan = new CharacterTypesPanel();

    PassPanel() {
        super(new GridBagLayout());

        sPassLength.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                PassGenPanel.runnablePass.run();
                TopPassGenPanel.progressBar.setValue(sPassLength.getValue());
            }
        });

        sPassLength.addChangeListener(e -> pLength.setText(String.valueOf(sPassLength.getValue())));

        gbb.setInsets(6, 6, 6, 6);
        gbb.setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Length: "));
        gbb.setGridWidthAndWeightX(GridBagConstraints.RELATIVE, 1);
        put(sPassLength);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(pLength);
        put(charTypesPan);
    }

    public void put(JComponent component) {
        this.add(component, gbb.getConstraints());
    }
}

class PassOptionsPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    static final JButton close = new JButton("Close");
    static final JButton regenerate = new JButton("Regenerate");
    static final JButton copy = new JButton("Copy");

    PassOptionsPanel() {
        super(new GridBagLayout());

        regenerate.addActionListener(e -> {
            PassGenPanel.runnablePass.run();
            TopPassGenPanel.progressBar.setValue(TopPassGenPanel.passwordField.getPassword().length);
        });
        copy.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(
                    String.valueOf(TopPassGenPanel.passwordField.getPassword()));

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });



        gbb.setInsets(6, 6, 6, 6);

        gbb.setGridWidthAndWeightX(0, 0);
        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.EAST);
        put(regenerate);
        gbb.setGridY(1);
        put(copy);
        gbb.setGridY(2);
        put(close);
    }

    public void put(JComponent component) {
        this.add(component, gbb.getConstraints());
    }
}