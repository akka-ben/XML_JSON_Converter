package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import service.XmlToJsonService;
import service.JsonToXmlService;
import util.FileUtils;

import java.io.File;
import javafx.scene.control.ChoiceBox;
import service.api.XmlToJsonApiService;
import service.api.JsonToXmlApiService;

/**
 * Contrôleur principal pour l'interface de conversion XML/JSON
 */
public class ConverterController {
    
    @FXML
    private TextArea inputTextArea;
    
    @FXML
    private TextArea outputTextArea;
    
    @FXML
    private Button loadXmlButton;
    
    @FXML
    private Button loadJsonButton;
    
    @FXML
    private Button xmlToJsonButton;
    
    @FXML
    private Button jsonToXmlButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private ChoiceBox<String> methodChoiceBox;
    
    private String currentType; // "xml" ou "json"
    
    /**
     * Initialisation du contrôleur
     */
    @FXML
    public void initialize() {
        // Configuration initiale
        outputTextArea.setEditable(false);
        saveButton.setDisable(true);
        
        // Choix de la méthode de conversion
        methodChoiceBox.getItems().addAll("From Scratch", "With API");
        methodChoiceBox.setValue("From Scratch"); // valeur par défaut
    }
    
    /**
     * Charge un fichier XML
     */
    @FXML
    private void handleLoadXml() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger un fichier XML");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers XML", "*.xml")
        );
        
        File file = fileChooser.showOpenDialog(loadXmlButton.getScene().getWindow());
        
        if (file != null) {
            try {
                String content = FileUtils.readFile(file);
                inputTextArea.setText(content);
                currentType = "xml";
                outputTextArea.clear();
                saveButton.setDisable(true);
                showInfo("Fichier XML chargé avec succès!");
            } catch (Exception e) {
                showError("Erreur lors du chargement du fichier XML: " + e.getMessage());
            }
        }
    }
    
    /**
     * Charge un fichier JSON
     */
    @FXML
    private void handleLoadJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger un fichier JSON");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers JSON", "*.json")
        );
        
        File file = fileChooser.showOpenDialog(loadJsonButton.getScene().getWindow());
        
        if (file != null) {
            try {
                String content = FileUtils.readFile(file);
                inputTextArea.setText(content);
                currentType = "json";
                outputTextArea.clear();
                saveButton.setDisable(true);
                showInfo("Fichier JSON chargé avec succès!");
            } catch (Exception e) {
                showError("Erreur lors du chargement du fichier JSON: " + e.getMessage());
            }
        }
    }
    
    /**
     * Convertit XML vers JSON
     */
    @FXML
    private void handleXmlToJson() {
        String xmlContent = inputTextArea.getText().trim();
        
        if (xmlContent.isEmpty()) {
            showWarning("Veuillez charger ou saisir du contenu XML d'abord.");
            return;
        }
        
        try {
            String jsonResult;

            // Vérification de la méthode choisie par l'utilisateur
            if ("From Scratch".equals(methodChoiceBox.getValue())) {
                // Appel du service de conversion manuel (DOM + StringBuilder)
                XmlToJsonService converter = new XmlToJsonService();
                jsonResult = converter.convert(xmlContent);
            } else {
                // Appel du service de conversion basé sur des API (Jackson Tree Model)
                XmlToJsonApiService converter = new XmlToJsonApiService();
                jsonResult = converter.convert(xmlContent);
            }

            // Affichage du résultat dans l'interface
            outputTextArea.setText(jsonResult);

            // Mise à jour du type courant pour la sauvegarde
            currentType = "json";
            saveButton.setDisable(false);

            showSuccess("XML → JSON conversion successful!");

        } catch (Exception e) {
            // Gestion centralisée des erreurs
            showError("Error during XML → JSON conversion: " + e.getMessage());
        }
    }
    
    /**
     * Convertit JSON vers XML
     */
    @FXML
    private void handleJsonToXml() {
        String jsonContent = inputTextArea.getText().trim();
        
        if (jsonContent.isEmpty()) {
            showWarning("Veuillez charger ou saisir du contenu JSON d'abord.");
            return;
        }
        
        try {
            String xmlResult;

            // Sélection dynamique de la méthode de conversion
            if ("From Scratch".equals(methodChoiceBox.getValue())) {
                // Conversion JSON → XML implémentée manuellement
                JsonToXmlService converter = new JsonToXmlService();
                xmlResult = converter.convert(jsonContent);
            } else {
                // Conversion JSON → XML via API (Jackson Tree Model)
                JsonToXmlApiService converter = new JsonToXmlApiService();
                xmlResult = converter.convert(jsonContent);
            }

            // Affichage du résultat XML
            outputTextArea.setText(xmlResult);

            currentType = "xml";
            saveButton.setDisable(false);

            showSuccess("JSON → XML conversion successful!");

        } catch (Exception e) {
            showError("Error during JSON → XML conversion: " + e.getMessage());
        }
    }
    
    /**
     * Sauvegarde le résultat dans un fichier
     */
    @FXML
    private void handleSave() {
        String content = outputTextArea.getText();
        
        if (content.isEmpty()) {
            showWarning("Aucun contenu à sauvegarder.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder le fichier");
        
        if ("xml".equals(currentType)) {
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers XML", "*.xml")
            );
            fileChooser.setInitialFileName("output.xml");
        } else {
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers JSON", "*.json")
            );
            fileChooser.setInitialFileName("output.json");
        }
        
        File file = fileChooser.showSaveDialog(saveButton.getScene().getWindow());
        
        if (file != null) {
            try {
                FileUtils.writeFile(file, content);
                showSuccess("Fichier sauvegardé avec succès: " + file.getName());
            } catch (Exception e) {
                showError("Erreur lors de la sauvegarde: " + e.getMessage());
            }
        }
    }
    
    /**
     * Efface le contenu des zones de texte
     */
    @FXML
    private void handleClear() {
        inputTextArea.clear();
        outputTextArea.clear();
        currentType = null;
        saveButton.setDisable(true);
    }
    
    // Méthodes utilitaires pour les alertes
    
    private void showSuccess(String message) {
        showAlert(AlertType.INFORMATION, "Succès", message);
    }
    
    private void showInfo(String message) {
        showAlert(AlertType.INFORMATION, "Information", message);
    }
    
    private void showWarning(String message) {
        showAlert(AlertType.WARNING, "Attention", message);
    }
    
    private void showError(String message) {
        showAlert(AlertType.ERROR, "Erreur", message);
    }
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}