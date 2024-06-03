/**
 * Module principal de l'application Bubbles &amp; Leaks qui contient les ressources nécessaires
 * pour son exécution, il permet l'injection de dépendances dans les contrôleurs FXML.
 */
module fr.ul.miage.ncm.bubbles {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.cli;
    requires java.logging;
    requires org.apache.commons.csv;

    opens fr.ul.miage.ncm.bubbles to javafx.fxml;
    exports fr.ul.miage.ncm.bubbles;
}