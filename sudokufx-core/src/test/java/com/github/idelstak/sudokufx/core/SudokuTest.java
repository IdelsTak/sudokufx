/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.core;

import org.junit.jupiter.api.Test;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class SudokuTest {

    public SudokuTest() {
    }

    @Test
    public void testSolve() {
        // Cape Times Mon 2006/06/19
        Sudoku sudoku = new Sudoku(
                2, 0, 0, 9, 0, 6, 0, 0, 4,
                0, 0, 5, 0, 7, 0, 9, 0, 0,
                0, 3, 0, 0, 0, 0, 0, 8, 0,
                0, 0, 3, 4, 0, 7, 8, 0, 0,
                8, 9, 0, 2, 0, 5, 0, 6, 3,
                0, 0, 7, 6, 0, 8, 2, 0, 0,
                0, 7, 0, 0, 0, 0, 0, 2, 0,
                0, 0, 8, 0, 6, 0, 1, 0, 0,
                3, 0, 0, 7, 0, 1, 0, 0, 8);

        if (sudoku.solve()) {
            System.out.println("SOLVED!!!");
        } else {
            System.out.println("Could not solve");
        }
    }

}
