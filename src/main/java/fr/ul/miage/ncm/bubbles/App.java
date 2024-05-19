package fr.ul.miage.ncm.bubbles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
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

    /**
     * Injection de la classe Baignoire.
     */
    private static final Baignoire baignoire = null;
    private static final String fxmlPath = "/baignoire.fxml";

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(c->{return new BaignoireController();});
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Classe principale qui permet de lancer le programme et d'intéragir avec celui-ci
     * avec l'interface en ligne de commande.
     * @param args arguments de main
     */
    public static void main(String[] args) {
        // Arguments
        int capacite;
        int nbRobinets;
        ArrayList<Integer> debitRobinets = new ArrayList<Integer>();
        int nbFuites;
        ArrayList<Integer> debitFuites = new ArrayList<Integer>();

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
        LOG.info("Démarrage de la simulation");
        capacite = saisirValeur("Entrer la capacité de la baignoire : ");
        nbRobinets = saisirValeur("Entrer le nombre de robinets : ");
        nbFuites = saisirValeur("Entrer le nombre de fuites : ");
        launch(args);
        LOG.info("Fin de la simulation");
    }

    /**
     * Demande à l'utilisateur de saisir une valeur entière positive ou nulle.
     * La saisie est répétée jusqu'à ce que l'utilisateur entre une valeur valide.
     *
     * @param message le message à afficher pour demander la saisie de la valeur
     * @return la valeur entière saisie par l'utilisateur
     */
    private static int saisirValeur(String message) {
        Scanner scanner = new Scanner(System.in);
        boolean saisieCorrecte = false;
        do {
            System.out.print("\n" + message);
            if (scanner.hasNextInt()) {
                int valeur = scanner.nextInt();
                if (valeur >= 0) {
                    saisieCorrecte = true;
                    return valeur;
                } else {
                    System.out.println("Erreur : la valeur saisie n'est pas correcte.\n" +
                            "Veuillez réessayer avec un entier >= 0");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Erreur : la valeur saisie n'est pas correcte.\n" +
                        "Veuillez réessayer avec un entier >= 0");
                scanner.nextLine();
            }
        } while (!saisieCorrecte);
        return 0;
    }
}
