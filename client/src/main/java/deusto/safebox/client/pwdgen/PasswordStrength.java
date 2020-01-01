package deusto.safebox.client.pwdgen;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the strength of a password.
 *
 * @see <a href="https://www.pleacher.com/mp/mlessons/algebra/entropy.html">Password Entropy</a>
 */
public enum PasswordStrength {
    VERY_WEAK("Very Weak", 0, new Color(204, 0, 0)),
    WEAK("Weak", 28, new Color(255, 102, 0)),
    REASONABLE("Reasonable", 36, new Color(204, 255, 102)),
    STRONG("Strong", 60, new Color(0, 153, 0)),
    VERY_STRONG("Very Strong", 128, new Color(0, 51, 0)),
    ;

    private static final List<PasswordStrength> ORDERED_STRENGTH = Arrays.asList(PasswordStrength.values());

    static {
        ORDERED_STRENGTH.sort((s1, s2) -> Integer.compare(s2.minEntropy, s1.minEntropy));
    }

    private final String name;
    private final int minEntropy;
    private final Color color;

    PasswordStrength(String name, int minEntropy, Color color) {
        this.name = name;
        this.minEntropy = minEntropy;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }

    public static PasswordStrength getByEntropy(double entropy) {
        return ORDERED_STRENGTH.stream()
                .filter(s -> entropy >= s.minEntropy)
                .findFirst().orElseThrow();
    }
}
