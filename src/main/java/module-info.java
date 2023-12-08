module projects.notes {
    requires javafx.controls;
    requires javafx.fxml;


    opens projects.notes to javafx.fxml;
    exports projects.notes;
}