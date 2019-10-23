/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.ui.controllers;

import com.github.idelstak.sudokufx.ui.util.ClueFormatter;
import java.util.Collection;
import java.util.logging.Level;
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

        solveButton.setOnAction(e -> {
            Collection<Integer> clues = getClues();
            LOG.log(Level.INFO, "Clues provided: {0}", clues);
        });
    }

    private void switchToUnsolvedGrid() {
        Platform.runLater(() -> {
            unsolvedGrid.toFront();
        });
    }

    private void switchToSolvedGrid() {
        Platform.runLater(() -> {
            solvedGrid.toFront();
        });
    }

    private Collection<Integer> getClues() {
        return unsolvedGrid.getChildren()
                .stream()
                .map(TextField.class::cast)
                .map(this::getIntValue)
                .collect(Collectors.toList());
    }

    private Integer getIntValue(TextField textField) {
        Object value = textField.getTextFormatter().getValue();
        return Integer.class.cast(value);
    }

//    private int parseIntFrom(TextField textField) {
//        String text = textField.getText();
//        return text == null || text.isBlank()
//               ? 0
//               : Integer.parseInt(text);
//    }
    private void bindFieldsToValidValues() {
        Stream.concat(
                unsolvedGrid.getChildren().stream(),
                solvedGrid.getChildren().stream())
                .map(TextField.class::cast)
                .forEach(textField -> textField.setTextFormatter(new ClueFormatter()));
    }

}
