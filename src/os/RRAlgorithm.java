package os;

import javafx.collections.ObservableList;

public class RRAlgorithm extends Algorithm {
    public ObservableList<PCB> dispatcher(){
        return this.processList;
    }
}
