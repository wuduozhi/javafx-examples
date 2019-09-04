package os;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
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

import java.util.Random;
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

    @FXML
    private CheckBox preEmptiveCheckBox;

    final ToggleGroup radioGroup = new ToggleGroup();
    ObservableList<PCB> processList = null;
    Algorithm algorithm ;
    String algo;
    boolean preEmptive;
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
        this.algo = algo;
        // 初始化算法
        if(algo.equals("fcfs")){
            this.algorithm = new FCFSAlgorithm();
        }else if(algo.equals("priority")){
            this.algorithm = new PriorityAlgorithm();
        }else if(algo.equals("sjf")){
            this.algorithm = new SJFAlgorithm();
        }else if(algo.equals("rr")){
            this.algorithm = new RRAlgorithm();
        }
        int count = Integer.parseInt(processCountField.getText());
        this.processList = algorithm.generateProcessList(this.processList,count);

        System.out.println("process count:" + processCountField.getText());
        System.out.println("algo:"+algo);

        this.preEmptive = preEmptiveCheckBox.isSelected();
        if (this.preEmptive){
            System.out.println("preEmptive select");
        }
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
        int curcleTime = 0;
        while(!this.processList.isEmpty()){
            PCB curPcb = this.processList.remove(0);
            double processNeedTime = Double.parseDouble(curPcb.getNeedTime());
            double processWorkTime = Double.parseDouble(curPcb.getWorkTime());

            while(processWorkTime < processNeedTime){
                double process = processWorkTime / processNeedTime;
                // 在 Javafx 主线程中修改
                Platform.runLater(() -> {
                    this.curentProcessBar.setProgress(process);
                    this.curentProcessIndicator.setProgress(process);
                    this.setCurentPcb(curPcb);
                });
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    System.out.println("Thread sleep error");
                }
                processWorkTime += 0.5;
                curPcb.setWorkTime(String.valueOf(processWorkTime));


                // RR 调度算法
                curcleTime ++;
                if (this.algo.equals("rr")){
                    if(curcleTime == 4){
                        this.processList.add(curPcb);
                        curcleTime = 0;
                        break;
                    }else{
                        continue;
                    }

                }
                //随机生成新的 pcb
                Random r = new Random();
                if (r.nextInt(30) > 15){
                    PCB nPcb = this.algorithm.producePCB();
                    boolean flag = false;
                    System.out.println("create new pcb" + nPcb);
                    // 是否抢占
                    if(this.preEmptive){
                        if(this.algo.equals("fcfs")){
                            if(Integer.parseInt(nPcb.getArrivalTime()) < Integer.parseInt(curPcb.getArrivalTime())){
                                System.out.println("preEmptive");
                                flag = true;
                            }else{
                                this.processList.add(nPcb);
                            }
                        }else if(this.algo.equals("priority")){
                            if(Integer.parseInt(nPcb.getPriority()) < Integer.parseInt(curPcb.getPriority())){
                                System.out.println("preEmptive");
                                flag = true;
                            }else{
                                this.processList.add(nPcb);
                            }
                        }else if(this.algo.equals("sjf")){
                            if(Integer.parseInt(nPcb.getNeedTime()) < Integer.parseInt(curPcb.getNeedTime())){
                                System.out.println("preEmptive");
                                flag = true;
                            }else{
                                this.processList.add(nPcb);
                            }
                        }

                        // 处理抢占对象
                        if(flag){
                            PCB  curPcbCopy = new PCB(curPcb.getProcessId(),curPcb.getPriority(),curPcb.getArrivalTime(),curPcb.getNeedTime());
                            curPcbCopy.setWorkTime(curPcb.getWorkTime());
                            this.processList.add(curPcbCopy);
                            curPcb.setPriority(nPcb.getPriority());
                            curPcb.setWorkTime(nPcb.getWorkTime());
                            curPcb.setNeedTime(nPcb.getNeedTime());
                            curPcb.setProcessId(nPcb.getProcessId());
                        }

                        this.processList = this.algorithm.dispatcher();

                    }else{
                        this.processList.add(nPcb);
                        this.processList = this.algorithm.dispatcher();
                    }
                }
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
