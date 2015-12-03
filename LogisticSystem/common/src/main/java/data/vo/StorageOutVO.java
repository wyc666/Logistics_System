package data.vo;

import data.po.StorageOutListPO;

/**
 * 
 * @author xu
 *

 */
public class StorageOutVO {
	//��ⵥ���
	String id;
	//������+position
    String[][] orderAndPosition;
    //����

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	String date;
    //װ�����ͣ��ɻ����𳵣�������
    String transferType;
    //��ת����װ������
    String transferListNum;
	//��ת���ı��
	String transferNum;
	public String[][] getOrderAndPosition() {
		return orderAndPosition;
	}
	public void setOrderAndPosition(String[][] orderAndPosition) {
		this.orderAndPosition = orderAndPosition;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public String getTransferListNum() {
		return transferListNum;
	}
	public void setTransferListNum(String deliveryListNum) {
		transferListNum = deliveryListNum;
	}
	
	public StorageOutVO(StorageOutListPO po){
		long[] order = po.getOrder();
		String[][] postion = po.getPosition();
		String[][] info = new String[order.length][5];
		
		for(int i = 0 ; i < order.length;i++){
			switch (info[i][0]){
			case "0":info[i][0] = "������";break;
			case "1":info[i][0] = "������";break;
			case "2":info[i][0] = "������";break;
			default: info[i][0] = "������";
			}
			
			String[] in = {order[i]+"",info[i][0],info[i][1],info[i][2],info[i][3]};
			info[i] = in;
		}
		
		this.orderAndPosition = info;
		this.transferListNum = po.getDeliveryListNum()+"";
		this.transferType = po.getTransferType();
		
	}
	
	public StorageOutVO(){
		
	}

	public String getTransferNum() {
		return transferNum;
	}

	public void setTransferNum(String transferNum) {
		this.transferNum = transferNum;
	}
}