/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.core;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Clue {

    private final int index;
    private final int value;

    public Clue(int index, int value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public String toString() {
        return "(clue @ " + index + " = " + value + ')';
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }

}
