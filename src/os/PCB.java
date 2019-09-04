package os;

import javafx.beans.property.SimpleStringProperty;

public class PCB {
    private final SimpleStringProperty processId = new SimpleStringProperty("");
    private final SimpleStringProperty priority = new SimpleStringProperty("");
    private final SimpleStringProperty arrivalTime = new SimpleStringProperty("");
    private final SimpleStringProperty workTime = new SimpleStringProperty("");
    private final SimpleStringProperty needTime = new SimpleStringProperty("");
    public PCB() {

    }

    public PCB(String processId,String priority,String arrivalTime,String needTime) {
        setProcessId(processId);
        setArrivalTime(arrivalTime);
        setPriority(priority);
        setNeedTime(needTime);
        this.setWorkTime("0");
    }

    public String getWorkTime() {
        return workTime.get();
    }

    public SimpleStringProperty workTimeProperty() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime.set(workTime);
    }

    public String getNeedTime() {
        return needTime.get();
    }

    public SimpleStringProperty needTimeProperty() {
        return needTime;
    }

    public void setNeedTime(String needTime) {
        this.needTime.set(needTime);
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

    @Override
    public String toString() {
        return "PCB{" +
                "processId=" + processId +
                ", priority=" + priority +
                ", arrivalTime=" + arrivalTime +
                ", workTime=" + workTime +
                ", needTime=" + needTime +
                '}';
    }
}
