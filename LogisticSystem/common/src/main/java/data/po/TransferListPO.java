package data.po;

import java.util.ArrayList;

import data.enums.POType;
import data.enums.StorageArea;
import data.vo.TransferLoadVO;

/**
 * ׼��ɾ��
 * @author xu
 *
 */
public class TransferListPO extends DataPO {
    public long getTransferListID() {
		return transferListID;
	}

	public void setTransferListID(long transferListID) {
		this.transferListID = transferListID;
	}

	public long getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(long vehicleID) {
		this.vehicleID = vehicleID;
	}

	String date;//����
    long staff;//��װԱ
    String staffName;
   
    long transferListID;//��ת�����
	long transferCenter;//��ת���ı��
    String transferCenterName;
    long vehicleID;//���
    long targetCenter;//Ŀ����ת���ı��
    String targetCenterName;
    long[] order;//������
    String[] storagePosition;//area-row-shelf-num
    StorageArea transferType;//װ������
    double fee;//����
    boolean isStorageOut;//�Ƿ���ɳ���
    boolean account;//�Ƿ�����
    
    
//    public TransferListPO(TransferLoadVO vo, StorageArea transferType){
//    	//Ҫ��vo �� �Ѿ�ɾ��false �Ķ�����Ϣ 
//    	//Ҫ��vo �� area ת��Ϊ���ֱ��
//    	super(POType.TRANSFERLIST);
//    	String[][] info = vo.getOrderInfo();
//    	order = new long[info.length];
//    	storagePosition = new String[info.length];
//    	for(int i = 0 ; i < info.length;i++){
//    		long ID = Long.parseLong(info[i][0]);
//    		order[i] = ID;
//    		String p = info[i][1]+"-"+info[i][2]+"-"+info[i][3]+"-"+info[i][4];
//    		storagePosition[i] = p;
//    		this.transferType = transferType;
//    	}
//
//    }

    public boolean isStorageOut() {
		return isStorageOut;
	}
    
    public ArrayList<Long> getOrderArray(){
    	ArrayList<Long> orderNum = new ArrayList<Long>();
    	for(int i = 0 ; i < order.length;i++)
    		orderNum.add(order[i]);
    	
    	return orderNum;
    }



	public void setStorageOut(boolean isStorageOut) {
		this.isStorageOut = isStorageOut;
	}




	public long getStaff() {
		return staff;
	}




	public void setStaff(long staff) {
		this.staff = staff;
	}



	 public long getTargetCenter() {
			return targetCenter;
		}




		public void setTargetCenter(long targetCenter) {
			this.targetCenter = targetCenter;
		}




		public String getStaffName() {
			return staffName;
		}




		public void setStaffName(String staffName) {
			this.staffName = staffName;
		}



	public long getTarget() {
		return targetCenter;
	}




	public void setTarget(long target) {
		this.targetCenter = target;
	}




	public String getTransferCenterName() {
		return transferCenterName;
	}




	public void setTransferCenterName(String transferCenterName) {
		this.transferCenterName = transferCenterName;
	}




	public String getTargetCenterName() {
		return targetCenterName;
	}




	public void setTargetCenterName(String targetCenterName) {
		this.targetCenterName = targetCenterName;
	}




	public long[] getOrder() {
		return order;
	}




	public void setOrder(long[] order) {
		this.order = order;
	}




	public StorageArea getTransferType() {
		return transferType;
	}




	public void setTransferType(StorageArea transferType) {
		this.transferType = transferType;
	}




	public double getFee() {
		return fee;
	}




	public void setFee(double fee) {
		this.fee = fee;
	}




	public TransferListPO() {
        super(POType.TRANSFERLIST);
        this.account = false;
        isStorageOut = false;
    }




    public boolean isAccount() {
		return account;
	}

	public void setAccount(boolean account) {
		this.account = account;
	}

	public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTransferCenter() {
        return transferCenter;
    }

    public void setTransferCenter(long transferCenter) {
        this.transferCenter = transferCenter;
    }

    public long getVehicleCode() {
        return vehicleID;
    }

    public void setVehicleCode(long vehicleCode) {
        this.vehicleID = vehicleCode;
    }
    






    public String[] getStoragePosition() {
        return storagePosition;
    }

    public void setStoragePosition(String[] storagePosition) {
        this.storagePosition = storagePosition;
    }

}