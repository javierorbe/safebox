package deusto.safebox.client.gui.component;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchBox extends PlaceholderTextField {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchBox.class);

    public SearchBox() {
        super(30, "Search...");
        setMaximumSize(getPreferredSize());
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        LOGGER.trace("Performing search...");
        // TODO
    }
}
