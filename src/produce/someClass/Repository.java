package produce.someClass;

import produce.application.MyController;

/**
 * 仓库：共享缓冲区
 * @author HWJ
 *
 */
public class Repository {

	private Product[] cache;//固定大小的缓冲区
	private int proPtr;//生产者指针
	private int conPtr;//消费者指针
	private final static int maxCapacity=20;//仓库（缓冲区）最大容量
	private MyController myController;
	
	public Repository(MyController myController) {
		this.myController=myController;
		cache=new Product[maxCapacity];
		for(int i=0;i<maxCapacity;i++) {
			Product duct=new Product();
//			if(i>6&&i<12)duct.holdPlace();
			cache[i]=duct;
		}
		proPtr=maxCapacity-1;
		conPtr=maxCapacity-1;
	}
	
	public Product[] getCache() {
		return cache;
	}
	
	/**
	 * 放入一个产品的同步方法
	 * @param product
	 * @return
	 */
	public synchronized boolean pushAProduct(Product product) {
		
		if(cache[proPtr].isEmpty()) {//仓库还没满
			cache[proPtr].holdPlace(product);
			proPtr--;
			if(proPtr<0) proPtr=maxCapacity-1;
			return true;
		}else {//仓库已满
			return false;
		}
		
	}
	
	/**
	 * 取出一个产品的同步方法
	 * @return
	 */
	public synchronized Product popAProduct(  ) {
		Product product=new Product();
		
		if(!cache[conPtr].isEmpty()) {//仓库不为空
			//取出该产品
			product.holdPlace(cache[conPtr]);
			cache[conPtr].releasePlace();
			conPtr--;
			if(conPtr<0) conPtr=maxCapacity-1; 
		}
		
		return product;
	}
	
}
