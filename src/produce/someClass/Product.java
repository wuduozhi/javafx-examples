package produce.someClass;

import produce.application.MyController;
import javafx.beans.property.SimpleStringProperty;

/**
 * 生产和消费的产品
 * @author HWJ
 *
 */
public class Product {
	public static String[] field= {//用来显示的变量名
			"id"
	};
	private SimpleStringProperty id = new SimpleStringProperty();//产品唯一ID,可由特定算法实现
	
	
	public Product() {	
		this.id.setValue("");
	}
	//放置产品
	public void holdPlace() {
		this.id.setValue(MyController.getID("product-"));
	}
	
	public void holdPlace(Product p) {
		this.id.setValue(p.getID());
	}
	//取出或释放产品
	public void releasePlace() {
		this.id.setValue("");
	}
	
	public boolean isEmpty() {
		if(this.id.get().equals(""))return true;
		return false;
	}
	public String getID() {
		return this.id.get();
	}
	public void setID(String id) {
		this.id.setValue(id);;
	}
	/*******************Property Begin*********************************/
	public SimpleStringProperty idProperty() {
		return id;
	}
	/*******************Property End*********************************/
}
