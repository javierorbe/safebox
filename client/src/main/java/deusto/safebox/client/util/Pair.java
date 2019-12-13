package deusto.safebox.client.util;

/**
 * Pair of values.
 *
 * @param <L> the type of the first value
 * @param <R> the type of the second value
 */
public class Pair<L, R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}
