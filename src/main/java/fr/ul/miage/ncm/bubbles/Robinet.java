package fr.ul.miage.ncm.bubbles;

public class Robinet implements Runnable {
    private int debit;
    private Baignoire baignoire;
    private final Thread thread;

    public Robinet(int debit, Baignoire baignoire) {
        this.debit = debit;
        this.baignoire = baignoire;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        System.out.println("in run Robinet");
        while(true) {
            baignoire.ajouterEau(debit);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        thread.start();
    }

    // Getters et Setters

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }
}
