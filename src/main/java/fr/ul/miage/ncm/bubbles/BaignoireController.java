package fr.ul.miage.ncm.bubbles;

import javafx.beans.binding.Bindings;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
 * Classe contrôleur pour la vue Baignoire.fxml.
 */
public class BaignoireController {
    /**
     * Logger pour la classe BaignoireController.
     */
    private static final Logger LOG = Logger.getLogger(BaignoireController.class.getName());

    // Éléments FXML
    @FXML
    TabPane tabPane;
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
    Slider sldRobinet;
    @FXML
    Label lblDebitRobinet;
    @FXML
    Label lblNiveauBaignoire;
    @FXML
    Label lblCapaciteBaignoire;
    @FXML
    ImageView imageBaignoire;
    @FXML
    Rectangle rectBaignoire;
    @FXML
    Tab tabBaignoire;
    @FXML
    Tab tabDemarrage;
    @FXML
    Tab tabStatistiques;
    @FXML
    ListView<Robinet> listViewRobinets = new ListView<>();
    @FXML
    ListView<Fuite> listViewFuites = new ListView<>();
    @FXML
    LineChart<Number, Number> lineChartBaignoire;
    @FXML
    HBox boxImages;
    // Fin éléments FXML

    private List<Robinet> robinets;
    private List<Fuite> fuites;
    private final Outils outils = new Outils();
    private int nbRobinets;
    private int nbFuites;
    private int nbEssaisBaignoireVide = 0;
    private boolean simulationActive = false;
    ScheduledExecutorService pool;
    Baignoire baignoire;
    List<Callable<Object>> taches = new ArrayList<>();
    List<Double> niveauBaignoire = new ArrayList<>();
    List<Double> temps = new ArrayList<>();
    ImageView[] imagesRobinets;
    ImageView[] imagesFuites;

    // TODO ajouter visuel fuites et robinets

    /**
     * Méthode qui initialise le contrôleur et crée un objet Baignoire, elle est appelée
     * automatiquement quand le fichier FXML est chargé.
     */
    @FXML
    public void initialize() {
        // Saisie capacité, nb de robinets et nb de fuites
        int capaciteMaxBaignoire = outils.saisirValeur(0, App.MAX_BAIGNOIRE,
                "Entrer la capacité de la baignoire (litres) : ");
        nbRobinets = outils.saisirValeur(App.MIN_ROBINETS, App.MAX_ROBINETS, "Entrer le nombre de robinets (1 à 10) : ");
        nbFuites = outils.saisirValeur(App.MIN_FUITES, App.MAX_FUITES, "Entrer le nombre de fuites (0 à 10) : ");
        imagesRobinets = new ImageView[nbRobinets];
        imagesFuites = new ImageView[nbFuites];

        baignoire = new Baignoire(capaciteMaxBaignoire);
        int debitDefaultRobinet = 5;
        robinets = outils.creerListeRobinets(debitDefaultRobinet, nbRobinets, baignoire);
        int debitDefaultFuite = 2;
        fuites = outils.creerListeFuites(debitDefaultFuite, nbFuites, baignoire);
        initialiserListeRobinets(robinets);
        initialiserListeFuites(fuites);
        baignoire = initialiserElementsFXML(baignoire);

        LOG.info("Heure de début : " + LocalTime.now());
        Instant top = Instant.now();
        pool = Executors.newScheduledThreadPool(nbRobinets + nbFuites);
    }

    /**
     * Méthode qui initialise des éléments FXML tels que les labels, sliders change d'onglet et fait
     * les bindings des valeurs aux éléments graphiques.
     * @param baignoire L'objet baignoire à initialiser.
     * @return L'objet baignoire intialisé.
     */
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
        boxImages.getChildren().addAll(imagesFuites);
        boxImages.getChildren().addAll(imagesRobinets);

        return baignoire;
    }

    /**
     * Méthode qui initialise la liste des robinets dans le listView à partir de la liste de Robinet
     * et ajoute les images des robinets à la liste d'image view.
     * @param robinets La liste des robinets.
     */
    private void initialiserListeRobinets(List<Robinet> robinets) {
        // Vider liste s'il y a encore des robinets
        listViewRobinets.getItems().clear();
        listViewRobinets.getItems().setAll(robinets);
        // Ajouter les images
        for (int i = 0; i < nbRobinets; i++) {
            Image imageRob = new Image(getClass().getResource("/faucet.png").toString());
            ImageView imageViewRob = new ImageView(imageRob);
            imageViewRob.setFitHeight(App.HEIGHT_ICONS);
            imageViewRob.setFitWidth(App.WIDTH_ICONS);
            imageViewRob.setPreserveRatio(true);
            imagesRobinets[i] = imageViewRob;
        }
    }

    /**
     * Méthode qui initialise la liste des fuites dans le listView à partir de la liste de Fuite et
     * ajoute les images des fuites à la liste d'image view.
     * @param fuites La liste des fuites.
     */
    private void initialiserListeFuites(List<Fuite> fuites) {
        // Vider liste s'il y a encore des fuites
        listViewFuites.getItems().clear();
        listViewFuites.getItems().setAll(fuites);
        // Ajouter les images
        for (int i = 0; i < nbFuites; i++) {
            Image imageFuite = new Image(getClass().getResource("/leak.png").toString());
            ImageView imageViewFuite = new ImageView(imageFuite);
            imageViewFuite.setFitHeight(App.HEIGHT_ICONS);
            imageViewFuite.setFitWidth(App.WIDTH_ICONS);
            imageViewFuite.setPreserveRatio(true);
            imagesFuites[i] = imageViewFuite;
        }
    }

    /**
     * Méthode qui change l'onglet sélectionné et désactive l'onglet de démarrage.
     */
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
        updateGraphiqueCSV(top);

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
        taches.addAll(initialiserThreads(robinets, top));
        taches.addAll(initialiserThreads(fuites, top));
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

    /**
     * Ajoute le niveau actuel de la baignoire et l'instant auquel cette donnée est observée et ajoute
     * au line chart les nouvelles données observées.
     * @param top Instant donné quand la fonction est appelée.
     */
    private void updateGraphiqueCSV(Instant top) {
        java.time.Duration duration = java.time.Duration.between(top, Instant.now());
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.setName(Long.toString(duration.toMillis()));

        niveauBaignoire.add((double) baignoire.getNiveauActuel());
        temps.add((double) duration.toMillis());
        series.getData().add(new XYChart.Data<>(temps.get(temps.size()-1),
                niveauBaignoire.get(niveauBaignoire.size()-1)));

        lineChartBaignoire.getData().add(series);
    }

    /**
     * Méthode qui initialise les threads pour les robinets et les fuites.
     * @param elements  La liste de robinets ou de fuites.
     * @param top       L'instant de début de la simulation.
     * @param <T>       Le type de la liste (Robinet ou Fuite).
     * @return          La collection de Callable représentant les tâches des threads.
     */
    private <T extends ScheduledService<Baignoire>> Collection<? extends Callable<Object>>
    initialiserThreads(List<? extends T> elements, Instant top) {
            List<Callable<Object>> taches = new ArrayList<>();

            for (T elem : elements) {
                Callable<Object> tache = () -> {
                    java.time.Duration duration = java.time.Duration.between(top, Instant.now());
                    elem.setOnSucceeded((WorkerStateEvent e) -> {
                        rectBaignoire.setHeight(baignoire.getNiveauActuel());
                        updateGraphiqueCSV(top);

                        if(baignoire.estRemplie()) {
                            System.out.printf("%nLa baignoire est prête pour un bain !%nTemps de remplissage : %ds.",
                                    duration.toMillis());
                            elem.cancel(); // Arrêter fuite ou robinet
                            terminerSimulation();
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
                    elem.setPeriod(Duration.millis(100));
                    elem.start();
                    return null;
                };
                taches.add(tache);
            }
            return taches;
        }

    /**
     * Méthode qui met fin à la simulation de la baignoire quand le bouton "Arrêter" est
     * cliqué par l'utilisateur, quand la baignoire est remplie ou quand elle est vidée.
     */
    @FXML
    void terminerSimulation() {
        simulationActive = false;
        btnStop.setDisable(true);
        sldRobinet.setValue(sldRobinet.getMin());
        sldFuite.setValue(sldFuite.getMin());
        listViewFuites.setDisable(true);
        listViewRobinets.setDisable(true);
        System.out.println("\nLa simulation vient de terminer. 🛀🏻");
        // Si la simulation s'est arrêtée car la baignoire est remplie
        if (baignoire.estRemplie()) {
            System.out.printf("%nVous avez utilisé %d litres pour remplir la baignoire. 💦%n",
                    baignoire.getLitresUtilises());
        } else { // si la simulation a été arrêtée manuellement
            System.out.printf("%nVous avez utilisé %d litres, la baignoire est à %.2f%% de sa capacité. 💦%n",
                    baignoire.getLitresUtilises(), baignoire.calculPourcentageRemp());
        }
        // TODO vérifier si c'est bon
        for (Robinet rob : robinets) {
            rob.cancel();
        }
        for (Fuite fui : fuites) {
            fui.cancel();
        }
        pool.shutdown();
        outils.exporterCSV(niveauBaignoire, temps);
    }

    /**
     * Méthode qui gère la modification du débit pour un robinet sélectionné.
     */
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

    /**
     * Méthode qui gère la modification du débit pour une fuite sélectionnée.
     */
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

    /**
     * Méthode qui gère la sélection d'une fuite dans la liste et active le slider pour modifier
     * son débit s'il n'y a pas de simulation en cours. Sinon, elle appelle la méthode qui répare
     * la fuite.
     */
    @FXML
    void listViewFuiSelect() {
        if (!simulationActive) {
            sldFuite.setDisable(false);
        } else {
            reparerFuite(listViewFuites.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Méthode qui gère la sélection d'un robinet dans la liste et active le slider pour modifier
     * son débit.
     */
    @FXML
    void listViewRobSelect() {
        sldRobinet.setDisable(false);
    }

    /**
     * Méthode qui permet de réparer une fuite lorsqu'il y a une simulation en cours.
     * @param idFuite L'identifiant de la fuite à réparer,
     */
    private void reparerFuite(int idFuite) {
        Fuite fuite = fuites.get(idFuite);
        fuite.setDebit(0);
        System.out.printf("Débit de la fuite %d : %d%n", fuite.getIdFuite(), fuite.getDebit());
        listViewFuites.getSelectionModel().clearSelection();
    }

    // TODO remove
//    @FXML
//    void afficherGraphique() {
//        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.setName("500");
//
//        niveauBaignoire.add((double) 60);
//        temps.add((double) 500);
//        series.getData().add(new XYChart.Data<>(temps.get(temps.size()-1),
//                niveauBaignoire.get(niveauBaignoire.size()-1)));
//
//        lineChartBaignoire.getData().add(series);
//    }
}
