package fr.ul.miage.ncm.bubbles;

import org.apache.commons.cli.*;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe représentant une baignoire pour la simulation.
 */
public class Baignoire {
    /**
     * Logger qui permet d'afficher des informations pour suivre l'exécution du programme.
     */
    public static final Logger LOG = Logger.getLogger(Baignoire.class.getName());

    /**
     * Classe principale qui permet de lancer le programme et d'intéragir avec celui-ci
     * avec l'interface en ligne de commande.
     * @param args arguments
     */
    public static void main(String[] args) {

        // Arguments
        LOG.setLevel(Level.INFO);

        Scanner scanner = new Scanner(System.in);
        boolean debug = false;

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
                debug = true;
                LOG.setLevel(Level.INFO);
            }
        } catch (Exception exc) {
            LOG.severe("Erreur dans la ligne de commande");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("run", options);
            System.exit(1);
        }

        // Process
        LOG.info("Démarrage de la simulation");
        System.out.println("Baignoire ... ");
        LOG.info("Fin de la simulation");


//        scanner.close();
    }
}
