package New.Controllers;

import New.Classes.Project;
import New.Classes.SumCallback;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

public class SpinnerWindowController {

    @FXML
    private Spinner<Integer> spinner1;

    @FXML
    private Spinner<Integer> spinner2;

    @FXML
    private Spinner<Integer> spinner3;

    @FXML
    private Spinner<Integer> spinner4;

    @FXML
    private Button calculateButton;

    private SumCallback callback;

    private Project project;

    public void setCallback(SumCallback callback) {
        this.callback = callback;
    }

    public void initData(Project project, SumCallback callback) {
        this.project = project;
        this.callback = callback;
    }

    @FXML
    private void initialize() {
        calculateButton.setOnAction(event -> {
            int sum = spinner1.getValue() + spinner2.getValue() + spinner3.getValue() + spinner4.getValue();

            // Invoke the callback function with the project and the sum
            callback.receiveSum(project, sum);

            Stage stage = (Stage) calculateButton.getScene().getWindow();
            stage.close();
        });
    }

}
