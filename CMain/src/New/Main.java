package New;

import New.Classes.Category;
import New.Classes.Country;
import New.Classes.Project;
import New.Classes.ProjectLoader;
import New.Controllers.ProjectTableController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_files/ProjectTable.fxml"));
            AnchorPane root = loader.load();

            ProjectTableController controller = loader.getController();
            ProjectLoader projectLoader = new ProjectLoader();
            controller.initialize(projectLoader);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Project Management");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Sample data for testing
    public static List<Project> getSampleProjects() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(1, "Project Alpha", Category.One, new String[]{"Alice", "Bob"}, "Description Alpha", Country.SRI_LANKA, "C:\\Users\\KEVIN SANJULA\\IdeaProjects\\CMain\\src\\New\\Screenshot 2024-06-22 194500.png"));
        projects.add(new Project(2, "Project Beta", Category.Three, new String[]{"Charlie", "Dave"}, "Description Beta", Country.UNITED_KINGDOM, "gi.jpg"));
        projects.add(new Project(3, "Project Gamma", Category.Four, new String[]{"Eve", "Frank"}, "Description Gamma", Country.CANADA, "gi.jpg"));
        return projects;
    }
}
