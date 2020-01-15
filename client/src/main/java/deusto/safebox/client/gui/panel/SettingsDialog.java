package deusto.safebox.client.gui.panel;

import static deusto.safebox.client.gui.ThemeSelector.Theme;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.ClientMain;
import deusto.safebox.client.gui.ThemeSelector;
import deusto.safebox.client.locale.Language;
import deusto.safebox.client.locale.Message;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.RightAlignedLabel;
import deusto.safebox.common.util.GuiUtil;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SettingsDialog extends JDialog {

    private final JComboBox<Language> langSelector;
    private final JComboBox<ThemeSelector.Theme> themeSelector;

    public SettingsDialog(JFrame owner) {
        super(owner, Message.SETTINGS.get(), true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagBuilder gbb = new GridBagBuilder();

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        /*
        {
            JPanel startup = new JPanel(new GridBagLayout());
            startup.setBorder(BorderFactory.createTitledBorder("Startup"));
            GridBagBuilder gbb2 = new GridBagBuilder();

            gbb2.setGridWidth(GridBagConstraints.REMAINDER)
                    .setInsets(2, 2, 2, 2);

            startup.add(new JCheckBox("Start only a single instance of SafeBox"), gbb2.getConstraints());
            startup.add(new JCheckBox("Minimize window at application startup"), gbb2.getConstraints());

            settingsPanel.add(startup);
        }
        */

        {
            JPanel appearance = new JPanel(new GridBagLayout());
            appearance.setBorder(BorderFactory.createTitledBorder(Message.APPEARANCE.get()));
            GridBagBuilder gbb2 = new GridBagBuilder();
            gbb2.setInsets(2, 2, 2, 2)
                    .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

            gbb2.setGridWidthAndWeightX(1, 0);
            appearance.add(new RightAlignedLabel(Message.LANGUAGE.get() + ":"), gbb2.getConstraints());
            langSelector = new JComboBox<>(Language.values());
            langSelector.setFocusable(false);
            gbb2.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
            appearance.add(langSelector, gbb2.getConstraints());

            gbb2.setGridWidthAndWeightX(1, 0);
            appearance.add(new RightAlignedLabel(Message.THEME.get() + ":"), gbb2.getConstraints());
            themeSelector = new JComboBox<>(ThemeSelector.Theme.values());
            themeSelector.setFocusable(false);
            gbb2.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
            appearance.add(themeSelector, gbb2.getConstraints());

            settingsPanel.add(appearance);
        }

        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST)
                .setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        add(settingsPanel, gbb.getConstraints());

        gbb.setAnchor(Anchor.PAGE_END)
                .setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        add(new ButtonPanel(
            () -> {
                save();
                dispose();
            },
            this::dispose,
            this::save
        ), gbb.getConstraints());

        load();

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }

    private void load() {
        String langCode = ClientMain.CONFIG.getString("lang");
        Language language = Language.fromCode(langCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid language code: " + langCode));
        langSelector.setSelectedItem(language);

        String themeId = ClientMain.CONFIG.getString("theme");
        Theme theme = Theme.fromId(themeId);
        themeSelector.setSelectedItem(theme);
    }

    private void save() {
        Language selectedLang = (Language) langSelector.getSelectedItem();
        ClientMain.CONFIG.setString("lang", Objects.requireNonNull(selectedLang).getCode());

        ThemeSelector.Theme selectedTheme = (ThemeSelector.Theme) themeSelector.getSelectedItem();
        if (selectedTheme != null) {
            ClientMain.CONFIG.setString("theme", selectedTheme.getId());
        }

        ClientMain.CONFIG.save();
    }
}
