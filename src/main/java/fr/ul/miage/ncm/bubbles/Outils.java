package fr.ul.miage.ncm.bubbles;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Classe qui contient des méthodes utilitaires pour la simulation de la baignoire.
 */
public class Outils {
    /**
     * Logger pour la classe Outils.
     */
    private static final Logger LOG = Logger.getLogger(Outils.class.getName());
    /**
     * Chemin pour le fichier CSV.
     */
    private final String csvPath = "simulationBaignoire.csv";

    /**
     * Demande à l'utilisateur de saisir une valeur entière positive ou nulle.
     * La saisie est répétée jusqu'à ce que l'utilisateur entre une valeur valide.
     * @param min       La valeur minimale à respecter pour la saisie.
     * @param max       La valeur maximale à respecter pour la saisie.
     * @param message   Le message à afficher pour demander la saisie de la valeur.
     * @return          La valeur entière saisie par l'utilisateur.
     */
    protected int saisirValeur(int min, int max, String message) {
        Scanner scanner = new Scanner(System.in);
        boolean saisieCorrecte = false;
        int valeur = 0;
        do {
            System.out.print("\n" + message);
            if (scanner.hasNextInt()) {
                valeur = scanner.nextInt();
                if (valeur >= min && valeur <= max) {
                    saisieCorrecte = true;
                } else {
                    System.out.println("Erreur : la valeur saisie n'est pas correcte.\n" +
                            "Veuillez réessayer avec un entier entre " + min + " et " + max + ".");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Erreur : la valeur saisie n'est pas correcte.\n" +
                        "Veuillez réessayer avec un entier.");
                scanner.nextLine();
            }
        } while (!saisieCorrecte);
        return valeur;
    }

    /**
     * Crée une liste de fuites avec un débit par défaut et un identifiant pour chacune.
     * @param debitDefault  Le débit par défaut.
     * @param nbFuites      Le nombre de fuites (longueur de la liste).
     * @param baignoire     La baignoire associée aux fuites.
     * @return              La liste des fuites créées.
     */
    protected List<Fuite> creerListeFuites(int debitDefault, int nbFuites, Baignoire baignoire) {
        List<Fuite> fuites = new ArrayList<>();
        for (int i = 0; i < nbFuites; i++) {
            Fuite fuite = new Fuite(i+1, debitDefault, baignoire);
            fuites.add(fuite);
        }
        return fuites;
    }

    /**
     * Crée une liste de robinets avec un débit par défaut et un identifiant pour chacun.
     * @param debitDefault  Le débit par défaut.
     * @param nbRobinets    Le nombre de robinets (longueur de la liste).
     * @param baignoire     La baignoire associée aux robinets.
     * @return              La liste des robinets crées.
     */
    protected List<Robinet> creerListeRobinets(int debitDefault, int nbRobinets, Baignoire baignoire) {
        List<Robinet> robinets = new ArrayList<>();
        for (int i = 0; i < nbRobinets; i++) {
            Robinet robinet = new Robinet(i+1, debitDefault, baignoire);
            robinets.add(robinet);
        }
        return robinets;
    }

    /**
     * Méthode qui exporte l'évolution du niveau de la baignoire lors de la
     * simulation et du temps écoulé dans un fichier CSV.
     * @param niveauBaignoire   La liste avec l'évolution du niveau de la baignoire.
     * @param temps             La liste avec les temps correspondants aux modifications du niveau
     *                          de la baignoire.
     */
    public void exporterCSV(List<Integer> niveauBaignoire, List<Long> temps) {
        String[] headers = {"niveauBaignoire", "temps"};
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();
        File csvFile = new File(csvPath);
        try {
            csvFile.createNewFile();

        } catch (IOException e) {
            LOG.warning("Une erreur est survenue lors de la création du fichier CSV.");
            throw new RuntimeException(e);
        }

        try (final CSVPrinter printer = new CSVPrinter(new FileWriter(csvFile), csvFormat)) {
            if (niveauBaignoire.size() != temps.size()) {
                printer.printRecord("Une erreur est survenue lors de l'écriture du fichier CSV.");
            } else {
                for (int i = 0; i < niveauBaignoire.size(); i++) {
                    try {
                        printer.printRecord(niveauBaignoire.get(i), temps.get(i));
                    } catch (IOException e) {
                        LOG.warning("Erreur lors de l'écriture' du fichier CSV.");
                        e.printStackTrace();
                    }
                }
                System.out.println("\nLe fichier CSV de la simulation '" + csvPath + "' a été créé.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
