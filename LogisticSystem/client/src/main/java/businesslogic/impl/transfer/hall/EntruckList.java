package businesslogic.impl.transfer.hall;

import java.rmi.RemoteException;
import java.util.ArrayList;

import businesslogic.impl.user.InstitutionInfo;
import data.enums.POType;
import data.message.ResultMessage;
import data.po.DataPO;
import data.po.EntruckPO;
import data.service.TransferDataService;
import data.vo.BriefEntruckListVO;
import data.vo.EntruckListVO;

public class EntruckList {
	ArrayList<DataPO> entruckList;
	EntruckPO choosedEntruck;
	TransferDataService transferData;
	public EntruckList(TransferDataService transferData){
		this.transferData = transferData;
	}
	
	public long[] getOrder(){
		ArrayList<Long> order =  choosedEntruck.getOrderList();
		long[] o = new long[order.size()];
		for(int i = 0 ; i < order.size();i++){
			o[i] = order.get(i);
		}
		
		return o;
	}
	
	public EntruckListVO chooseEntruckList(long id){
		for(DataPO d: entruckList){
			if(d.getSerialNum() == id){
				return new EntruckListVO((EntruckPO) d);
			}
		}
		
		return null;
	}
	
	public BriefEntruckListVO getEntruckList(long institutionID) throws RemoteException{
		entruckList = transferData.searchCheckedList(POType.ENTRUCK, institutionID);
		String[][] info = new String[entruckList.size()][2];
		for(int i = 0 ; i < entruckList.size();i++){
			EntruckPO e = (EntruckPO) entruckList.get(i);
			String[] in = {e.getSerialNum()+"",e.getLoadingDate()};
			info[i] = in;
		}
		return new BriefEntruckListVO(info);
	}
	
	public ResultMessage saveEntruckList(EntruckListVO entruckList) throws RemoteException {
		EntruckPO entruckPO = new EntruckPO();
		entruckPO.setDestID(entruckList.destID);
		entruckPO.setDestName(entruckList.destName);
		entruckPO.setEntruckListID(Long.parseLong(entruckList.entruckListID));
		entruckPO.setEscortID(entruckList.escortID);
		entruckPO.setEscortName(entruckList.escortName);
		entruckPO.setFee(Double.parseDouble(entruckList.fee));
		entruckPO.setFrom(Long.parseLong(entruckList.fromID));
		entruckPO.setLoadingDate(entruckList.loadingDate);
		entruckPO.setMonitorName(entruckList.monitorName);
		ArrayList<Long> order =  new ArrayList<Long>();
		String[][] info = entruckList.info;
		for(int i = 0 ; i < info.length;i++){
			order.add(Long.parseLong(info[i][0]));
		}
		entruckPO.setOrderList(order);
			return transferData.add(entruckPO);

	}

	
	
	public EntruckListVO createEntruckList(String[][] orders,InstitutionInfo user,String desName,String driverID,String driverName,String truckID) {

		
		EntruckListVO entruck = new EntruckListVO();
		entruck.escortID = user.getStaffID();
		entruck.info = orders;
		entruck.destName = desName;
		entruck.escortName = user.getStaffName();
		entruck.fromID = user.getInstitutionID()+"";
		entruck.fromName = user.getInstitutionName();
		entruck.loadingDate = null;
		entruck.fee = null;
		entruck.monitorName = user.getStaffName();
		entruck.driverID = driverID;
		entruck.driverName = driverName;
		entruck.vehicleID = truckID;
		
		return entruck;
	}
}