module fr.ul.miage.ncm.bubbles {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.cli;
    requires java.logging;

    opens fr.ul.miage.ncm.bubbles to javafx.fxml;
    exports fr.ul.miage.ncm.bubbles;
}