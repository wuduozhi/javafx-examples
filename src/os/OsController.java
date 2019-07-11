package os;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Window;

import java.util.ResourceBundle;


public class OsController implements Initializable {
    @FXML
    private TextField processCountField;

    @FXML
    private RadioButton fcfsRadioButton;
    @FXML
    private RadioButton sjfRadioButton;
    @FXML
    private RadioButton rrRadioButton;
    @FXML
    private RadioButton priorityRadioButton;

    @FXML
    private Button startButton;

    @FXML
    private TableView<PCB> processTableView;

    final ToggleGroup radioGroup = new ToggleGroup();
    ObservableList<PCB> processList = null;
    Algorithm algorithm ;

    @FXML
    protected void handleStartButtonAction (ActionEvent event){
        this.processList = this.algorithm.dispatcher();
    }

    @FXML
    protected void handleGenerateButtonAction(ActionEvent event){
        System.out.println("start button click");
        Window owner = startButton.getScene().getWindow();
        if(processCountField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter process count");
            return;
        }

        String algo = radioGroup.getSelectedToggle().getUserData().toString();

        // 初始化算法
        if(algo.equals("fcfs")){
            this.algorithm = new FCFSAlgorithm();
        }else if(algo.equals("priority")){
            this.algorithm = new PriorityAlgorithm();
        }
        int count = Integer.parseInt(processCountField.getText());
        this.processList = algorithm.generateProcessList(this.processList,count);

        System.out.println("process count:" + processCountField.getText());
        System.out.println("algo:"+algo);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void initialize(java.net.URL location, ResourceBundle resources) {
        System.out.println("initialize...");
        fcfsRadioButton.setToggleGroup(radioGroup);
        fcfsRadioButton.setUserData("fcfs");
        fcfsRadioButton.setSelected(true);
        rrRadioButton.setToggleGroup(radioGroup);
        rrRadioButton.setUserData("rr");
        priorityRadioButton.setToggleGroup(radioGroup);
        priorityRadioButton.setUserData("priority");
        sjfRadioButton.setToggleGroup(radioGroup);
        sjfRadioButton.setUserData("sjf");

        this.processList  = processTableView.getItems();
    }

}
