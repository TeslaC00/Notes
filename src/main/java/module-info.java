module projects.notes {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens projects.notes to javafx.fxml;
    exports projects.notes;
}