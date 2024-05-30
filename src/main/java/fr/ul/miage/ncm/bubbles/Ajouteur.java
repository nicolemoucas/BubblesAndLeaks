package fr.ul.miage.ncm.bubbles;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.logging.Logger;

public class Ajouteur extends ScheduledService<Baignoire> {
    private static final Logger LOG = Logger.getLogger(Ajouteur.class.getName());
    private Baignoire baignoire;
    private int debit;

    public Ajouteur(Baignoire baignoire, int debit){
        super();
        this.baignoire = baignoire;
        this.debit = debit;
    }

    @Override
    protected Task<Baignoire> createTask() {

        return new Task<Baignoire>() {
            @Override
            protected Baignoire call() throws Exception {
                baignoire.ajouterEau(debit);
                return baignoire;
            }
        };
    }
}
