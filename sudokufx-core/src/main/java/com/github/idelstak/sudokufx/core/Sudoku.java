/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.core;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Sudoku {

    private static final int GRID = 9;
    private final Map<Position, EnumSet<Value>> numbers = new HashMap<>();

    public Sudoku(int... values) {
        if (values.length != GRID * GRID) {
            throw new IllegalArgumentException("Bad value count");
        }

        var all = Value.values();

        for (var i = 0; i < values.length; i++) {
            var pos = new Position(i / GRID, i % GRID);
            if (values[i] == 0) {
                numbers.put(pos, EnumSet.range(Value.ONE, Value.NINE));
            } else {
                numbers.put(pos, EnumSet.of(all[values[i] - 1]));
            }
        }

        sieveImpossibleNumbers();
    }

    public boolean solve() {
        do {
            System.out.println(this);
        } while (sieveImpossibleNumbers() || searchForAnswers());

        return numbers.values()
                .stream()
                .noneMatch(values -> (values.size() != 1));
    }

    @Override
    public String toString() {
        var result = new StringBuilder();
        for (var row = 0; row < GRID; row++) {
            for (var col = 0; col < GRID; col++) {
                var vals = numbers.get(new Position(row, col));
                result.append('[');
                vals.forEach(result::append);
                result.append(']').append('\t');
            }
            result.append('\n');
        }
        return result.toString();
    }

    /**
     Goes through all the positions and removes numbers that are
     not possible. Also checks the correctness of the found
     numbers.
     */
    private boolean sieveImpossibleNumbers() {
        var removed = false;

        for (var pos : numbers.keySet()) {
            var value = getNumber(pos);
            if (value == null) {
                // must be bitwise OR, otherwise it will fall through
                removed |= removeImpossibleNumbers(pos);
            } else {
                checkCorrectness(pos, value);
            }
        }
        return removed;
    }

    private boolean removeImpossibleNumbers(Position pos) {
        var removed = false;
        var vals = numbers.get(pos);

//        for (Position other : pos.getRelatedPositions()) {
//            removed |= vals.remove(getNumber(other));
//        }
        removed = pos.getRelatedPositions()
                .stream()
                .map(other -> vals.remove(getNumber(other)))
                .reduce(removed, (accumulator, item) -> accumulator | item);

        return removed;
    }

    private Value getNumber(Position pos) {
        var vals = numbers.get(pos);

        if (vals.size() == 1) {
            return vals.iterator().next();
        }

        return null;
    }

    private void checkCorrectness(Position pos, Value val) {
        pos.getRelatedPositions()
                .stream()
                .filter(other -> (val == getNumber(other)))
                .forEachOrdered(other -> {
                    throw new IllegalArgumentException("Error with: " + pos
                            + " clashes with relative " + other);
                });
    }

    private boolean searchForAnswers() {
        for (var pos : numbers.keySet()) {
            var possible = numbers.get(pos);
            if (possible.size() > 1) {
                for (var value : possible) {
                    if (valueNotIn(value, pos.getHorizontalPositions())
                            || valueNotIn(value, pos.getVerticalPositions())
                            || valueNotIn(value, pos.getSmallSquarePositions())) {
                        System.out.println(pos + " MUST BE " + value);
                        numbers.put(pos, EnumSet.of(value));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean valueNotIn(Value value, Collection<Position> positions) {
        return positions.stream()
                .noneMatch(pos -> (numbers.get(pos)
                .contains(value)));
    }

    public enum Value {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

        @Override
        public String toString() {
            return Integer.toString(ordinal() + 1);
        }
    }
}
