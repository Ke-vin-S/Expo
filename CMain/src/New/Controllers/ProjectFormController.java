package New.Controllers;

import New.Classes.Category;
import New.Classes.Country;
import New.Classes.Project;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProjectFormController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;

    @FXML
    private TextField membersField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ChoiceBox<Country> countryChoiceBox;

    @FXML
    private TextField logoField;

    private Project newProject;

    @FXML
    public void initialize() {
        categoryChoiceBox.setItems(FXCollections.observableArrayList(Category.values()));
        countryChoiceBox.setItems(FXCollections.observableArrayList(Country.values()));
    }

    @FXML
    private void handleBrowseButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            logoField.setText(selectedFile.getPath());
        }
    }

    @FXML
    private void handleSaveButtonAction() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            Category category = categoryChoiceBox.getValue();
            String[] members = membersField.getText().split(",");
            String description = descriptionField.getText();
            Country country = countryChoiceBox.getValue();
            String logoPath = logoField.getText();

            newProject = new Project(id, name, category, members, description, country, logoPath);

            Stage stage = (Stage) idField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            // Handle invalid input
            showAlert("Invalid ID", "The ID must be an integer.");
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleCancelButtonAction() {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }

    public void setProject(Project project) {
        idField.setText(String.valueOf(project.getID()));
        nameField.setText(project.getName());
        categoryChoiceBox.setValue(project.getCategory());
        membersField.setText(String.join(",", project.getMembers()));
        descriptionField.setText(project.getDescription());
        countryChoiceBox.setValue(project.getCountry());
        logoField.setText(project.getLogo().getUrl());
    }
    public Project getNewProject() {
        return newProject;
    }
}
