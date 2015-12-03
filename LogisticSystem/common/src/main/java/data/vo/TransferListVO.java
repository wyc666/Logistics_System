package data.vo;

import data.po.TransferListPO;

/**
 * 
 * @author xu
 *
 */
public class TransferListVO extends DeliveryListVO {
	public String[] header= {"�������","����","�ź�","�ܺ�","λ��"};
	public String transferListID;//��ת��ID
    public String date;//����
    public String staff;//��װԱ
    public String transferCenter;//��ת����(from)
    public String transferCenterID;
    public String vehicleCode;//���
    public String target;//Ŀ�����
    public String targetName;//Ŀ���������
    public String[][] orderAndPosition;
    public String transferType;//װ������
    public String fee;//����


    
	public TransferListVO(TransferListPO po){
		this.date = po.getDate();
		this.staff = po.getStaffName();
		this.transferCenter = po.getTransferCenterName();
		this.transferListID = po.getTransferListID()+"";
		this.transferCenterID = po.getTransferCenter()+"";
		switch (po.getTransferType()){
		case PLANE:transferType = "����";
		case TRAIN:transferType = "����";
		default:transferType = "����";
		}

		this.fee = po.getFee()+"";
		
		long[] order = po.getOrder();
		String[] position = po.getStoragePosition();
		orderAndPosition = new String[order.length][5];
		for(int i = 0 ; i <order.length;i++){
			String[] pos = position[i].split("-");
			String[] in = {order[i]+"",pos[0],pos[1],pos[2],pos[3]}; 
			orderAndPosition[i] = in;
		}
	}


}