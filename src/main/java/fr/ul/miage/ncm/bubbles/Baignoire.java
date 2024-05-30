package fr.ul.miage.ncm.bubbles;

/**
 * Classe représentant une baignoire pour la simulation.
 */
public class Baignoire {
    private int capaciteMax;
    private int niveauActuel;

    public Baignoire(int capaciteMax) {
        this.capaciteMax = capaciteMax;
        this.niveauActuel = 0;
    }

    public void ajouterEau(int debit) {
        niveauActuel += debit;
        if (niveauActuel >= capaciteMax) {
            niveauActuel = capaciteMax;
        }
        System.out.printf("[ajouter eau] capacité baignoire : %d%n", niveauActuel);
    }

    public void enleverEau(int debit) {
        niveauActuel -= debit;
        if (niveauActuel <= 0) {
            niveauActuel = 0;
        }
        System.out.printf("[enlever eau] capacité baignoire : %d%n", niveauActuel);
    }

    public boolean estRemplie() {
        return (niveauActuel <= capaciteMax);
    }

    // Getters et Setters

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }
    /**
     * Renvoie la capacité actuelle de la baignoire.
     *
     * @return la capacité actuelle de la baignoire en litres.
     */
    public int getNiveauActuel() {
        return niveauActuel;
    }

    /**
     * Définit la capacité actuelle de la baignoire.
     *
     * @param niveauActuel la nouvelle capacité actuelle de la baignoire en litres.
     */
    public void setNiveauActuel(int niveauActuel) {
        this.niveauActuel = niveauActuel;
    }

    // Fin Getters et Setters

}
