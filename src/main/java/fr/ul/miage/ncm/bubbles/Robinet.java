package fr.ul.miage.ncm.bubbles;

public class Robinet implements Runnable {
    private int debit;
    private Baignoire baignoire;

    private final Thread thread;
    private boolean isRunning = true;

    public Robinet(int debit, Baignoire baignoire) {
        this.debit = debit;
        this.baignoire = baignoire;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        isRunning = true;
        System.out.println("in run Robinet");
        while(isRunning) {
            baignoire.ajouterEau(debit);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        isRunning = true;
        System.out.println("thrad"+thread.isAlive());
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    public void stop() {
        isRunning = false;
    }

    // Getters et Setters

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public boolean getRunning() {
        return isRunning;
    }

    public Thread getThread() {
        return thread;
    }
}
