package deusto.safebox.client.util;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PasswordGenerator {

    private static ArrayList<Character> upper = new ArrayList<>(Arrays.asList(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));

    private static ArrayList<Character> lower = new ArrayList<>(Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

    private static ArrayList<Character> numbers = new ArrayList<>(Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

    private static ArrayList<Character> symbols = new ArrayList<>(Arrays.asList(
            '?', '_', '-', '*' ,'^', '{', '}'));

    private static HashMap<Integer, ArrayList<Character>> mapChar = new HashMap<>();

    private static ArrayList<String> words  = new ArrayList<>(Arrays.asList(
            "mail","broken","operation","forth","brave","officer",
            "feathers","barn","rear","building","leader","light",
            "wood","die","go","tried","refused","stream",
            "table","stems","it","machinery","principal","boy",
            "victory","seven","brought","discover","wise","running",
            "pink","red","college","earn","out","bus",
            "gun","raw","noun","larger","tight","several"
    ));

    public static String generatePassword(int passLength, boolean... values) {

        StringBuilder password = new StringBuilder();
        int k = 0;

        if (values[0]) {
            mapChar.put(k++, lower);
        }
        if (values[1]) {
            mapChar.put(k++, upper);
        }
        if (values[2]) {
            mapChar.put(k++, numbers);
        }
        if (values[3]) {
            mapChar.put(k++,  symbols);
        }

        for (int i = 0; i < passLength && k != 0; i++) {
            ArrayList<Character> aux = mapChar.get((int)Math.floor(Math.random() * k));
            password.append(aux.get((int)Math.floor(Math.random() * aux.size())));
        }

        if (k == 0) {
            JOptionPane.showMessageDialog(null, "You must select password features",
                    "No password features selected", JOptionPane.ERROR_MESSAGE);
        }

        return password.toString();
    }

    public static String generatePassphrase(int passLength, String separator) {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passLength - 1; i++) {
            password.append(words.get((int)Math.floor(Math.random() * words.size())));
            password.append(separator);
        }
        password.append(words.get((int)Math.floor(Math.random() * words.size())));

        return password.toString();
    }
}
