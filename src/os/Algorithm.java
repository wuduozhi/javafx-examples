package os;

import javafx.collections.ObservableList;

import java.util.Random;

public class Algorithm {
    protected ObservableList<PCB> processList;
    protected int count;

    public Algorithm(){

    }

    public ObservableList<PCB> dispatcher(){
        return this.processList;
    }

    public ObservableList<PCB> generateProcessList(ObservableList<PCB> list,int count){
        this.count = count;
        this.processList = list;
        int maxArrivalTime = this.count;
        int maxPriority = 10;
        int maxNeedTime = 15;
        Random r = new Random();
        this.processList.removeAll();
        for(int i=0;i<this.count;i++){
            String arrivalTime = String.valueOf(r.nextInt(maxArrivalTime));
            String priority = String.valueOf(r.nextInt(maxPriority));
            String processId = String.valueOf(i+1);
            String needTime = String.valueOf(r.nextInt(maxNeedTime));
            PCB pcb = new PCB(processId,priority,arrivalTime,needTime);
            this.processList.add(pcb);
        }

        return this.processList;
    }

    public ObservableList<PCB> getProcessList() {
        return processList;
    }

    public PCB producePCB(){
        int maxArrivalTime = this.count;
        int maxPriority = 10;
        int maxNeedTime = 15;
        Random r = new Random();
        String arrivalTime = String.valueOf(r.nextInt(maxArrivalTime));
        String priority = String.valueOf(r.nextInt(maxPriority));
        String needTime = String.valueOf(r.nextInt(maxNeedTime));
        String processId = String.valueOf(r.nextInt(30));
        PCB pcb = new PCB(processId,priority,arrivalTime,needTime);

        return pcb;
    }
}
