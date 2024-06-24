package New.Controllers;

import New.Classes.Project;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;

public class WinnersChartController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    public void initialize() {
        // Initialization if needed
    }

    public void setChartData(List<Map.Entry<Project, Integer>> sortedProjects) {
        // Clear any existing data
        barChart.getData().clear();

        // Create a new series with a title
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Project Points");

        // Add data to the series
        for (Map.Entry<Project, Integer> entry : sortedProjects) {
            Project project = entry.getKey();
            int points = entry.getValue();
            series.getData().add(new XYChart.Data<>(project.getName() + "\n(" + project.getCountry() + ")", points));
        }

        // Add series to the chart
        barChart.getData().add(series);

        // Customize appearance
        barChart.setCategoryGap(10);
        barChart.setBarGap(5);
        barChart.setTitle("Top 3 Projects");
        xAxis.setLabel("Project");
        yAxis.setLabel("Points");
    }
}
