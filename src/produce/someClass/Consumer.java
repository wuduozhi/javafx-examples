package produce.someClass;

import produce.application.MyController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 消费者
 * @author HWJ
 *
 */
public class Consumer implements Runnable{
	public static String[] field= {//用来显示的变量名
			"tid",
			"consumeRate",
			"createTime",
			"startRunTime",
			"currentStatus"
	};
	
	private SimpleStringProperty tid = new SimpleStringProperty();//线程标识符
	private SimpleIntegerProperty consumeRate = new SimpleIntegerProperty();//消费速率sleep(10000/consumeRate)
	private SimpleStringProperty createTime = new SimpleStringProperty();//创建时间
	private SimpleStringProperty startRunTime = new SimpleStringProperty();//启动时间
	private SimpleStringProperty currentStatus = new SimpleStringProperty();//当前状态
	private Repository repository;
	private MyController myController;
	enum STATUS{
		PAUSE,//被用户停止
		CONSUME,//正在消费
		SLEEP,//缓冲区为空，陷入休眠，等待唤醒
		STOP//线程终止
	}
	
	public Consumer(Repository repository, MyController myController) {
		this.repository=repository;
		this.myController=myController;
		createTime.setValue(MyController.getCurTime());
		consumeRate.setValue(5);
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
				if(currentStatus.get().equals(STATUS.PAUSE.toString())) {
					Thread.sleep(1000);
					continue;
				}
				if(currentStatus.get().equals(STATUS.CONSUME.toString())) {
					Thread.sleep( 10000/consumeRate.get() );
					Product product=this.repository.popAProduct();
					if(!product.isEmpty()) {
						MyController.Log(tid.get()+":Consume a product->"+product.getID());
						this.myController.refreshDucTable();
						synchronized(this.repository) {
							this.repository.notifyAll();
						};
						
					}
					else {//仓库已空
						this.setToSleep();	//开始休眠
						synchronized(this.repository) {
							this.repository.notifyAll();
							this.repository.wait();
						};
						if(this.currentStatus.get().equals(STATUS.SLEEP.toString()))//解决休眠期间被暂停
						this.setToConsume();; //结束休眠
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
		if(consumeRate.get()==1) {
			consumeRate.setValue(1);
		}
		else {
			consumeRate.setValue(consumeRate.get()-1);
		}
	}
	public void setAddRate() {
		if(consumeRate.get()==10) {
			consumeRate.setValue(10);
		}
		else {
			consumeRate.setValue(consumeRate.get()+1);
		}
	}
	public void setToPause() {
		currentStatus.setValue(STATUS.PAUSE.toString());
		myController.refreshConTable();
	}
	public void setToConsume() {
		currentStatus.setValue(STATUS.CONSUME.toString());
		myController.refreshConTable();
	}
	public void setToSleep() {
		currentStatus.setValue(STATUS.SLEEP.toString());
		myController.refreshConTable();
	}
	public void setToStop() {
		currentStatus.setValue(STATUS.STOP.toString());
		myController.refreshConTable();
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
	public SimpleIntegerProperty consumeRateProperty() {
		return consumeRate;
	}
	/******************Property End**************************************/
	

}
