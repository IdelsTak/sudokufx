/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.core;

import java.util.Collection;
import java.util.HashSet;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public final class Position {

    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public int hashCode() {
        return row * 29 + col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        var position = (Position) o;
        return !(col != position.col || row != position.row);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

    public Collection<Position> getRelatedPositions() {
        Collection<Position> result = new HashSet<>();
        result.addAll(getHorizontalPositions());
        result.addAll(getVerticalPositions());
        result.addAll(getSmallSquarePositions());
        return result;
    }

    public Collection<Position> getHorizontalPositions() {
        Collection<Position> result = new HashSet<>();
        for (var i = 0; i < 9; i++) {
            result.add(new Position(row, i));
        }
        result.remove(this);
        return result;
    }

    public Collection<Position> getVerticalPositions() {
        Collection<Position> result = new HashSet<>();
        for (var i = 0; i < 9; i++) {
            result.add(new Position(i, col));
        }
        result.remove(this);
        return result;
    }

    public Collection<Position> getSmallSquarePositions() {
        Collection<Position> result = new HashSet<>();
        for (var i = 0; i < 9; i++) {
            var smallSqRow = i / 3 + (row / 3) * 3;
            var smallSqCol = i % 3 + (col / 3) * 3;
            result.add(new Position(smallSqRow, smallSqCol));
        }
        result.remove(this);
        return result;
    }
}
