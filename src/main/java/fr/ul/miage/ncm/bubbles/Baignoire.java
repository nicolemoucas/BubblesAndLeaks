package fr.ul.miage.ncm.bubbles;

/**
 * Classe représentant une baignoire pour la simulation.
 */
public class Baignoire {
    /**
     * Capacité maximale de remplissage de la baignoire.
     */
    private final int capaciteMax;
    /**
     * Niveau actuel de la baignoire en litres.
     */
    private int niveauActuel;

    /**
     * Crée une nouvelle instance de la classe Baignoire.
     * @param capaciteMax La capacité maximale de la baignoire en litres.
     */
    public Baignoire(int capaciteMax) {
        this.capaciteMax = capaciteMax;
        this.niveauActuel = 0;
    }

    /**
     * Ajoute de l'eau dans la baignoire à partir d'un robinet donné.
     * @param debit     Le débit d'eau du robinet en litres par seconde.
     * @param idRobinet L'identifiant du robinet.
     */
    public void ajouterEau(int debit, int idRobinet) {
        niveauActuel += debit;
        if (niveauActuel >= capaciteMax) {
            niveauActuel = capaciteMax;
        }
        System.out.printf("[ajouter eau rob %d] capacité baignoire : %d%n", idRobinet, niveauActuel);
    }

    /**
     * Enlève de l'eau dans la baignoire à partir d'une fuite donnée.
     * @param debit     Le débit d'eau de la fuite en litres par seconde.
     * @param idFuite   L'identifiant de la fuite.
     */
    public void enleverEau(int debit, int idFuite) {
        niveauActuel -= debit;
        if (niveauActuel <= 0) {
            niveauActuel = 0;
        }
        System.out.printf("[enlever eau fuite %d] capacité baignoire : %d%n", idFuite, niveauActuel);
    }

    /**
     * Vérifie si la baignoire est remplie.
     * @return Un booléen indiquant si la baignoire est remplie ou non.
     */
    public boolean estRemplie() {
        return (capaciteMax <= niveauActuel);
    }

    /**
     * Vérifie si la baignoire est vide.
     * @return Un booléen indiquant si la baignoire est vide ou non.
     */
    public boolean estVide() {
        return niveauActuel == 0;
    }

    // Getters et Setters
    /**
     * Renvoie la capacité maximale de la baignoire en litres.
     * @return La capacité maximale de la baignoire.
     */
    public int getCapaciteMax() {
        return capaciteMax;
    }

    /**
     * Renvoie la capacité actuelle de la baignoire en litres.
     * @return La capacité actuelle de la baignoire.
     */
    public int getNiveauActuel() {
        return niveauActuel;
    }
    // Fin Getters et Setters

}
