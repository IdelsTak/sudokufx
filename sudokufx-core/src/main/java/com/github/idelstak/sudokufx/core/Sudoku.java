/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Sudoku {

    private static final int GRID = 9;
    private final Map<Position, EnumSet<Value>> numbers = new HashMap<>();
    private final Collection<Integer> solution = new ArrayList<>();
    private final Collection<Clue> clues = new ArrayList<>();

    public Sudoku(int... values) {
        if (values.length != GRID * GRID) {
            throw new IllegalArgumentException("Bad value count");
        }

        var all = Value.values();

        for (var i = 0; i < values.length; i++) {
            int val = values[i];

            if (val > 0) {
                clues.add(new Clue(i, val));
            }

            var pos = new Position(i / GRID, i % GRID);
            
            if (val == 0) {
                numbers.put(pos, EnumSet.range(Value.ONE, Value.NINE));
            } else {
                numbers.put(pos, EnumSet.of(all[val - 1]));
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
                .map(Set::size)
                .noneMatch(size -> size != 1);
    }

    public Collection<Integer> getSolution() {
        return Collections.unmodifiableCollection(solution);
    }

    public Collection<Clue> getClues() {
        return Collections.unmodifiableCollection(clues);
    }

    @Override
    public String toString() {
        solution.clear();

        var result = new StringBuilder(GRID * GRID);

        for (var row = 0; row < GRID; row++) {
            for (var column = 0; column < GRID; column++) {
                var vals = numbers.get(new Position(row, column));

                vals.forEach(value -> {
                    solution.add(Integer.parseInt(value.toString()));
                    result.append(value);
                });

                result.append(' ');
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

    private boolean removeImpossibleNumbers(Position position) {
        var removed = false;
        var values = numbers.get(position);

        removed = position.getRelatedPositions()
                .stream()
                .map(this::getNumber)
                .map(values::remove)
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

    private void checkCorrectness(Position position, Value value) {
        position.getRelatedPositions()
                .stream()
                .map(pos -> getNumber(pos))
                .filter(val -> val == value)
                .forEachOrdered(pos -> {
                    throw new IllegalArgumentException("Error with: " + position
                            + " clashes with relative " + pos);
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
                .map(numbers::get)
                .noneMatch(valueSet -> valueSet.contains(value));
    }

    public enum Value {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

        @Override
        public String toString() {
            return Integer.toString(ordinal() + 1);
        }
    }
}
