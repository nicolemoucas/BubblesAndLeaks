package fr.ul.miage.ncm.bubbles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe principale de l'application Bubbles &amp; Leaks.
 * App étend la classe JavaFX Application et contient la méthode main pour démarrer l'application.
 */
public class App extends Application {
    /**
     * Logger qui permet d'afficher des informations pour suivre l'exécution du programme.
     */
    private static final Logger LOG = Logger.getLogger(App.class.getName());
    private static Scene scene;

    /**
     * Injection de la classe Baignoire.
     */
//    private static Baignoire baignoire = null;
    private static final String fxmlPath = "/baignoire.fxml";

    private static int nbRobinets;
    private static int nbFuites;
    private static List<Robinet> robinets = new ArrayList<Robinet>();
    private static List<Fuite> fuites = new ArrayList<Fuite>();
    private static ExecutorService threadPool;

    /**
     * Classe principale qui permet de lancer le programme et d'intéragir avec celui-ci
     * avec l'interface en ligne de commande.
     * @param args arguments de main
     */
    public static void main(String[] args) {
        // Arguments
        List<Thread> robinetThreads = new ArrayList<>();
        List<Thread> fuiteThreads = new ArrayList<>();

        // Syntaxe
        Options options = new Options();
        Option d = new Option("d", "debug", false, "mode debug");
        options.addOption(d);

        // Parse
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            LOG.setLevel(Level.WARNING);
            if (line.hasOption("d")) {
                LOG.setLevel(Level.INFO);
            }
        } catch (Exception exp) {
            LOG.severe("Erreur dans la ligne de commande");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Hello there", options);
            System.exit(1);
        }

        // Process
        LOG.info("Bubbles & Leaks a démarré");

        // Création des threads
//        threadPool = Executors.newFixedThreadPool(nbRobinets + nbFuites);

//        for (int i = 0; i < nbRobinets; i++) {
//            Robinet robinet = new Robinet(robinets.get(i).getDebit(), baignoire);
////            threadPool.submit(robinet);
//        }
//
//        for (int i = 0; i < nbFuites; i++) {
//            Fuite fuite = new Fuite(fuites.get(i).getDebit(), baignoire);
////            threadPool.submit(fuite);
//        }

        launch(args);
        LOG.info("Bubbles & Leaks a fini son exécution");
    }

//    private static List<Fuite> creerListeFuites(int debitDefault) {
//        List<Fuite> fuites = new ArrayList<>();
//        for (int i = 0; i < nbFuites; i++) {
//            Fuite fuite = new Fuite(debitDefault, baignoire);
//            fuites.add(fuite);
//        }
//        return fuites;
//    }
//
//    private static List<Robinet> creerListeRobinets(int debitDefault) {
//        List<Robinet> robinets = new ArrayList<>();
//        for (int i = 0; i < nbRobinets; i++) {
//            Robinet robinet = new Robinet(debitDefault, baignoire);
//            robinets.add(robinet);
//        }
//        return robinets;
//    }

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
//            fxmlLoader.setControllerFactory(c->{return new BaignoireController(baignoire, robinets, fuites);});
            scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        threadPool.shutdown();
    }

    // Getters et Setters

    public static int getNbRobinets() {
        return nbRobinets;
    }

    public static void setNbRobinets(int nbRobinets) {
        App.nbRobinets = nbRobinets;
    }

    public static int getNbFuites() {
        return nbFuites;
    }

    public static void setNbFuites(int nbFuites) {
        App.nbFuites = nbFuites;
    }
    // Fin Getters et Setters
}
