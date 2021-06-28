package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PartiesApplication extends Application {

    private HashMap<String, HashMap<Integer, Double>> values;

    public PartiesApplication() {
        this.values = readFile("partiesdata.tsv");
    }

    public void start(Stage window) {
        NumberAxis xAxis = new NumberAxis(1964, 2012, 4);
        NumberAxis yAxis = new NumberAxis(0, 30, 5);

        xAxis.setLabel("Year");
        yAxis.setLabel("% Support");
        LineChart graph = new LineChart(xAxis, yAxis);


        graph.setTitle("Relative support of the parties");

        values.keySet().stream()
                .forEach(party -> {
                    XYChart.Series data = new XYChart.Series();
                    data.setName(party);

                    values.get(party).entrySet().stream()
                            .forEach(pair -> {
                            data.getData().add(new XYChart.Data(pair.getKey(), pair.getValue()));
                            });
                    graph.getData().add(data);
                });

        Scene view = new Scene(graph, 640 ,480);
        window.setScene(view);
        window.show();
    }
    public static void main(String[] args) {
        launch(PartiesApplication.class);
    }

    private HashMap<String, HashMap<Integer, Double>> readFile(String file) {
        HashMap<String, HashMap<Integer, Double>> values = new HashMap<>();
        ArrayList<Integer> yearList = new ArrayList<>();

        try(Scanner data = new Scanner(Paths.get(file))) {
            String row = data.nextLine();
            String[] pieces = row.split("\t");
            for (int i = 1; i < pieces.length; i++) {
                yearList.add(Integer.parseInt(pieces[i]));
            }

            while (data.hasNextLine()) {
                HashMap<Integer, Double> map = new HashMap<>();

                row = data.nextLine();
                String[] partyData = row.split("\t");

                String partyName = partyData[0];

                for (int i = 1; i < partyData.length; i++) {
                    if (!partyData[i].equals("-")) {

                        int year = yearList.get(i - 1);
                        String support = partyData[i];
                        double supportDouble = Double.parseDouble(support);
                        map.put(year, supportDouble);
                    }
                }
                values.put(partyName, map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

}
