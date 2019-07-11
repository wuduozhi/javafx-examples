package os;

import javafx.beans.property.SimpleStringProperty;

public class PCB {
    private final SimpleStringProperty processId = new SimpleStringProperty("");
    private final SimpleStringProperty priority = new SimpleStringProperty("");
    private final SimpleStringProperty arrivalTime = new SimpleStringProperty("");

    public PCB() {

    }

    public PCB(String processId,String priority,String arrivalTime) {
        setProcessId(processId);
        setArrivalTime(arrivalTime);
        setPriority(priority);
    }


    public String getProcessId() {
        return processId.get();
    }

    public SimpleStringProperty processIdProperty() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId.set(processId);
    }

    public String getPriority() {
        return priority.get();
    }

    public SimpleStringProperty priorityProperty() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public String getArrivalTime() {
        return arrivalTime.get();
    }

    public SimpleStringProperty arrivalTimeProperty() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime.set(arrivalTime);
    }
}
