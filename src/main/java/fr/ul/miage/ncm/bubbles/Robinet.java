package fr.ul.miage.ncm.bubbles;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.logging.Logger;

public class Robinet extends ScheduledService<Baignoire> {
    private static final Logger LOG = Logger.getLogger(Robinet.class.getName());
    private final Baignoire baignoire;

    private int debit;
    private int idRobinet;

    public Robinet(int idRobinet, int debit, Baignoire baignoire){
        this.idRobinet = idRobinet;
        this.baignoire = baignoire;
        this.debit = debit;
    }

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

    @Override
    public String toString() {
        return idRobinet + ". débit : " + debit + " l/s";
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

    public int getIdRobinet() {
        return idRobinet;
    }

    public void setIdRobinet(int idRobinet) {
        this.idRobinet = idRobinet;
    }
    // End Getters et Setters
}
