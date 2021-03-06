package businesslogic.impl.transfer.center;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import businesslogic.impl.user.InstitutionInfo;
import utils.Timestamper;
import data.enums.DataState;
import data.enums.DataType;
import data.enums.POType;
import data.enums.StorageArea;
import utils.DataServiceFactory;
import data.message.ResultMessage;
import data.po.DataPO;
import data.po.TransferListPO;
import data.service.TransferDataService;
import data.vo.TransferListVO;
import data.vo.TransferLoadVO;

public class TransferList {
	TransferListPO transfer;
	TransferDataService transferData;
	ArrayList<DataPO> transferList;
	
	public long  saveTransferList(TransferListVO vo,long centerID) throws RemoteException{
		transfer = new TransferListPO();
		String[][] info = vo.orderAndPosition;
		String[] position = new String[info.length];
		long[] order = new long[info.length];
		String area = "0";
		transfer.setTransferType(StorageArea.PLANE);
		if (vo.transferType.equals("铁运")) {
			area = "1";
			transfer.setTransferType(StorageArea.TRAIN);
		}else if(vo.transferType.equals("汽运")){
			area = "2";
			transfer.setTransferType(StorageArea.TRUCK);
		}
		for (int i = 0; i < info.length; i++) {
			order[i] = Long.parseLong(info[i][0]);
			if (info[i][1].equals("机动区"))
				area = "3";
			position[i] = area + "-" + info[i][2] + "-" + info[i][3] + "-"
					+ info[i][4];
		}
		
		
		
		transfer.setSerialNum(centerID*10000+transfer.getSerialNum());
		transfer.setTransferListID(transfer.getSerialNum());
		transfer.setDriverName(vo.driver);
		transfer.setAccount(false);
		transfer.setDate(vo.date);
		transfer.setFee(Double.parseDouble(vo.fee));
		transfer.setOrder(order);
		transfer.setStaff(vo.staffID);
		transfer.setStaffName(vo.staff);
		transfer.setStorageOut(false);
		transfer.setStorageOut(false);
		transfer.setStoragePosition(position);
		transfer.setTarget(Long.parseLong(vo.target));
		transfer.setTargetCenterName(vo.targetName);
		transfer.setTransferCenter(Long.parseLong(vo.transferCenterID));
		transfer.setTransferCenterName(vo.transferCenter);
		transfer.setVehicleCode(vo.vehicleCode);
		
	
		 ResultMessage r = transferData.add(transfer);
		 if(r == ResultMessage.FAILED)
			 return -1;
		 return transfer.getSerialNum();
	}
	
	public  TransferListVO createTransferList(TransferLoadVO load,
			StorageArea transferType) {
		TransferListVO transferList = new TransferListVO();
		transferList.date = Timestamper.getTimeByDate();
		transferList.transferType = "航运";
		String area = "0";
		if (transferType == StorageArea.TRAIN){
			transferList.transferType = "铁运";
			area = "1";
		}
		else if(transferType == StorageArea.TRUCK){
			transferList.transferType = "汽运";
			area = "2";
		}
		Vector<Vector<String>> v = load.getOrderInfo();
		String[][] info = new String[v.size()][5];
		for(int i = 0 ; i < v.size();i++){
			Vector<String> vv = v.get(i);
			for(int j = 0 ; j < 5;j++){
				info[i][j] = vv.get(j);
			}
			info[i][1] = area;
		}
		transferList.orderAndPosition = info;

		return transferList;
	}
	
	public TransferListVO searchTransferList(long listID) throws RemoteException{
		transfer =  (TransferListPO) transferData.search(POType.TRANSFERLIST, listID);
		if(transfer == null){
			return null;
		}
		return new TransferListVO(transfer);
	}
	
	public TransferListPO getTransferList(){
		return transfer;
	}
	
	public TransferListVO getCheckedTransferList(long listID){
		for(DataPO d : transferList){
			if(d.getSerialNum() == listID){
				transfer = (TransferListPO) d;
				return new TransferListVO(transfer);
			}
		}
		
		return null;
	}
	
	
	public String[][] getBriefTranserList(long center) throws RemoteException{
		transferList = transferData.getNewlyApprovedPO(POType.TRANSFERLIST, center);
		String[][] transferInfo = new String[transferList.size()][2];
		for(int i = 0;i < transferList.size();i++){
			TransferListPO transfer = (TransferListPO) transferList.get(i);
			String[] in ={transfer.getSerialNum()+"",transfer.getDate()};
			transferInfo[i] = in;
		}	
		return transferInfo;
	}
	
	public TransferList(TransferDataService transferData){
		this.transferData = transferData;
	}
	
	public TransferList(){
		this.transferData = (TransferDataService) DataServiceFactory.getDataServiceByType(DataType.TransferDataService);
	}
}
