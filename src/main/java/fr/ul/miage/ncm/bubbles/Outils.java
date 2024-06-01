package fr.ul.miage.ncm.bubbles;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Outils {
    private final String csvPath = "csv/simulationBaignoire.csv";

    /**
     * Demande à l'utilisateur de saisir une valeur entière positive ou nulle.
     * La saisie est répétée jusqu'à ce que l'utilisateur entre une valeur valide.
     * @param message le message à afficher pour demander la saisie de la valeur
     * @return la valeur entière saisie par l'utilisateur
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

        protected List<Fuite> creerListeFuites(int debitDefault, int nbFuites, Baignoire baignoire) {
            List<Fuite> fuites = new ArrayList<>();
            for (int i = 0; i < nbFuites; i++) {
                Fuite fuite = new Fuite(i+1, debitDefault, baignoire);
                fuites.add(fuite);
            }
            return fuites;
        }

        protected List<Robinet> creerListeRobinets(int debitDefault, int nbRobinets, Baignoire baignoire) {
            List<Robinet> robinets = new ArrayList<>();
            for (int i = 0; i < nbRobinets; i++) {
                Robinet robinet = new Robinet(i+1, debitDefault, baignoire);
                robinets.add(robinet);
            }
            return robinets;
        }

    public void exporterCSV(List<Integer> niveauBaignoire, List<Long> temps) {
    }
}
