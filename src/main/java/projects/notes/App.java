package projects.notes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {

    private static Stage stage;
    private double xOffset, yOffset;

    public static void main(String[] args) {
        launch();
    }

    public static void closeApp() {
        stage.close();
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            DataManager dataManager = new SQLiteDataManager();
            MainScreenController mainScreenController = new MainScreenController(dataManager);
            loader.setController(mainScreenController);
            Parent root = loader.load();
            makeWindowDraggable(root);
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (NullPointerException n) {
            System.err.println("Error in locating the fxml file: " + n.getMessage());
        } catch (IOException e) {
            System.err.println("Error in loading fxml: " + e.getMessage());
        }
    }

    private void makeWindowDraggable(Parent root) {
        root.setOnMousePressed(event -> {
//            Initial position of mouse cursor
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
//            ScreenX - xOffset -> the distance mouse moved
//            Set X to the distance moved to move window relative to its previous position
//            For dragging we need mouse coordinates of the screen so if we use sceneX instead of screen it won't work
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

}
