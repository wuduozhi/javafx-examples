package os;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.ResourceBundle;


public class OsController implements Initializable,Runnable {
    @FXML
    private TextField processCountField;

    @FXML
    private ProgressBar curentProcessBar;

    @FXML
    private ProgressIndicator curentProcessIndicator;

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

    @FXML
    private Label curentProcessId;

    @FXML
    private Label curentProcessArrivalTime;

    @FXML
    private Label curentProcessProirity;

    @FXML
    private Label curentProcessWorkTime;

    @FXML
    private Label curentProcessTotalTime;

    final ToggleGroup radioGroup = new ToggleGroup();
    ObservableList<PCB> processList = null;
    Algorithm algorithm ;
    Timeline timeline;

    @FXML
    protected void handleStartButtonAction (ActionEvent event){
        this.processList = this.algorithm.dispatcher();

        Thread thread = new Thread(this);
        thread.start();
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
        curentProcessBar.setProgress(0.3);
        curentProcessIndicator.setProgress(0.6);
        curentProcessId.setText("zero");
    }

    public void animation(){
        while(!this.processList.isEmpty()){
            PCB pcb = this.processList.remove(0);
            double processNeedTime = Double.parseDouble(pcb.getNeedTime());
            double processWorkTime = Double.parseDouble(pcb.getWorkTime());

            while(processWorkTime < processNeedTime){
                double process = processWorkTime / processNeedTime;
                // 在 Javafx 主线程中修改
                Platform.runLater(() -> {
                    this.curentProcessBar.setProgress(process);
                    this.curentProcessIndicator.setProgress(process);
                    this.setCurentPcb(pcb);
                });
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    System.out.println("Thread sleep error");
                }
                processWorkTime += 0.5;
                pcb.setWorkTime(String.valueOf(processWorkTime));
            }
        }
    }

    @Override
    public void run() {
        this.animation();
    }

    public void setCurentPcb(PCB pcb){
        this.curentProcessId.setText(pcb.getProcessId());
        this.curentProcessArrivalTime.setText(pcb.getArrivalTime());
        this.curentProcessProirity.setText(pcb.getPriority());
        this.curentProcessWorkTime.setText(pcb.getWorkTime());
        this.curentProcessTotalTime.setText(pcb.getNeedTime());
    }

}
