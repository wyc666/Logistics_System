package data.vo;

import data.enums.StockStatus;
import data.po.ArrivalPO;

import java.util.ArrayList;

/**
 * pass
 * @author xu
 *
 */
public class ArrivalVO {
	//�������ڡ���ת����š������ء����ﵽ��״̬���𻵡���������ʧ��
	public ArrivalVO(){
		
	}


	public ArrivalVO(ArrivalPO arrival){

		deliveryListNum = arrival.getTransferList()+"";
		long[] order = arrival.getOrder();
		status = arrival.getStockStatus();
		orderAndStatus = new String[order.length][2];
		for(int i = 0 ; i < order.length;i++){
			String[] info = new String[2];
			info[0] = order[i]+"";
			info[1] = status.get(i)+"";
		}
		
		fromName = null;
		date = arrival.getDate();
		fromNum = arrival.getFrom()+"";
		
	}
    public ArrayList<StockStatus> getStatus() {
		return status;
	}
	public void setStatus(ArrayList<StockStatus> status) {
		this.status = status;
	}

	public String[][] getOrderAndStatus() {
		return orderAndStatus;
	}
	public void setOrderAndStatus(String[][] orderAndStatus) {
		this.orderAndStatus = orderAndStatus;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getFromNum() {
		return fromNum;
	}
	public void setFromNum(String fromNum) {
		this.fromNum = fromNum;
	}
	public String[] getHeader() {
		return header;
	}
	
	
	public String getDeliveryListNum() {
		return deliveryListNum;
	}


	public void setDeliveryListNum(String deliveryListNum) {
		this.deliveryListNum = deliveryListNum;
	}




	public String getFromName() {
		return fromName;
	}


	public void setFromName(String fromName) {
		this.fromName = fromName;
	}





	ArrayList<StockStatus> status;//����״̬

	String deliveryListNum;//��ת����װ������

	String[][] orderAndStatus;//������+״̬
    String date;//����
    String fromName;//������
    String  fromNum;//�����ر��
	String destName;//�����
    String[] header = {"�������","����״̬"};
	long id;
	long transferList;//��ת���ı��

	public long getTransferList() {
		return transferList;
	}

	public void setTransferList(long transferList) {
		this.transferList = transferList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}
}