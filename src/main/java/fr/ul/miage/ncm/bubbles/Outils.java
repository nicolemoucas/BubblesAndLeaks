package fr.ul.miage.ncm.bubbles;

import java.util.Scanner;

public class Outils {
    /**
     * Demande à l'utilisateur de saisir une valeur entière positive ou nulle.
     * La saisie est répétée jusqu'à ce que l'utilisateur entre une valeur valide.
     *
     * @param message le message à afficher pour demander la saisie de la valeur
     * @return la valeur entière saisie par l'utilisateur
     */
    protected static int saisirValeur(int min, int max, String message) {
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
//        scanner.close();
        return valeur;
    }
}