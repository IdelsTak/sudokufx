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
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class SudokuBoardController {

    private static final Logger LOG = Logger.getLogger(SudokuBoardController.class.getName());
    @FXML
    private GridPane grid;
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
        transferFocusOnClueEntered();

        solveButton.setOnAction(e -> showSolution());
        resetButton.setOnAction(e -> clearCluesFromFields());
    }

    private void showSolution() {
        var clues = getClues();
        var values = clues.stream()
                .mapToInt(i -> i)
                .toArray();
        var sudoku = new Sudoku(values);

        if (sudoku.solve()) {
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

    private Collection<Integer> getClues() {
        return grid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .map(this::getIntValue)
                .collect(Collectors.toList());
    }

    private void clearCluesFromFields() {
        grid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .forEach(this::clearField);
    }

    private void clearField(TextField field) {
        clearSolutionStyling(field);

        Platform.runLater(field::clear);
    }

    private void clearSolutionStyling(TextField field) {
        field.getStyleClass().remove("clues-font");
        field.getStyleClass().remove("solution-font");
        field.setEditable(true);
    }

    private Integer getIntValue(TextField textField) {
        var value = textField.getTextFormatter().getValue();
        return Integer.class.cast(value);
    }

    private void bindFieldsToValidValues() {
        grid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .forEach(textField -> textField.setTextFormatter(new ClueFormatter()));
    }

    private void transferFocusOnClueEntered() {
        var unsolvedFields = grid.getChildren()
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
        var solvedFields = grid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .toArray(TextField[]::new);

        for (int i = 0; i < solvedFields.length; i++) {
            var field = solvedFields[i];
            var styleClass = field.getStyleClass();

            if (cluesIndices.contains(i)) {
                styleClass.add("clues-font");
            } else {
                styleClass.add("solution-font");
            }

//            field.setText(solutionValues[i].toString());
            field.setEditable(false);

            var solution = solutionValues[i];
            var animate = new AnimateSolution(field, solution);
            
            animate.setPeriod(Duration.millis(solution * 100));
            animate.start();
        }
    }

    private class AnimateSolution extends ScheduledService<Void> {

        private final TextField field;
        private final Integer solution;
        private Integer value;

        private AnimateSolution(TextField field, Integer solution) {
            this.field = field;
            this.solution = solution;
            this.value = 0;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (value < solution) {
                        value++;
                        Platform.runLater(() -> field.setText("" + value));
                    }
                    return null;
                }
            };
        }

    }

}
