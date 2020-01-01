package deusto.safebox.client.gui.panel.pwdgen;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.gui.component.LimitedTextField;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.gui.component.ToggleButton;
import deusto.safebox.client.pwdgen.PassphraseGenerator;
import deusto.safebox.client.pwdgen.PasswordGenerator;
import deusto.safebox.client.pwdgen.PasswordStrength;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.RightAlignedLabel;
import deusto.safebox.common.gui.SimpleButton;
import deusto.safebox.common.util.GuiUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import me.gosimple.nbvcxz.Nbvcxz;

public class PasswordGenDialog extends JDialog {

    private static final Nbvcxz NBVCXZ = new Nbvcxz();
    private final ExecutorService entropyCalculationService = Executors.newSingleThreadExecutor();
    private Future<?> entropyCalculation;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");
    private static final int DEFAULT_PASSWORD_LENGTH = 12;
    private static final int MAX_PASSWORD_LENGTH = 128;
    private static final int MAX_PASSPHRASE_LENGTH = 40;

    private final GridBagBuilder gbb = new GridBagBuilder();
    private final JPanel contentPanel = new JPanel(new GridBagLayout());

    private final PasswordField passwordField = new PasswordField("", true);
    private final JProgressBar passwordSecurity = new JProgressBar(0, 200);
    private final JLabel passwordQuality = new JLabel();
    private final JLabel passwordEntropy = new JLabel();

    private final PasswordGenerator.Options options = new PasswordGenerator.Options();
    private int passphraseLength = 4;
    private String wordSeparator = " ";

    public PasswordGenDialog(JFrame owner) {
        super(owner, "Password Generator", false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        ToggleButton showPasswordBtn = new ToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                true,
                passwordField::showPassword,
                passwordField::hidePassword
        );

        passwordField.getDocument().addDocumentListener(new DocumentAdapter(this::updatePasswordInfo));

        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST)
                .setInsets(4, 4, 4, 4)
                .setGridWidthAndWeightX(1, 0)
                .setGridX(0);
        put(new JLabel("Password:"));
        gbb.setGridX(1)
                .setGridWidthAndWeightX(2, 1);
        put(passwordField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0)
                .setGridX(3);
        put(showPasswordBtn);

        passwordSecurity.setBorder(BorderFactory.createEmptyBorder());
        passwordSecurity.setBorderPainted(false);
        passwordSecurity.setStringPainted(true);
        gbb.setGridX(1)
                .setGridWidth(2)
                .setInsets(0, 4, 0, 4);
        put(passwordSecurity);
        passwordSecurity.setValue(25); // TEMP

        gbb.setGridX(1)
                .setGridWidthAndWeightX(1, 0);
        put(passwordQuality);

        JPanel entropyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        entropyPanel.add(passwordEntropy);
        gbb.setGridX(2);
        put(entropyPanel);

        gbb.setGridX(0)
                .setGridWidthAndWeightX(4, 1);
        put(new MainGeneratorPanel());

        contentPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        add(contentPanel, BorderLayout.CENTER);

        regeneratePassword();

        pack();
        setLocation(GuiUtil.getCenteredLocation(owner, this));
        setVisible(true);
    }

    private void put(JComponent component) {
        contentPanel.add(component, gbb.getConstraints());
    }

    private void regeneratePassword() {
        passwordField.setText(PasswordGenerator.generate(options));
    }

    private void regeneratePassphrase() {
        passwordField.setText(PassphraseGenerator.generate(passphraseLength, wordSeparator));
    }

    private void updatePasswordInfo() {
        String password = new String(passwordField.getPassword());

        if (entropyCalculation != null) {
            entropyCalculation.cancel(true);
        }

        entropyCalculation = entropyCalculationService.submit(() -> {
            double entropy = NBVCXZ.estimate(password).getEntropy();
            PasswordStrength strength = PasswordStrength.getByEntropy(entropy);

            SwingUtilities.invokeLater(() -> {
                passwordEntropy.setText("Entropy: " + DECIMAL_FORMAT.format(entropy) + " bit");
                passwordQuality.setText("Password Quality: " + strength);
                passwordSecurity.setString("");
                passwordSecurity.setValue((int) entropy);
                passwordSecurity.setForeground(strength.getColor());
            });
        });
    }

    private class MainGeneratorPanel extends JPanel {

        private final GridBagBuilder gbb = new GridBagBuilder();

        private MainGeneratorPanel() {
            super(new GridBagLayout());

            gbb.setInsets(4, 4, 4, 4);
            gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

            JTabbedPane pwdTypePane = new JTabbedPane();
            pwdTypePane.addTab("Password", new PasswordPanel());
            pwdTypePane.addTab("Passphrase", new PassphrasePanel());

            JPanel btnPanel = new JPanel(new GridBagLayout());
            GridBagBuilder btnGbb = new GridBagBuilder()
                    .setFill(Fill.HORIZONTAL)
                    .setGridWidth(GridBagConstraints.REMAINDER)
                    .setInsets(2, 0, 2, 0);
            btnPanel.add(new SimpleButton("Regenerate", () -> {
                if (pwdTypePane.getSelectedIndex() == 0) {
                    regeneratePassword();
                } else {
                    regeneratePassphrase();
                }
            }), btnGbb.getConstraints());
            btnPanel.add(new SimpleButton("Copy", this::copyPasswordToClipboard), btnGbb.getConstraints());
            btnPanel.add(new SimpleButton("Close", PasswordGenDialog.this::dispose), btnGbb.getConstraints());

            gbb.setGridWidthAndWeightX(1, 1);
            put(pwdTypePane);
            gbb.setWeightX(0);
            put(btnPanel);
        }

        private void put(JComponent component) {
            add(component, gbb.getConstraints());
        }

        private void copyPasswordToClipboard() {
            StringSelection stringSelection = new StringSelection(new String(passwordField.getPassword()));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

    private class PasswordPanel extends JPanel {

        private final GridBagBuilder gbb = new GridBagBuilder();

        private PasswordPanel() {
            super(new GridBagLayout());
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            int minPasswordLength = 1;

            SliderAndSpinner lengthSelector = new SliderAndSpinner(minPasswordLength,
                    MAX_PASSWORD_LENGTH, DEFAULT_PASSWORD_LENGTH, length -> {
                options.setLength(length);
                regeneratePassword();
            });

            JPanel characterTypePanel = new JPanel();
            characterTypePanel.setBorder(BorderFactory.createTitledBorder("Character Types"));
            characterTypePanel.add(new ToggleButton("A-Z", "Upper Case Letters",
                    options.isUpperCase(), options::setUpperCase));
            characterTypePanel.add(new ToggleButton("a-z", "Lower Case Letters",
                    options.isLowerCase(), options::setLowerCase));
            characterTypePanel.add(new ToggleButton("0-9", "Numbers",
                    options.isNumbers(), options::setNumbers));
            characterTypePanel.add(new ToggleButton("/*_...", "Special Characters",
                    options.isSpecial(), options::setSpecial));

            gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST)
                    .setInsets(0, 4, 0, 4)
                    .setGridWidthAndWeightX(1, 0);
            put(new JLabel("Length:"));
            gbb.setWeightX(1);
            put(lengthSelector.getSlider());
            gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
            put(lengthSelector.getSpinner());

            put(characterTypePanel);
        }

        private void put(JComponent component) {
            add(component, gbb.getConstraints());
        }
    }

    private class PassphrasePanel extends JPanel {

        private final GridBagBuilder gbb = new GridBagBuilder();

        private PassphrasePanel() {
            super(new GridBagLayout());

            SliderAndSpinner countSelector = new SliderAndSpinner(1,
                    MAX_PASSPHRASE_LENGTH, 4, wordCount -> {
                passphraseLength = wordCount;
                regeneratePassphrase();
            });

            gbb.setFill(Fill.HORIZONTAL)
                    .setInsets(0, 4, 0, 4)
                    .setGridWidthAndWeightX(1, 0);
            JLabel wordCountLabel = new RightAlignedLabel("Word Count:");
            put(wordCountLabel);
            gbb.setWeightX(1);
            put(countSelector.getSlider());
            gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
            put(countSelector.getSpinner());

            JTextField wordSeparatorField = new LimitedTextField(30, wordSeparator, true);
            wordSeparatorField.getDocument()
                    .addDocumentListener(new DocumentAdapter(() -> {
                        wordSeparator = wordSeparatorField.getText();
                        regeneratePassphrase();
                    }));

            gbb.setGridWidthAndWeightX(1, 0);
            put(new JLabel("Word Separator:"));
            gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
            put(wordSeparatorField);
        }

        private void put(JComponent component) {
            add(component, gbb.getConstraints());
        }
    }
}
