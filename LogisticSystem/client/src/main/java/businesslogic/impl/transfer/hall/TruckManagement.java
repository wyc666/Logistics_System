package businesslogic.impl.transfer.hall;

import java.rmi.RemoteException;
import java.util.ArrayList;

import data.enums.DataType;
import data.enums.POType;
import data.factory.DataServiceFactory;
import data.message.ResultMessage;
import data.po.DataPO;
import data.po.VehicleInfoPO;
import data.service.TransferDataService;
import data.vo.TruckInfoVO;
import data.vo.TruckListVO;
import businesslogic.impl.user.InstitutionInfo;
import businesslogic.service.Transfer.hall.TruckManagementService;

public class TruckManagement implements TruckManagementService {
	TransferDataService transferData;
	ArrayList<VehicleInfoPO> trucks;
	InstitutionInfo user;
	
	/**
	 * �޸�һ��������״̬��ϢΪ ����
	 * @param truckID
	 * @return
	 */
	public ResultMessage useTruck(long truckID){
		for(VehicleInfoPO d: trucks){
			if(d.getSerialNum() == truckID){
				d.setEngaged(true);
				try {
					return transferData.modify(d);
				} catch (RemoteException e) {
					e.printStackTrace();
					return ResultMessage.FAILED;
				}
			}
		}
		
		return ResultMessage.NOTEXIST;
	}
	
	/**
	 * �޸�һ������״̬��ϢΪ ����
	 * @param truckID
	 * @return
	 */
	public ResultMessage freeTruck(long truckID){
		for(VehicleInfoPO d: trucks){
			if(d.getSerialNum() == truckID){
				d.setEngaged(false);
				try {
					return transferData.modify(d);
				} catch (RemoteException e) {
					e.printStackTrace();
					return ResultMessage.FAILED;
				}
			}
		}
		
		return ResultMessage.NOTEXIST;
	}
	
	public long getAvailableTruck(){
		for(VehicleInfoPO d:trucks){
			if(!d.isEngaged()){
				return d.getSerialNum();
			}
		}
		
		return -1;
	}
	
	public  TruckManagement(InstitutionInfo user) throws RemoteException {
		this.user = user;
		transferData = (TransferDataService) DataServiceFactory.getDataServiceByType(DataType.TransferDataService);
	}
	//
	@Override

	public TruckListVO getTrucks() {
		ArrayList<DataPO> trucks;
		String[][] info;
		try {
			trucks = transferData.searchList(POType.VEHICLEINFO, user.getInstitutionID());
			info = new String[trucks.size()][2];
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		this.trucks = new ArrayList<VehicleInfoPO>();
		int index = 0;
		for(DataPO po: trucks){
			VehicleInfoPO d = (VehicleInfoPO)po;
			this.trucks.add(d);
			String[] truck = {d.getSerialNum()+"",d.getEngagedString()};
			info[index] = truck;
			index++;
		}
		
		return new TruckListVO(info);
	}

	@Override
	public TruckInfoVO chooseTruck(long truckID) {
		for(VehicleInfoPO d: trucks){
			if(d.getSerialNum() == truckID)
				return new TruckInfoVO(d);
		}
		return null;
	}

	@Override
	public ResultMessage addTruck(TruckInfoVO newTruck) {
		// TODO Auto-generated method stub
		VehicleInfoPO truck = new VehicleInfoPO(newTruck);
		try {
			return transferData.add(truck);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			return ResultMessage.FAILED;
		}
	}

	@Override
	public ResultMessage modifyTruck(TruckInfoVO truck) {
		VehicleInfoPO d = new VehicleInfoPO(truck);
		
		try {
			return transferData.modify(d);
		} catch (RemoteException e) {
			return ResultMessage.FAILED;
		}
	}

	@Override
	public ResultMessage deleteTruck(long truckID) {
		// TODO Auto-generated method stub
		for(VehicleInfoPO d:trucks){
			if(d.getSerialNum() == truckID){
				try {
					return transferData.delete(d);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					return ResultMessage.FAILED;
				}
				
			}
		}
		return ResultMessage.NOTEXIST;
	}

}