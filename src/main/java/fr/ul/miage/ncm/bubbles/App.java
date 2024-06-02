package fr.ul.miage.ncm.bubbles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe principale de l'application Bubbles &amp; Leaks.
 * App étend la classe JavaFX Application et contient la méthode main pour démarrer l'application.
 */
public class App extends Application {
    public static final int MAX_BAIGNOIRE = 450;
    public static final int MAX_ESSAIS_BAIGNOIRE_VIDE = 50;
    public static final int HEIGHT_ICONS = 40;
    public static final int WIDTH_ICONS = 40;
    public static final int MAX_FUITES = 10;
    public static final int MIN_FUITES = 0;
    public static final int MAX_ROBINETS = 10;
    public static final int MIN_ROBINETS = 1;
    /**
     * Logger qui permet d'afficher des informations pour suivre l'exécution du programme.
     */
    private static final Logger LOG = Logger.getLogger(App.class.getName());
    /**
     * Scène graphique sur laquelle on ajoute les éléments FXML.
     */
    private static Scene scene;

    /**
     * Injection de la classe Baignoire.
     */
    private static final String fxmlPath = "/baignoire.fxml";

    /**
     * Classe principale qui permet de lancer le programme et d'intéragir avec celui-ci
     * avec l'interface en ligne de commande.
     * @param args arguments de main
     */
    public static void main(String[] args) {
        // Options
        Options options = new Options();
        Option d = new Option("d", "debug", false, "mode debug");
        options.addOption(d);
        // Fin Options

        // Parsing
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            LOG.setLevel(Level.WARNING);
            if (line.hasOption("d")) {
                LOG.setLevel(Level.INFO);
                LOG.info("Le mode débogage est activé.");
            }
        } catch (org.apache.commons.cli.ParseException e) {
            LOG.severe("Erreur dans la ligne de commande");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Demo", options);
            System.exit(1);
        }
        // Fin Parsing

        // Traitement
        LOG.info("Bubbles & Leaks a démarré !");

        launch(args);
        LOG.info("Bubbles & Leaks a fini son exécution !");
        // Fin Traitement
    }


    // Méthodes
    /**
     * Méthode qui lance l'application.
     * La méthode charge le fichier FXML de la baignoire puis charge la scène principale.
     * @param primaryStage Scène principale de l'application
     * @throws Exception Erreur lors du chargement du fichier FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Bubbles & Leaks");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Fin Méthodes
}
