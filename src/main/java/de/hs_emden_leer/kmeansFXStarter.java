package de.hs_emden_leer;

/**
 * Class is need to solve a problem with maven assembly and missing JavaFX runtime components
 * <p>
 * Starts the application without any JavaFX components
 * Source: https://stackoverflow.com/questions/56894627/how-to-fix-error-javafx-runtime-components-are-missing-and-are-required-to-ru/58498686
 */
public class kmeansFXStarter {
    public static void main(final String[] args) {
        kmeansFXApp.main(args);
    }
}
