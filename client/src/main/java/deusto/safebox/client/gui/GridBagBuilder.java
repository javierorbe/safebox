package deusto.safebox.client.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * {@link GridBagConstraints} builder utility.
 */
public class GridBagBuilder {

    private GridBagConstraints gbc;

    public GridBagBuilder(GridBagConstraints constraints) {
        this.gbc = constraints;
    }

    public GridBagBuilder() {
        this(new GridBagConstraints());
    }

    public GridBagConstraints getConstraints() {
        return gbc;
    }

    public GridBagBuilder reset() {
        gbc = new GridBagConstraints();
        return this;
    }

    public GridBagBuilder setGridX(int value) {
        gbc.gridx = value;
        return this;
    }

    public GridBagBuilder setGridY(int value) {
        gbc.gridy = value;
        return this;
    }

    public GridBagBuilder incrementGridX() {
        gbc.gridx += 1;
        return this;
    }

    public GridBagBuilder incrementGridY() {
        gbc.gridy += 1;
        return this;
    }

    public GridBagBuilder setWeightX(int value) {
        gbc.weightx = value;
        return this;
    }

    public GridBagBuilder setWeightY(int value) {
        gbc.weighty = value;
        return this;
    }

    public GridBagBuilder setGridWidth(int value) {
        gbc.gridwidth = value;
        return this;
    }

    public GridBagBuilder setGridHeight(int value) {
        gbc.gridheight = value;
        return this;
    }

    public GridBagBuilder setGridWidthAndWeightX(int gridWidth, int weightX) {
        gbc.gridwidth = gridWidth;
        gbc.weightx = weightX;
        return this;
    }

    public GridBagBuilder setFillAndAnchor(Fill fillMode, Anchor anchor) {
        gbc.fill = fillMode.id;
        gbc.anchor = anchor.id;
        return this;
    }

    public GridBagBuilder setFill(Fill fillMode) {
        gbc.fill = fillMode.id;
        return this;
    }

    public GridBagBuilder setAnchor(Anchor anchor) {
        gbc.anchor = anchor.id;
        return this;
    }

    public GridBagBuilder setInsets(int top, int left, int bottom, int right) {
        gbc.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public enum Fill {
        NONE(GridBagConstraints.NONE),
        HORIZONTAL(GridBagConstraints.HORIZONTAL),
        VERTICAL(GridBagConstraints.VERTICAL),
        BOTH(GridBagConstraints.BOTH),
        ;

        private int id;

        Fill(int id) {
            this.id = id;
        }
    }

    public enum Anchor {
        NORTH(GridBagConstraints.NORTH),
        SOUTH(GridBagConstraints.SOUTH),
        EAST(GridBagConstraints.EAST),
        WEST(GridBagConstraints.WEST),
        NORTHEAST(GridBagConstraints.NORTHEAST),
        NORTHWEST(GridBagConstraints.NORTHWEST),
        SOUTHEAST(GridBagConstraints.SOUTHEAST),
        SOUTHWEST(GridBagConstraints.SOUTHWEST),
        CENTER(GridBagConstraints.CENTER),
        ;

        private int id;

        Anchor(int id) {
            this.id = id;
        }
    }
}
