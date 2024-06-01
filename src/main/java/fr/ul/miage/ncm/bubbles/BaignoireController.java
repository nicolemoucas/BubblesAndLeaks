package fr.ul.miage.ncm.bubbles;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Slider;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

/**
 * Classe contrôleur pour la vue Baignoire.fxml
 */
public class BaignoireController {
    private static final Logger LOG = Logger.getLogger(App.class.getName());
    // Éléments FXML
    @FXML
    TabPane tabPane;
    @FXML
    BorderPane borderPane;
    @FXML
    StackPane stackPaneBaignoire;
    @FXML
    Button btnStart;
    @FXML
    Button btnStop;
    @FXML
    Slider sldFuite;
    @FXML
    Label lblDebitFuite;
    @FXML
    Label lblTitleDebitFuite;
    @FXML
    Label lblTitleDebitRob;
    @FXML
    Slider sldRobinet;
    @FXML
    Label lblDebitRobinet;
    @FXML
    VBox sliderBox;
    @FXML
    Label lblNiveauBaignoire;
    @FXML
    Label lblCapaciteBaignoire;
    @FXML
    ImageView imageBaignoire;
    @FXML
    Rectangle rectBaignoire;
    @FXML
    TextField textFieldCapBaignoire;
    @FXML
    TextField textFieldNbRobinets;
    @FXML
    TextField textFieldNbFuites;
    @FXML
    Tab tabBaignoire;
    @FXML
    Tab tabDemarrage;
    @FXML
    Tab tabStatistiques;
    @FXML
    Button btnCommencer;
    @FXML
    ListView<Robinet> listViewRobinets = new ListView<>();
    @FXML
    ListView<Fuite> listViewFuites = new ListView<>();
    // Fin éléments FXML
//    private Robinet robinet;
//    private Fuite fuite;
    static ScheduledExecutorService thread;
    private List<Robinet> robinets;
    private List<Fuite> fuites;
    private Outils outils = new Outils();
    int nbRobinets;
    int nbFuites;
    int debitDefaultRobinet = 5;
    int debitDefaultFuite = 2;
    boolean simulationActive = false;

    /**
     * Méthode qui initialise le contrôleur et crée un objet Baignoire, elle est appelée
     * automatiquement quand le fichier FXML est chargé.
     */
    @FXML
    public void initialize() {
//        robinet = new Robinet((int) sldRobinet.getValue(), baignoire);
//        fuite = new Fuite((int) sldFuite.getValue(), baignoire);
        // Saisie capacité, nb de robinets et nb de fuites
        int capaciteMaxBaignoire = outils.saisirValeur(0, 1000,
                "Entrer la capacité de la baignoire (litres) : ");
        nbRobinets = outils.saisirValeur(1, 10, "Entrer le nombre de robinets (1 à 10) : ");
        nbFuites = outils.saisirValeur(0, 10, "Entrer le nombre de fuites (0 à 10) : ");

        Baignoire baignoire = new Baignoire(capaciteMaxBaignoire);
        robinets = outils.creerListeRobinets(debitDefaultRobinet, nbRobinets, baignoire);
        fuites = outils.creerListeFuites(debitDefaultFuite, nbFuites, baignoire);
        // TODO ajouter fuites et robinets à liste fxml
        initialiserListeRobinets(robinets);
        initialiserListeFuites(fuites);
        baignoire = initialiserElementsFXML(baignoire);

        System.out.println(LocalTime.now());
        Instant top = Instant.now();
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(nbRobinets + nbFuites);
        // TODO sliders avant de démarrer et après démarrer disable slider fuite
    }

    private Baignoire initialiserElementsFXML(Baignoire baignoire) {
        lblDebitRobinet.textProperty().bind(Bindings.format("%.0f", sldRobinet.valueProperty()));
        lblDebitFuite.textProperty().bind(Bindings.format("%.0f", sldFuite.valueProperty()));
        lblCapaciteBaignoire.textProperty().bind(Bindings.format("%.0f", (double) baignoire.getCapaciteMax()));
        lblNiveauBaignoire.textProperty().bind(Bindings.format("%.0f", (double) baignoire.getNiveauActuel()));
        btnStop.setDisable(true);
        sldFuite.setDisable(true);
        sldRobinet.setDisable(true);
//        sliderBox.setVisible(false);
        stackPaneBaignoire.getChildren().remove(rectBaignoire);
        tabDemarrage.setDisable(false);
        tabBaignoire.setDisable(true);
        tabStatistiques.setDisable(true);
        return baignoire;
    }

    private void initialiserListeFuites(List<Fuite> fuites) {
        // vider liste s'il y a encore des fuites
        listViewFuites.getItems().clear();
        listViewFuites.getItems().setAll(fuites);
    }

    private void initialiserListeRobinets(List<Robinet> robinets) {
        // vider liste s'il y a encore des robinets
        listViewRobinets.getItems().clear();
        listViewRobinets.getItems().setAll(robinets);
    }

    @FXML
    void toTabBaignoire() {
        tabPane.getSelectionModel().select(tabBaignoire);
        tabDemarrage.setDisable(true);
        tabBaignoire.setDisable(false);
        tabStatistiques.setDisable(false);
    }

    /**
     * Méthode qui démarre la simulation de la baignoire quand le bouton "Démarrer" est
     * cliqué par l'utilisateur.
     */
    @FXML
    void demarrerSimulation() {
        Instant top = Instant.now();
        simulationActive = true;
        // Modification partie graphique
        btnStart.setDisable(true);
        btnStop.setDisable(false);
        stackPaneBaignoire.getChildren().remove(imageBaignoire);
        stackPaneBaignoire.getChildren().add(rectBaignoire);
        rectBaignoire.setHeight(0.0);
        sldFuite.setVisible(false);
        lblDebitFuite.setVisible(false);
        lblTitleDebitFuite.setText("Réparer une fuite");
// debut, boucher trou, changer debit, arrêt
        // Initialisation des threads
//        for (Robinet rob : robinets) {
//            rob.start();
//        }
//
//        for (Fuite fui : fuites) {
//            fui.start();
//        }
        System.out.println("La simulation vient de démarrer");

    }

    /**
     * Méthode qui met fin à la simulation de la baignoire quand le bouton "Arrêter" est
     * cliqué par l'utilisateur.
     */
    @FXML
    void terminerSimulation() {
        simulationActive = false;
        btnStart.setDisable(false);
        btnStop.setDisable(true);
//        sliderBox.setVisible(false);
        stackPaneBaignoire.getChildren().add(imageBaignoire);
        stackPaneBaignoire.getChildren().remove(rectBaignoire);
        sldRobinet.setValue(sldRobinet.getMin());
        sldFuite.setValue(sldFuite.getMin());
        // TODO remove in demarrer
        sldFuite.setVisible(true);
        lblDebitFuite.setVisible(true);
        lblTitleDebitFuite.setText("Débit fuites");
        // todo stop robinets
        System.out.println("La simulation vient de terminer");
    }

    @FXML
    void robinetDrag() {
        int idRob = listViewRobinets.getSelectionModel().getSelectedIndex();
        Robinet robinet = robinets.get(idRob);
        int nouveauDebit = (int) Math.round(sldRobinet.getValue());
        robinet.setDebit(nouveauDebit);
        sldRobinet.setDisable(true);
        sldRobinet.setValue(sldRobinet.getMin());
        System.out.printf("Débit du robinet %d : %d%n", robinet.getIdRobinet(), robinet.getDebit());
        try {Thread.sleep(500); // Attendre 0.5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listViewRobinets.getSelectionModel().clearSelection();
    }

    @FXML
    void fuiteDrag() {
        int idFuite = listViewFuites.getSelectionModel().getSelectedIndex();
        Fuite fuite = fuites.get(idFuite);
        int nouveauDebit = (int) Math.round(sldFuite.getValue());
        fuite.setDebit(nouveauDebit);
        sldFuite.setDisable(true);
        sldFuite.setValue(sldFuite.getMin());
        System.out.printf("Débit de la fuite %d : %d%n", fuite.getIdFuite(), fuite.getDebit());
        try {Thread.sleep(500); // Attendre 0.5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listViewFuites.getSelectionModel().clearSelection();
    }

    @FXML
    void listViewFuiSelect() {
        if (!simulationActive) {
            sldFuite.setDisable(false);
        } else {
            reparerFuite(listViewFuites.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    void listViewRobSelect() {
        sldRobinet.setDisable(false);
    }

    private void reparerFuite(int idFuite) {
        Fuite fuite = fuites.get(idFuite);
        fuite.setDebit(0);
        System.out.printf("Débit de la fuite %d : %d%n", fuite.getIdFuite(), fuite.getDebit());
        listViewFuites.getSelectionModel().clearSelection();
    }
}
