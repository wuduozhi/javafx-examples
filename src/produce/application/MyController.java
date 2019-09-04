package produce.application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import produce.someClass.Consumer;
import produce.someClass.Producer;
import produce.someClass.Product;
import produce.someClass.Repository;

public class MyController implements Initializable {
	
   /*********************************************ModelBegin****************************************************/
   private static Repository repository  ;//共享缓存区
   private LinkedList<Producer> producers=new LinkedList();//生产者链表
   private LinkedList<Consumer> consumers=new LinkedList();//消费者链表
   /*********************************************ModelEnd**********************************************/
	   
	
   /********************************************ViewBegin***********************************************/
   
   //生产者
   @FXML
   private TableView producerTable;
   @FXML
   private TextField selectProducer;
   @FXML
   private Button proPause;
   @FXML
   private Button proConti;
   @FXML
   private Button proRateDec;
   @FXML
   private Button proRateInc;
   @FXML
   private Button proClear;
   @FXML
   private Button proAdd;
   				
   //缓冲区
   @FXML
   private TableView productTable;
   
   //消费者
   @FXML
   private TableView consumerTable;
   @FXML
   private TextField selectConsumer;
   @FXML
   private Button conPause;
   @FXML
   private Button conConti;
   @FXML
   private Button conRateDec;
   @FXML
   private Button conRateInc;
   @FXML
   private Button conClear;
   @FXML
   private Button conAdd;
   
   
   /*********************************************ViewEnd************************************************/
   
  
   /*********************************************ControlBegin************************************************/
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {

       // TODO (don't really need to do anything here).
	   System.out.println("Init...");
	   
	   //创建缓冲区产品
	   repository=new Repository(this);
	   ObservableList<Product> productsData = FXCollections.observableArrayList(repository.getCache());
		ObservableList<TableColumn> observableListDuct = productTable.getColumns();
		observableListDuct.get(0).setCellFactory((col) -> {
	        TableCell<Alert, String> cell = new TableCell<Alert, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                        super.updateItem(item, empty);
	                        this.setText(null);
	                        this.setGraphic(null);
	                        if (!empty) {
	                                int rowIndex = this.getIndex() + 1;
	                                this.setText(String.valueOf(rowIndex));
	                        }
	                }
	        };
	        return cell;
		});
		for(int i=0;i<Product.field.length&&(i+1)<observableListDuct.size();i++) {
			observableListDuct.get(i+1).setCellValueFactory(new PropertyValueFactory(Product.field[i]));
		}
		productTable.setItems(productsData);
	   
	   //创建生产者
	   for(int i=0;i<3;i++) {
		   Producer pro=new Producer(this.repository, this);
		   producers.addLast( pro );
		   Thread proThread=new Thread(pro);
		   proThread.start(); 
	   }
	   ObservableList<Producer> producersData = FXCollections.observableList(producers);
		ObservableList<TableColumn> observableListPro = producerTable.getColumns();
		observableListPro.get(0).setCellFactory((col) -> {
	        TableCell<Alert, String> cell = new TableCell<Alert, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                        super.updateItem(item, empty);
	                        this.setText(null);
	                        this.setGraphic(null);
	                        if (!empty) {
	                                int rowIndex = this.getIndex() + 1;
	                                this.setText(String.valueOf(rowIndex));
	                        }
	                }
	        };
	        return cell;
		});
		for(int i=0;i<Producer.field.length&&(i+1)<observableListPro.size();i++) {
			observableListPro.get(i+1).setCellValueFactory(new PropertyValueFactory(Producer.field[i]));

		}
		producerTable.setItems(producersData);
	   //创建消费者
	   for(int i=0;i<3;i++) {
		   Consumer con=new Consumer(this.repository,this);
		   consumers.addLast( con );
		   Thread conThread=new Thread(con);
		   conThread.start(); 
	   }
	   ObservableList<Consumer> consumersData = FXCollections.observableList(consumers);
		ObservableList<TableColumn> observableListCon = consumerTable.getColumns();
		observableListCon.get(0).setCellFactory((col) -> {
	        TableCell<Alert, String> cell = new TableCell<Alert, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                        super.updateItem(item, empty);
	                        this.setText(null);
	                        this.setGraphic(null);
	                        if (!empty) {
	                                int rowIndex = this.getIndex() + 1;
	                                this.setText(String.valueOf(rowIndex));
	                        }
	                }
	        };
	        return cell;
		});
		for(int i=0;i<Consumer.field.length&&(i+1)<observableListCon.size();i++) {
			observableListCon.get(i+1).setCellValueFactory(new PropertyValueFactory(Consumer.field[i]));
		}
		consumerTable.setItems(consumersData);
	  

   }
   
   
   /*********************************************ControlBegin************************************************/
   //生产者
   @FXML
   public void ProClearAllClick() {
	   for(int i=0;i<producers.size();i++) {
		   producers.get(i).setToStop();;
	   }
	   producers.clear();
	   producerTable.refresh();
   }
   @FXML
   public void ProPauseAllClick() {
	   for(int i=0;i<producers.size();i++) {
		   producers.get(i).setToPause();
	   }
	   producerTable.refresh();
   }
   @FXML
   public void ProContiAllClick() {
	   for(int i=0;i<producers.size();i++) {
		   if( producers.get(i).currentStatusProperty().get().equals("PAUSE") )
		   producers.get(i).setToProduce();;
	   }
	   producerTable.refresh();
   }
   @FXML
   public void ProPauseClick() {
	   int index=0;
	   try {
		   Log(selectProducer.getText());
		   index=Integer.parseInt(selectProducer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<producers.size()) {
		   producers.get(index-1).setToPause();;
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   producerTable.refresh();
   }
   @FXML
   public void ProContiClick() {
	   int index=0;
	   try {
		   Log(selectProducer.getText());
		   index=Integer.parseInt(selectProducer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<producers.size()) {
		   if( producers.get(index-1).currentStatusProperty().get().equals("PAUSE") )	   
		   producers.get(index-1).setToProduce();
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   producerTable.refresh();
   }
   @FXML
   public void ProRateDecClick() {
	   int index=0;
	   try {
		   Log(selectProducer.getText());
		   index=Integer.parseInt(selectProducer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<producers.size()) {
		   producers.get(index-1).setDecRate();
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   producerTable.refresh();
   }
   @FXML
   public void ProRateIncClick() {
	   int index=0;
	   try {
		   Log(selectProducer.getText());
		   index=Integer.parseInt(selectProducer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<producers.size()) {
		   producers.get(index-1).setAddRate();;
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   producerTable.refresh();
   }
   @FXML
   public void ProClearClick() {
	   int index=0;
	   try {
		   Log(selectProducer.getText());
		   index=Integer.parseInt(selectProducer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<producers.size()) {
		   producers.get(index-1).setToStop();
		   producers.remove(index-1);
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   producerTable.refresh();
   }
   @FXML
   public void ProAddClick() {
	   Log("ProAddClick");
	   Producer pro=new Producer(this.repository, this);
	   producers.addLast( pro );
	   Thread proThread=new Thread(pro);
	   proThread.start(); 
	   producerTable.refresh();
   }
   
   
   //消费者
   @FXML
   public void ConClearAllClick() {
	   for(int i=0;i<consumers.size();i++) {
		   consumers.get(i).setToStop();;
	   }
	   consumers.clear();
	   consumerTable.refresh();
   }
   @FXML
   public void ConPauseAllClick() {
	   for(int i=0;i<consumers.size();i++) {
//		   if(consumers.get(i).currentStatusProperty().get().equals("CONSUME"))
			   consumers.get(i).setToPause();
	   }
	   consumerTable.refresh();
   }
   @FXML
   public void ConContiAllClick() {
	   for(int i=0;i<consumers.size();i++) {
		   if( consumers.get(i).currentStatusProperty().get().equals("PAUSE") )   
			   consumers.get(i).setToConsume();;
	   }
	   consumerTable.refresh();
   }
   @FXML
   public void ConPauseClick() {
	   int index=0;
	   try {
		   Log(selectConsumer.getText());
		   index=Integer.parseInt(selectConsumer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<consumers.size()) {
//		   if(consumers.get(index-1).currentStatusProperty().get().equals("CONSUME"))
		   consumers.get(index-1).setToPause();;
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   consumerTable.refresh();
   }
   @FXML
   public void ConContiClick() {
	   int index=0;
	   try {
		   Log(selectConsumer.getText());
		   index=Integer.parseInt(selectConsumer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<consumers.size()) {
		   if( consumers.get(index-1).currentStatusProperty().get().equals("PAUSE") )
		   consumers.get(index-1).setToConsume();;;
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   consumerTable.refresh();
   }
   @FXML
   public void ConRateDecClick() {
	   int index=0;
	   try {
		   Log(selectConsumer.getText());
		   index=Integer.parseInt(selectConsumer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<consumers.size()) {
		   consumers.get(index-1).setDecRate();
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   consumerTable.refresh();
   }
   @FXML
   public void ConRateIncClick() {
	   int index=0;
	   try {
		   Log(selectConsumer.getText());
		   index=Integer.parseInt(selectConsumer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<consumers.size()) {
		   consumers.get(index-1).setAddRate();;
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   consumerTable.refresh();
   }
   @FXML
   public void ConClearClick() {
	   int index=0;
	   try {
		   Log(selectConsumer.getText());
		   index=Integer.parseInt(selectConsumer.getText());
		   
	   }catch(NumberFormatException e) {
		   Log(e.toString());return;
	   }
	   if((index-1)>=0 && (index-1)<consumers.size()) {
		   consumers.get(index-1).setToStop();
		   consumers.remove(index-1);
	   }else {
		   Log(index+":Out Of Bound");
	   }
	   consumerTable.refresh();
   }
   @FXML
   public void ConAddClick() {
	   Log("ConAddClick");
	   Consumer con=new Consumer(this.repository,this);
	   consumers.addLast( con );
	   Thread conThread=new Thread(con);
	   conThread.start(); 
	   consumerTable.refresh();
   }
   
   //组件
   public void refreshProTable() {//刷新生产者表
	   producerTable.refresh();
   }
   public void refreshDucTable() {//刷新产品表
	   productTable.refresh();
   }
   public void refreshConTable() {//刷新消费者表
	   consumerTable.refresh();
   }
   
   /*********************************************ControlEnd************************************************/
   
   /**
    * 获取当前时间的公共方法
    * @return
    */
   public static String getCurTime() {
	   Date date = new Date();
	   SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	   String dateTimeString=dateFormat.format(date);
       return dateTimeString;
   }

   
   /**
    * 获取当前时间戳的公共方法
    * @return
    */
   public static String getTimeStamp() {
       String dateTimeString = Long.toString(new Date().getTime());
       return dateTimeString;
   }

   /**
    * 生成唯一ID的算法
    * @prefix 前缀
    * @return 前缀+当前时间
    */
   public static String getID(String prefix) {
       String dateTimeString = MyController.getTimeStamp();
       if(prefix==null)return dateTimeString+"-"+(int)(1000+Math.random()*(9999-1000+1));
       return prefix+dateTimeString+"-"+(int)(1000+Math.random()*(9999-1000+1));
   }
   /**
    * 生成日志
    * @param msg
    */
   public static void Log(String msg) {
	   System.out.println(msg);
   }
}
