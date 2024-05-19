package fr.ul.miage.ncm.bubbles;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Classe contrôleur pour la vue Baignoire.fxml
 */
public class BaignoireController {
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Slider sldFuite;
    @FXML
    private Label lblDebitFuite;
    @FXML
    private Slider sldRobinet;
    @FXML
    private Label lblDebitRobinet;
    @FXML
    private VBox sliderBox;
    @FXML
    private Label lblNiveauBaignoire;
    @FXML
    private Label lblCapaciteBaignoire;

    private Baignoire baignoire;
    private Robinet robinet;
    private Fuite fuite;
    private List<Robinet> robinets;
    private List<Fuite> fuites;

    /**
     * Constructeur de BaignoireController
     */
    public BaignoireController(Baignoire baignoire, List<Robinet> robinets, List<Fuite> fuites) {
        this.baignoire = baignoire;
        this.robinets = robinets;
        this.fuites = fuites;
        System.out.println("Baignore controller initialized");
    }

    /**
     * Méthode qui initialise le contrôleur et crée un objet Baignoire, elle est appelée
     * automatiquement quand le fichier FXML est chargé.
     */
    @FXML
    protected void initialize() {
        robinet = new Robinet((int) sldRobinet.getValue(), baignoire);
        fuite = new Fuite((int) sldFuite.getValue(), baignoire);
        lblDebitRobinet.textProperty().bind(Bindings.format("%.0f", sldRobinet.valueProperty()));
        lblDebitFuite.textProperty().bind(Bindings.format("%.0f", sldFuite.valueProperty()));
        lblCapaciteBaignoire.textProperty().bind(Bindings.format("%.0f", (double) baignoire.getCapaciteMax()));
        lblNiveauBaignoire.textProperty().bind(Bindings.format("%.0f", (double) baignoire.getNiveauActuel()));
        btnStop.setDisable(true);
        sliderBox.setVisible(false);
    }

    /**
     * Méthode qui démarre la simulation de la baignoire quand le bouton "Démarrer" est
     * cliqué par l'utilisateur.
     */
    @FXML
    private void demarrerSimulation() {
        btnStart.setDisable(true);
        btnStop.setDisable(false);
        sliderBox.setVisible(true);

        for (Robinet rob : robinets) {
            rob.start();
        }

        for (Fuite fui : fuites) {
            fui.start();
        }
        System.out.println("La simulation vient de démarrer");
    }

    /**
     * Méthode qui met fin à la simulation de la baignoire quand le bouton "Arrêter" est
     * cliqué par l'utilisateur.
     */
    @FXML
    private void terminerSimulation() {
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        sliderBox.setVisible(false);
        System.out.println("La simulation vient de terminer");
    }

    @FXML
    private void robinetDrag() {
        int nouveauDebit = (int) Math.round(sldRobinet.getValue());
        robinet.setDebit(nouveauDebit);
        System.out.printf("debit du robinet : %d%n", robinet.getDebit());
    }

    @FXML
    private void fuiteDrag() {
        int nouveauDebit = (int) Math.round(sldFuite.getValue());
        fuite.setDebit(nouveauDebit);
        System.out.printf("debit de la fuite : %d%n", fuite.getDebit());
    }}
