/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.ui;

import javafx.application.Application;

/**
 The Java 13 runtime will check if the main class extends {@link Application},
 and if that is the case, it strongly requires the JavaFX platform to be
 available as a module, and not as a jar for example.
 <p>
 While going modular is definitely the way to go, I still think it's unfortunate
 that JavaFX applications are treated a bit different unlike other applications
 (where you can still use the classpath).
 <p>
 There are some easy workarounds though. For example, you can have a main class
 (i.e. {@link Launcher}) that is not extending JavaFX.application.Application,
 and that main class can then call the main(String[]) method on your real main
 class --i.e., {@link Main} (that way, the Java launcher doesn't require the
 JavaFX libraries to be available as named modules).

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Launcher {

    public static void main(String[] args) {
        Main.main(args);
    }
}
