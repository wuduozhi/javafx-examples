package produce.someClass;

import produce.application.MyController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 生产者
 * @author HWJ
 *
 */
public class Producer implements Runnable{
	public static String[] field= {//用来显示的变量名
			"tid",
			"produceRate",
			"createTime",
			"startRunTime",
			"currentStatus"
	};
	
	private SimpleStringProperty tid = new SimpleStringProperty();//线程标识符
	private SimpleIntegerProperty produceRate = new SimpleIntegerProperty();//生产速率sleep(10000/produceRate)
	private SimpleStringProperty createTime = new SimpleStringProperty();//创建时间
	private SimpleStringProperty startRunTime = new SimpleStringProperty();//启动时间
	private SimpleStringProperty currentStatus = new SimpleStringProperty();//当前状态
	private Repository repository;
	private MyController myController;
	enum STATUS{
		PAUSE,//被用户停止
		PRODUCE,//正在消费
		SLEEP,//缓冲区已满，陷入休眠，等待唤醒
		STOP//线程终止
	}
	
	public Producer(Repository repository, MyController myController) {
		this.repository=repository;
		this.myController=myController;
		createTime.setValue(MyController.getCurTime());
		produceRate.setValue(5);
		currentStatus.setValue(STATUS.PAUSE.toString());
	}	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.startRunTime.setValue(MyController.getCurTime());;
		this.tid.setValue(""+Thread.currentThread().getId());
		
		while(true) {
			try {
				if(currentStatus.get().equals(STATUS.STOP.toString())) {
					break;
				}
				else if(currentStatus.get().equals(STATUS.PAUSE.toString())) {
					Thread.sleep(1000);
					continue;
				}
				else if(currentStatus.get().equals(STATUS.PRODUCE.toString())) {
					Thread.sleep( 10000/produceRate.get() );
					Product product=new Product();
					product.holdPlace();
					if(this.repository.pushAProduct(product)) {
						synchronized(this.repository) {
							this.repository.notifyAll();//唤醒消费者
						}
						
						MyController.Log(tid.get()+":Produce a product->"+product.getID());
						this.myController.refreshDucTable();
					}
					else {//仓库已满
						this.setToSleep();	//开始休眠
						synchronized(this.repository) {	
							this.repository.wait();
						};
						if(this.currentStatus.get().equals(STATUS.SLEEP.toString()))//解决休眠期间被暂停
						this.setToProduce(); //结束休眠
					}
					continue;
				}
				else {
					Thread.sleep(100);
				}
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setDecRate() {
		if(produceRate.get()==1) {
			produceRate.setValue(1);
		}
		else {
			produceRate.setValue(produceRate.get()-1);
		}
	}
	public void setAddRate() {
		if(produceRate.get()==10) {
			produceRate.setValue(10);
		}
		else {
			produceRate.setValue(produceRate.get()+1);
		}
	}
	public void setToPause() {
		currentStatus.setValue(STATUS.PAUSE.toString());
		myController.refreshProTable();
	}
	public void setToProduce() {
		currentStatus.setValue(STATUS.PRODUCE.toString());
		myController.refreshProTable();
	}
	public void setToSleep() {
		currentStatus.setValue(STATUS.SLEEP.toString());
		myController.refreshProTable();
	}
	public void setToStop() {
		currentStatus.setValue(STATUS.STOP.toString());
		myController.refreshProTable();
	}
	

	/******************Property Begin**************************************/
	public SimpleStringProperty tidProperty() {
		return tid;
	}
	public SimpleStringProperty createTimeProperty() {
		return createTime;
	}
	public SimpleStringProperty startRunTimeProperty() {
		return startRunTime;
	}
	public SimpleStringProperty currentStatusProperty() {
		return currentStatus;
	}
	public SimpleIntegerProperty produceRateProperty() {
		return produceRate;
	}
	/******************Property End**************************************/
	

}
