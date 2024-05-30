package fr.ul.miage.ncm.bubbles;

public class Fuite implements Runnable {
    private int debit;
    private Baignoire baignoire;
    private final Thread thread;
    private boolean isRunning = true;

    public Fuite(int debit, Baignoire baignoire) {
        this.debit = debit;
        this.baignoire = baignoire;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        isRunning = true;
        System.out.println("in run Fuite");
        while(isRunning) {
            baignoire.enleverEau(debit);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        isRunning = true;
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
}
