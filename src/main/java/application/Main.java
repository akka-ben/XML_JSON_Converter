package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée de l'application XML ⇄ JSON Converter
 * Lanceur JavaFX
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Chargement du fichier FXML
            Parent root = FXMLLoader.load(getClass().getResource("/view/main_view.fxml"));
            
            // Configuration de la scène
            Scene scene = new Scene(root, 900, 700);
            
            // Configuration du stage
            primaryStage.setTitle("XML ⇄ JSON Converter");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du démarrage de l'application: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        launch();
    }
}