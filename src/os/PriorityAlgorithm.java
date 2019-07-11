package os;

import javafx.collections.ObservableList;

import java.util.Comparator;

public class PriorityAlgorithm extends Algorithm {
    public ObservableList<PCB> dispatcher(){
        Comparator<PCB> comparator = (pcb1, pcb2) -> pcb1.getPriority().compareTo(pcb2.getPriority());
        this.processList.sort(comparator);
        return this.processList;
    }
}
