package projects.notes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    public TextArea mainTextArea;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new FXMLLoader(getClass().getResource("/Main.fxml")).load());
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
