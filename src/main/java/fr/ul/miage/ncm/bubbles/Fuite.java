package fr.ul.miage.ncm.bubbles;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.logging.Logger;

/**
 * Classe qui représente une fuite qui vide une baignoire à un débit donné. Elle étend la classe
 * ScheduledService de JavaFX pour planifier des tâches de vidage de la baignoire.
 */
public class Fuite extends ScheduledService<Baignoire> {
    /**
     * Logger pour la classe Fuite.
     */
    private static final Logger LOG = Logger.getLogger(Fuite.class.getName());
    /**
     * La baignoire à laquelle appartient la fuite.
     */
    private final Baignoire baignoire;
    /**
     * Le débit de la fuite en litres par seconde.
     */
    private int debit;
    /**
     * L'identifiant de la fuite.
     */
    private int idFuite;

    /**
     * Constructeur de la classe Fuite, crée une nouvelle Fuite avec des paramètres donnés.
     * @param idFuite   L'identifiant de la fuite
     * @param debit     Le débit en litres par seconde
     * @param baignoire La baignoire associée à la fuite
     */
    public Fuite(int idFuite, int debit, Baignoire baignoire){
        this.idFuite = idFuite;
        this.baignoire = baignoire;
        this.debit = debit;
    }

    /**
     * Crée la tâche de vidage de la baignoire par la fuite.
     * @return La tâche de remplissage
     */
    @Override
    protected Task<Baignoire> createTask() {
        return new Task<Baignoire>() {
            @Override
            protected Baignoire call() throws Exception {
                synchronized (baignoire) { // Synchronisé avec la ressource critique baignoire
                    baignoire.enleverEau(debit, idFuite);
                }
                return baignoire;
            }
        };
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de la fuite.
     * @return La chaîne de caractères
     */
    @Override
    public String toString() {
        return idFuite + ". débit : " + debit + " l/s";
    }

    // Getters et Setters
    /**
     * Renvoie le débit de la fuite en litres par seconde.
     * @return Le débit de la fuite
     */
    public int getDebit() {
        return debit;
    }

    /**
     * Modifie le débit de la fuite.
     * @param debit Le débit de la fuite
     */
    public void setDebit(int debit) {
        this.debit = debit;
    }

    /**
     * Renvoie l'identifiant de la fuite.
     * @return L'identifiant de la fuite
     */
    public int getIdFuite() {
        return idFuite;
    }
    // End Getters et Setters
}
