package App;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import os.OsController;
import produce.application.MyController;

public class AppController {
    @FXML
    private TabPane tabPane;
    // Inject tab content.
    @FXML private Tab osTabPage;
    // Inject controller
    @FXML private OsController osController;

    // Inject tab content.
    @FXML private Tab produceTabPage;
    // Inject controller
    @FXML private MyController myController;

    public void init() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable,
                                                                        Tab oldValue, Tab newValue) -> {
            if (newValue == produceTabPage) {
                System.out.println("Produce Tab page");
            } else if (newValue == osTabPage) {
                System.out.println("Os Tab page");
            }
        });
    }
}

