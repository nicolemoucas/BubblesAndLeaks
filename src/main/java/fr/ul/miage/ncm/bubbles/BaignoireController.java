package fr.ul.miage.ncm.bubbles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Classe contrôleur pour la vue Baignoire.fxml
 */
public class BaignoireController {
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;

    Baignoire baignoire;

    /**
     * Constructeur de BaignoireController
     */
    public BaignoireController() {
        System.out.println("Baignore controller initialized");
    }

    /**
     * Méthode qui initialise le contrôleur et crée un objet Baignoire, elle est appelée
     * automatiquement quand le fichier FXML est chargé.
     */
    @FXML
    protected void initialize() {
        baignoire = new Baignoire();
    }

    /**
     * Méthode qui démarre la simulation de la baignoire quand le bouton "Démarrer" est
     * cliqué par l'utilisateur.
     */
    @FXML
    void demarrerSimulation() {
        System.out.println("La simulation vient de démarrer");
    }
}
