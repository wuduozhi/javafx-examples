package os;

import javafx.collections.ObservableList;

import java.util.Comparator;

public class FCFSAlgorithm extends Algorithm {

    public ObservableList<PCB> dispatcher(){
        Comparator<PCB> comparator = (pcb1, pcb2) -> pcb1.getArrivalTime().compareTo(pcb2.getArrivalTime());
        this.processList.sort(comparator);
        return this.processList;
    }
}
