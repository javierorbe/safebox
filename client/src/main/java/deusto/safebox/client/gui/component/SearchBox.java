package deusto.safebox.client.gui.component;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchBox extends PlaceholderTextField {

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
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    private void performSearch() {
        String text = getText();

        if (text.trim().length() == 0) {
            DataTable.searchTitle("");
        } else {
            DataTable.searchTitle("(?i)" + text);
        }
    }
}
