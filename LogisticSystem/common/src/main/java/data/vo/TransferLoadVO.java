package data.vo;

import java.util.ArrayList;
import java.util.Vector;

public class TransferLoadVO {

	public Vector<String>  header;
	public Vector<Vector<String>> orderInfo;
	
	
	public Vector<Vector<String>> getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(Vector<Vector<String>> orderInfo) {
		this.orderInfo = orderInfo;
	}
	
	public TransferLoadVO(ArrayList<String> orderInfo){
		this.orderInfo = new Vector<Vector<String>>();
		for(int i = 0 ; i < orderInfo.size();i++){
			String[] s = orderInfo.get(i).split("-");
			Vector<String> v = new Vector<String>();
			for(int j = 0 ; j < s.length;j++){
				v.add(s[j]);
			}
			this.orderInfo.add(v);
		}
		
		header = new Vector<String>();
		header.add("������");
		header.add("����");
		header.add("�ź�");
		header.add("�ܺ�");
		header.add("λ��");
		header.add("������kg��");
	}
}
