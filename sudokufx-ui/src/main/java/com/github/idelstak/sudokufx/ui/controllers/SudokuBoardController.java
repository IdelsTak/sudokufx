/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.ui.controllers;

import com.github.idelstak.sudokufx.core.Sudoku;
import com.github.idelstak.sudokufx.ui.util.ClueFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class SudokuBoardController {

    private static final Logger LOG = Logger.getLogger(SudokuBoardController.class.getName());
    @FXML
    private StackPane stackPane;
    @FXML
    private GridPane unsolvedGrid;
    @FXML
    private GridPane solvedGrid;
    @FXML
    private Button resetButton;
    @FXML
    private Button solveButton;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        bindFieldsToValidValues();
        switchToUnsolvedGrid();
        transferFocusOnClueEntered();

        solveButton.setOnAction(e -> showSolution());
        resetButton.setOnAction(e -> {
            switchToUnsolvedGrid();
            clearCluesFromFields();
        });
    }

    private void showSolution() {
        var clues = getClues();
        var values = clues.stream()
                .mapToInt(i -> i)
                .toArray();
        var sudoku = new Sudoku(values);

        if (sudoku.solve()) {
            switchToSolvedGrid();

            Platform.runLater(() -> {
                fillSolution(
                        sudoku.getSolution(),
                        getValidCluesIndices(clues));
            });
        }
    }

    private Collection<Integer> getValidCluesIndices(Collection<Integer> allClues) {
        var indices = new ArrayList<Integer>();
        var clues = allClues.toArray(Integer[]::new);

        for (int i = 0; i < clues.length; i++) {
            Integer clue = clues[i];
            if (clue != 0) {
                indices.add(i);
            }
        }

        return indices;
    }

    private void switchToUnsolvedGrid() {
        Platform.runLater(() -> unsolvedGrid.toFront());
    }

    private void switchToSolvedGrid() {
        Platform.runLater(() -> solvedGrid.toFront());
    }

    private Collection<Integer> getClues() {
        return unsolvedGrid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .map(this::getIntValue)
                .collect(Collectors.toList());
    }

    private void clearCluesFromFields() {
        unsolvedGrid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .forEach(this::clearField);
    }

    private void clearField(TextField field) {
        Platform.runLater(field::clear);
    }

    private Integer getIntValue(TextField textField) {
        var value = textField.getTextFormatter().getValue();
        return Integer.class.cast(value);
    }

    private void bindFieldsToValidValues() {
        Stream.concat(
                unsolvedGrid.getChildren().stream(),
                solvedGrid.getChildren().stream())
                .map(TextField.class::cast)
                .forEach(textField -> textField.setTextFormatter(new ClueFormatter()));
    }

    private void transferFocusOnClueEntered() {
        var unsolvedFields = unsolvedGrid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .toArray(TextField[]::new);

        for (int i = 0; i < unsolvedFields.length; i++) {
            var unsolvedField = unsolvedFields[i];
            var isLastIndex = (i == (unsolvedFields.length - 1));
            var nextIndex = isLastIndex ? 0 : (i + 1);

            unsolvedField.textProperty()
                    .addListener((ob, ov, nv) -> {
                        Platform.runLater(() -> {
                            var field = unsolvedFields[nextIndex];
                            field.selectAll();
                            field.requestFocus();
                        });
                    });
        }
    }

    private void fillSolution(
            Collection<Integer> solutions,
            Collection<Integer> cluesIndices) {
        var solutionValues = solutions.toArray(Integer[]::new);
        var solvedFields = unsolvedGrid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .toArray(TextField[]::new);

        for (int i = 0; i < solvedFields.length; i++) {
            var field = solvedFields[i];

            if (!cluesIndices.contains(i)) {
                field.getStyleClass().add("solution-font");
            }

            field.setText(solutionValues[i].toString());
        }
    }

}
