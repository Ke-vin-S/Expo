package New.Controllers;

import New.Classes.Category;
import New.Classes.Project;
import New.Classes.Spotlight;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SpotlightSceneController {

    @FXML
    private TableView<Project[]> spotlightTable;

    private double width = 200;

    private List<Integer> sumList = new ArrayList<>();

    private ObservableList<Project[]> spotlightList = FXCollections.observableArrayList();

    private Spotlight spotlight;

    private HashMap<Project, Integer> pointsMap = new HashMap<>();

    public void setSpotlight(Spotlight spotlight) {
        this.spotlight = spotlight;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @FXML
    public void initialize() {
        initializeSpotlightTable();
    }

    private void initializeSpotlightTable() {
        for (Category category : Category.values()) {
            TableColumn<Project[], Project> column = new TableColumn<>(category.name());
            column.setCellValueFactory(cellData -> {
                Project[] projects = cellData.getValue();
                for (Project project : projects) {
                    if (project != null && project.getCategory() == category) {
                        return new SimpleObjectProperty<>(project);
                    }
                }
                return new SimpleObjectProperty<>(null);
            });
            column.setCellFactory(new Callback<>() {
                @Override
                public TableCell<Project[], Project> call(TableColumn<Project[], Project> param) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(Project project, boolean empty) {
                            super.updateItem(project, empty);
                            if (empty || project == null) {
                                setGraphic(null);
                            } else {
                                VBox vbox = new VBox();
                                ImageView imageView = new ImageView();
                                Image image = project.getLogo();
                                imageView.setImage(image);
                                imageView.setFitHeight((width - 50) / 4);
                                imageView.setFitWidth((width - 50) / 4);
                                Label details = new Label(formatProjectDetails(project));

                                Button button = new Button("Give points");
                                button.setOnAction(event -> {
                                    handleButtonClick(project);
                                });
                                vbox.getChildren().addAll(imageView, details, button);
                                setGraphic(vbox);
                            }
                        }
                    };
                }
            });
            spotlightTable.getColumns().add(column);
        }
    }

    private String formatProjectDetails(Project project) {
        if (project == null) {
            return "";
        }
        return String.format(
                "ID: %d\nName: %s\nCategory: %s\nMembers: %s\nDescription: %s\nCountry: %s",
                project.getID(),
                project.getName(),
                project.getCategory(),
                String.join(", ", project.getMembers()),
                project.getDescription(),
                project.getCountry()
        );
    }

    public void setSpotlightProjects(List<Project> projects) {
        Project[] row = new Project[Category.values().length];
        for (Project project : projects) {
            row[project.getCategory().ordinal()] = project;
        }
        spotlightList.add(row);
        spotlightTable.setItems(spotlightList);
    }

    private List<Project> allProjects;

    private void shuffleProjects() {
        spotlightList.clear();
        Project[] row = new Project[Category.values().length];
        Random random = new Random();
        for (Category category : Category.values()) {
            List<Project> projectsInCategory = allProjects.stream()
                    .filter(project -> project.getCategory() == category)
                    .toList();
            if (!projectsInCategory.isEmpty()) {
                int randomIndex = random.nextInt(projectsInCategory.size());
                row[category.ordinal()] = projectsInCategory.get(randomIndex);
            }
        }
        spotlightList.add(row);
        spotlightTable.setItems(spotlightList);
    }

    @FXML
    private void handleShuffleButtonAction() {
        shuffleProjects();
    }

    @FXML
    private void handleSeeWinnersButtonAction() {
        showWinnersChart();
    }

    @FXML
    private void handleButtonClick(Project project) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_files/SpinnerWindow.fxml"));
        try {
            Parent root = loader.load();
            SpinnerWindowController controller = loader.getController();

            // Pass the callback function to SpinnerWindowController
            controller.initData(project, this::receiveSum);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Spinners for Project: " + project.getName());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveSum(Project project, int sum) {
        sumList.add(sum);
        // Update the pointsMap with the new sum for the project
        pointsMap.merge(project, sum, Integer::sum);
        System.out.println("Updated points map: " + pointsMap);
    }

    private void showWinnersChart() {
        List<Map.Entry<Project, Integer>> sortedProjects = pointsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_files/WinnersChart.fxml"));
            Parent root = loader.load();
            WinnersChartController controller = loader.getController();
            controller.setChartData(sortedProjects);

            Stage chartStage = new Stage();
            chartStage.initModality(Modality.APPLICATION_MODAL);
            chartStage.setTitle("Top 3 Projects");

            Scene chartScene = new Scene(root);
            chartStage.setScene(chartScene);
            chartStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
