package fr.ul.miage.ncm.bubbles;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.logging.Logger;

/**
 * Classe qui représente un robinet qui remplit une baignoire à un débit donné. Elle étend la classe
 * ScheduledService de JavaFX pour planifier des tâches de remplissage de la baignoire.
 */
public class Robinet extends ScheduledService<Baignoire> {
    /**
     * Logger pour la classe Robinet.
     */
    private final Logger LOG = Logger.getLogger(Robinet.class.getName());
    /**
     * La baignoire à laquelle appartient le robinet.
     */
    private final Baignoire baignoire;
    /**
     * Le débit du robinet en litres par seconde.
     */
    private int debit;
    /**
     * L'identifiant du robinet.
     */
    private int idRobinet;

    /**
     * Constructeur de la classe Robinet, crée un nouveau Robinet avec des paramètres donnés.
     * @param idRobinet L'identifiant du robinet
     * @param debit     Le débit en litres par seconde
     * @param baignoire La baignoire associée au robinet
     */
    public Robinet(int idRobinet, int debit, Baignoire baignoire){
        this.idRobinet = idRobinet;
        this.baignoire = baignoire;
        this.debit = debit;
    }

    /**
     * Crée la tâche de remplissage de la baignoire par le robinet.
     * @return La tâche de remplissage
     */
    @Override
    protected Task<Baignoire> createTask() {
        return new Task<Baignoire>() {
            @Override
            protected Baignoire call() throws Exception {
                synchronized (baignoire) { // Synchronisé avec la ressource critique baignoire
                    baignoire.ajouterEau(debit, idRobinet);
                }
                return baignoire;
            }
        };
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères du robinet.
     * @return La chaîne de caractères
     */
    @Override
    public String toString() {
        return idRobinet + ". débit : " + debit + " l/s";
    }


    // Getters et Setters
    /**
     * Renvoie le débit du robinet en litres par seconde.
     * @return Le débit du robinet
     */
    public int getDebit() {
        return debit;
    }

    /**
     * Modifie le débit du robinet.
     * @param debit Le nouveau débit du robinet
     */
    public void setDebit(int debit) {
        this.debit = debit;
    }

    /**
     * Renvoie l'identifiant du robinet.
     * @return L'identifiant du robinet
     */
    public int getIdRobinet() {
        return idRobinet;
    }
    // End Getters et Setters
}
