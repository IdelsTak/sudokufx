/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.ui.util;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ClueFormatter extends TextFormatter<Integer> {

    private static final Logger LOG = Logger.getLogger(ClueFormatter.class.getName());
    /**
     Compiles a regex to match any digit from 1 to 9, where the digit occurs
     exactly once.
     */
    private static final Pattern PATTERN = Pattern.compile("[1-9]?");

    public ClueFormatter() {
        super(
                new StringToIntConverter(),//String converter
                0,//Default value
                ClueFormatter::filter//Change filter
        );
    }

    private static Change filter(Change change) {
        String text = change.getControlNewText();

        if (PATTERN.matcher(text).matches()) {
            return change;
        }

        return null;
    }

    private static class StringToIntConverter extends StringConverter<Integer> {

        @Override
        public String toString(Integer value) {
            String result = value.toString();
            return new Emptystring(result).ifZero();
        }

        @Override
        public Integer fromString(String string) {
            int result = 0;
            if (string != null) {
                try {
                    result = Integer.parseInt(string);
                } catch (NumberFormatException exception) {
                }
            }
            return result;
        }

    }

    private static class Emptystring {

        private final String value;

        private Emptystring(String value) {
            this.value = value;
        }

        public String ifZero() {
            return Objects.equals("0", value)
                   ? ""
                   : value;
        }

    }
    
    
}
