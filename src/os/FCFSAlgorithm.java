package os;

import javafx.collections.ObservableList;

public class FCFSAlgorithm extends Algorithm {

    public ObservableList<PCB> dispatcher(){
       this.sort();
        return this.processList;
    }

    public void sort(){
        int length = this.processList.size();
        for(int i=0;i<length-1;i++){
            int index = i;
            for(int j=i+1;j<length;j++){
                if(Integer.parseInt(this.processList.get(index).getArrivalTime())> Integer.parseInt(this.processList.get(j).getArrivalTime())){
                    index = j;
                }
            }
            PCB mPcb = this.processList.get(index);
            PCB iPcb = this.processList.get(i);
            this.processList.set(i,mPcb);
            this.processList.set(index,iPcb);
        }
    }
}
