package fr.ul.miage.ncm.bubbles;

import javafx.beans.binding.Bindings;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
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

    static ScheduledExecutorService thread;
    private List<Robinet> robinets;
    private List<Fuite> fuites;
    private Outils outils = new Outils();
    int nbRobinets;
    int nbFuites;
    int debitDefaultRobinet = 5;
    int debitDefaultFuite = 2;
    int nbEssaisBaignoireVide = 0;
    boolean simulationActive = false;
    ScheduledExecutorService pool;
    Baignoire baignoire;
    List<Callable<Object>> taches = new ArrayList<>();

    /**
     * Méthode qui initialise le contrôleur et crée un objet Baignoire, elle est appelée
     * automatiquement quand le fichier FXML est chargé.
     */
    @FXML
    public void initialize() {
        // Saisie capacité, nb de robinets et nb de fuites
        int capaciteMaxBaignoire = outils.saisirValeur(0, App.MAX_BAIGNOIRE,
                "Entrer la capacité de la baignoire (litres) : ");
        nbRobinets = outils.saisirValeur(1, 10, "Entrer le nombre de robinets (1 à 10) : ");
        nbFuites = outils.saisirValeur(0, 10, "Entrer le nombre de fuites (0 à 10) : ");

        baignoire = new Baignoire(capaciteMaxBaignoire);
        robinets = outils.creerListeRobinets(debitDefaultRobinet, nbRobinets, baignoire);
        fuites = outils.creerListeFuites(debitDefaultFuite, nbFuites, baignoire);
        initialiserListeRobinets(robinets);
        initialiserListeFuites(fuites);
        baignoire = initialiserElementsFXML(baignoire);

        System.out.println("\n" + LocalTime.now());
        Instant top = Instant.now();
        pool = Executors.newScheduledThreadPool(nbRobinets + nbFuites);
    }

    private Baignoire initialiserElementsFXML(Baignoire baignoire) {
        lblDebitRobinet.textProperty().bind(Bindings.format("%.0f", sldRobinet.valueProperty()));
        lblDebitFuite.textProperty().bind(Bindings.format("%.0f", sldFuite.valueProperty()));
        lblCapaciteBaignoire.textProperty().bind(Bindings.format("%.0f", (double) baignoire.getCapaciteMax()));
        // TODO update lors du remplissage
        lblNiveauBaignoire.textProperty().bind(Bindings.format("%.0f", (double) baignoire.getNiveauActuel()));
        btnStop.setDisable(true);
        sldFuite.setDisable(true);
        sldRobinet.setDisable(true);
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
        // Modification partie graphique
        btnStart.setDisable(true);
        btnStop.setDisable(false);
        stackPaneBaignoire.getChildren().remove(imageBaignoire);
        if (!stackPaneBaignoire.getChildren().contains(rectBaignoire)) {
            stackPaneBaignoire.getChildren().add(rectBaignoire);
        }
        rectBaignoire.setHeight(0.0);
        sldFuite.setVisible(false);
        lblDebitFuite.setVisible(false);
        lblTitleDebitFuite.setText("Réparer une fuite");

        // Initialisation des threads
        if (!simulationActive) {
            taches.addAll(initialiserThreadsRobinet(top));
            taches.addAll(initialiserThreadsFuites(top));
        }

        // Lancer tous les threads en même temps
        try {
            pool.invokeAll(taches);
        } catch (InterruptedException e) {
            LOG.warning("Une erreur s'est produite lors de l'exécution des threads pour les robinets et fuites.");
            e.printStackTrace();
        }

        simulationActive = true;
        System.out.println("\nLa simulation vient de démarrer. 🫧");
    }

    private Collection<? extends Callable<Object>> initialiserThreadsFuites(Instant top) {
        List<Callable<Object>> tachesFuites = new ArrayList<>();

        for (Fuite fui : fuites) {
            Callable<Object> tache = () -> {
                fui.setOnSucceeded((WorkerStateEvent e) -> {
                    rectBaignoire.setHeight(baignoire.getNiveauActuel());
                    if(baignoire.estRemplie()) {
                        java.time.Duration duration = java.time.Duration.between(top, Instant.now());
                        System.out.printf("La baignoire est prête pour un bain !%nTemps de remplissage : %dms.",
                                duration.toMillis());
                        fui.cancel(); // Arrêter fuite
                        // TODO remettre à zéro
                        btnStart.setDisable(false); // Active bouton Démarrer simulation
                        btnStop.setDisable(true);
//                        simulationActive = false;
                    } else if (baignoire.estVide()) {
                        if (nbEssaisBaignoireVide <= App.MAX_ESSAIS_BAIGNOIRE_VIDE) {
                            nbEssaisBaignoireVide ++;
                        } else {
                            System.out.println("""
                                    Les fuites empêchent le remplissage de la baignoire.
                                    La simulation sera arrêtée.""");
                            terminerSimulation();
                        }
                    }
                });
                fui.setPeriod(Duration.millis(100));
                fui.start();
                return null;
            };
            tachesFuites.add(tache);
        }
        return tachesFuites;
    }
// TODO gerre stable
    private List<Callable<Object>> initialiserThreadsRobinet(Instant top) {
        List<Callable<Object>> tachesRob = new ArrayList<>();

        for (Robinet rob : robinets) {
            Callable<Object> tache = () -> {
                rob.setOnSucceeded((WorkerStateEvent e) -> {
                    rectBaignoire.setHeight(baignoire.getNiveauActuel());
                    if (baignoire.estRemplie()) {
                        java.time.Duration duration = java.time.Duration.between(top, Instant.now());
                        System.out.printf("La baignoire est prête pour un bain !%nTemps de remplissage : %dms.",
                                duration.toMillis());
                        rob.cancel(); // Arrêter robinet
                        // TODO remettre à zéro
                        btnStart.setDisable(false); // Active bouton Démarrer simulation
                        btnStop.setDisable(true);
//                        simulationActive = false;
                    } else if (baignoire.estVide()) {
                        if (nbEssaisBaignoireVide <= App.MAX_ESSAIS_BAIGNOIRE_VIDE) {
                            nbEssaisBaignoireVide ++;
                        } else {
                            System.out.println("""
                                    Les fuites empêchent le remplissage de la baignoire.
                                    La simulation sera arrêtée.""");
                            terminerSimulation();
                        }
                    }
                });
                rob.setPeriod(Duration.millis(100));
                rob.start();
                return null;
            };
            tachesRob.add(tache);
        }
        return tachesRob;
    }

//    private List<Callable<Object>> initialiserThreadsObjets(Instant top, List<Object> objets) {private <T extends ScheduledService<Baignoire>> Collection<? extends Callable<Object>> initialiserThreads(List<? extends T> elements, Instant top) {
//            List<Callable<Object>> taches = new ArrayList<>();
//
//            for (T elem : elements) {
//                Callable<Object> tache = () -> {
//                    elem.setOnSucceeded((WorkerStateEvent e) -> {
//                        rectBaignoire.setHeight(baignoire.getNiveauActuel());
//                        if(baignoire.estRemplie()) {
//                            java.time.Duration duration = java.time.Duration.between(top, Instant.now());
//                            System.out.printf("La baignoire est prête pour un bain !%nTemps de remplissage : %dms.",
//                                    duration.toMillis());
//                            elem.cancel(); // Arrêter fuite ou robinet
//                            // TODO remettre à zéro
//                            btnStart.setDisable(false); // Active bouton Démarrer simulation
//                            btnStop.setDisable(true);
//                            simulationActive = false;
//                        } else if (baignoire.estVide()) {
//                            if (nbEssaisBaignoireVide <= App.MAX_ESSAIS_BAIGNOIRE_VIDE) {
//                                nbEssaisBaignoireVide ++;
//                            } else {
//                                System.out.println("""
//                                    Les fuites empêchent le remplissage de la baignoire.
//                                    La simulation sera arrêtée.""");
//                                terminerSimulation();
//                            }
//                        }
//                    });
//                    elem.setPeriod(Duration.millis(100));
//                    elem.start();
//                    return null;
//                };
//                taches.add(tache);
//            }
//            return taches;
//        }

    /**
     * Méthode qui met fin à la simulation de la baignoire quand le bouton "Arrêter" est
     * cliqué par l'utilisateur.
     */
    @FXML
    void terminerSimulation() {
        simulationActive = false;
        btnStart.setDisable(false);
        btnStop.setDisable(true);

        stackPaneBaignoire.getChildren().remove(rectBaignoire);
        if (!stackPaneBaignoire.getChildren().contains(imageBaignoire)) {
            stackPaneBaignoire.getChildren().add(imageBaignoire);
        }
        stackPaneBaignoire.getChildren().remove(rectBaignoire);
        sldRobinet.setValue(sldRobinet.getMin());
        sldFuite.setValue(sldFuite.getMin());
        // TODO remove in demarrer
        sldFuite.setVisible(true);
        lblDebitFuite.setVisible(true);
        lblTitleDebitFuite.setText("Débit fuites");
        // todo stop robinets
        System.out.println("\nLa simulation vient de terminer. 🛀🏻");
        // TODO vérifier si c'est bon
        for (Robinet rob : robinets) {
            rob.cancel();
        }
        for (Fuite fui : fuites) {
            fui.cancel();
        }
        pool.shutdown();
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
