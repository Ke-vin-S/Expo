package New.Controllers;

import New.Classes.Project;
import New.Classes.ProjectLoader;
import New.Classes.Spotlight;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ProjectTableController {

    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, Integer> idColumn;

    @FXML
    private TableColumn<Project, String> nameColumn;

    @FXML
    private TableColumn<Project, String> categoryColumn;

    @FXML
    private TableColumn<Project, String> membersColumn;

    @FXML
    private TableColumn<Project, String> descriptionColumn;

    @FXML
    private TableColumn<Project, String> countryColumn;

    @FXML
    private TableColumn<Project, Image> logoColumn;

    @FXML
    private TableColumn<Project, Void> updateColumn;

    @FXML
    private TableColumn<Project, Void> deleteColumn;

    public ObservableList<Project> getProjectList() {
        return projectList;
    }

    private ObservableList<Project> projectList = FXCollections.observableArrayList();

    private ProjectLoader projectLoader;

    public ProjectTableController() {
        // No-argument constructor required by FXMLLoader
    }
    public void initialize(ProjectLoader projectLoader) {
        this.projectLoader = projectLoader;

        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        membersColumn.setCellValueFactory(data -> {
            String members = String.join(", ", data.getValue().getMembers());
            return new SimpleStringProperty(members);
        });
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        logoColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLogo()));
        logoColumn.setCellFactory(new Callback<TableColumn<Project, Image>, TableCell<Project, Image>>() {
            @Override
            public TableCell<Project, Image> call(TableColumn<Project, Image> param) {
                return new TableCell<Project, Image>() {
                    private final ImageView imageView = new ImageView();

                    {
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);
                    }

                    @Override
                    protected void updateItem(Image item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            imageView.setImage(item);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setOnAction(event -> {
                    Project project = getTableView().getItems().get(getIndex());
                    handleUpdateButtonAction(project);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Project project = getTableView().getItems().get(getIndex());
                    handleDeleteButtonAction(project);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        try {
            List<String> lines = projectLoader.load();
            LinkedList<LinkedList<String>> splitObjects = projectLoader.splitObjects(lines);
            List<Project> projects = projectLoader.makeProjectList(splitObjects);
            projectList.setAll(projects);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load projects from file: " + e.getMessage());
        }
        sortProjectsById(projectList);
        projectTable.setItems(projectList);
    }

    @FXML
    private void handleAddButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_files/ProjectForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Project");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            ProjectFormController controller = loader.getController();
            Project newProject = controller.getNewProject();
            if (newProject != null) {
                projectList.add(newProject);
                sortProjectsById(projectList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateButtonAction(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_files/ProjectForm.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Project");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ProjectFormController controller = loader.getController();
            controller.setProject(project); // Pre-fill the form with project details
            dialogStage.showAndWait();

            Project updatedProject = controller.getNewProject();
            if (updatedProject != null) {
                int index = projectTable.getItems().indexOf(project);
                projectTable.getItems().set(index, updatedProject);
                sortProjectsById(projectList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleDeleteButtonAction(Project project) {
        // no need to sort, delete doesn't affect sort
        projectTable.getItems().remove(project);
    }

    public void setProjectList(List<Project> projects) {
        projectList.setAll(projects);
    }

    private void saveProjectsToFile() {
        try {
            projectLoader.save(projectList);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save projects to file: " + e.getMessage());
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
    private void handleSaveButtonAction() {
        saveProjectsToFile();
    }

    private static void sortProjectsById(ObservableList<Project> projectList) {
        //bubble sort
        int n = projectList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (projectList.get(j).getID() > projectList.get(j + 1).getID()) {
                    Project temp = projectList.get(j);
                    projectList.set(j, projectList.get(j + 1));
                    projectList.set(j + 1, temp);
                }
            }
        }
    }
    @FXML
    private void handleSpotlightButtonAction() {
        try {
            Spotlight spotlight = new Spotlight(projectList);
            List<Project> spotlightProjects = spotlight.getProjects();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_files/SpotlightScene.fxml"));
            AnchorPane page = loader.load();
            SpotlightSceneController controller = loader.getController();
            controller.setSpotlightProjects(spotlightProjects);

            Stage currentStage = (Stage) projectTable.getScene().getWindow();
            double width = currentStage.getWidth();
            double height = currentStage.getHeight();
            controller.setWidth(width);

            Stage newStage = new Stage();
            Scene scene = new Scene(page);
            newStage.setScene(scene);
            newStage.setWidth(width);
            newStage.setHeight(height);

            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Spotlight scene: " + e.getMessage());
        }
    }


}
