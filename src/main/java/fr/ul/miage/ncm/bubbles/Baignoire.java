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

    public void ajouterEau(int debit, int idRobinet) {
        niveauActuel += debit;
        if (niveauActuel >= capaciteMax) {
            niveauActuel = capaciteMax;
        }
        System.out.printf("[ajouter eau rob %d] capacité baignoire : %d%n", idRobinet, niveauActuel);
    }

    public void enleverEau(int debit, int idFuite) {
        niveauActuel -= debit;
        if (niveauActuel <= 0) {
            niveauActuel = 0;
        }
        System.out.printf("[enlever eau fuite %d] capacité baignoire : %d%n", idFuite, niveauActuel);
    }

    public boolean estRemplie() {
        return (capaciteMax <= niveauActuel);
    }

    public boolean estVide() {
        return niveauActuel == 0;
    }

    // Getters et Setters
    public int getCapaciteMax() {
        return capaciteMax;
    }

    /**
     * Renvoie la capacité actuelle de la baignoire.
     * @return la capacité actuelle de la baignoire en litres.
     */
    public int getNiveauActuel() {
        return niveauActuel;
    }

    /**
     * Définit la capacité actuelle de la baignoire.
     * @param niveauActuel la nouvelle capacité actuelle de la baignoire en litres.
     */
    public void setNiveauActuel(int niveauActuel) {
        this.niveauActuel = niveauActuel;
    }
    // Fin Getters et Setters

}
