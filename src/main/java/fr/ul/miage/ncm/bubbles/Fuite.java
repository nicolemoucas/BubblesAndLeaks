package fr.ul.miage.ncm.bubbles;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.logging.Logger;

public class Fuite extends ScheduledService<Baignoire> {
    private static final Logger LOG = Logger.getLogger(Fuite.class.getName());
    private final Baignoire baignoire;

    private int debit;
    private int idFuite;

    public Fuite(int idFuite, int debit, Baignoire baignoire){
        this.idFuite = idFuite;
        this.baignoire = baignoire;
        this.debit = debit;
    }

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

    @Override
    public String toString() {
        return idFuite + ". débit : " + debit + " l/s";
    }

    // Getters et Setters
    public Baignoire getBaignoire() {
        return baignoire;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getIdFuite() {
        return idFuite;
    }

    public void setIdFuite(int idFuite) {
        this.idFuite = idFuite;
    }
    // End Getters et Setters
}
