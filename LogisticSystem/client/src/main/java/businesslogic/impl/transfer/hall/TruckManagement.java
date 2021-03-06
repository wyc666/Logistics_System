package businesslogic.impl.transfer.hall;

import java.rmi.RemoteException;
import java.util.ArrayList;

import data.enums.DataType;
import data.enums.POType;
import utils.DataServiceFactory;
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
	 * 修改一个车辆的状态信息为 货运
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
	 * 修改一个车辆状态信息为 空闲
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
		getTrucks();
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
			if(trucks == null){
				return null;
			}
			info = new String[trucks.size()][2];
		} catch (RemoteException e) {
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
		VehicleInfoPO truck = new VehicleInfoPO(newTruck);
		long num = truck.getSerialNum()+ user.getInstitutionID()*10000;
		truck.setSerialNum(num);
		try {
			return transferData.add(truck);
		} catch (RemoteException e) {
			return ResultMessage.FAILED;
		}
	}

	@Override
	public ResultMessage modifyTruck(TruckInfoVO truck) {
		VehicleInfoPO d = new VehicleInfoPO(truck);
		d.setSerialNum(truck.serialNum);
		try {
			return transferData.modify(d);
		} catch (RemoteException e) {
			return ResultMessage.FAILED;
		}
	}

	@Override
	public ResultMessage deleteTruck(long truckID) {
		for(VehicleInfoPO d:trucks){
			if(d.getSerialNum() == truckID){
				try {
					return transferData.delete(d);
				} catch (RemoteException e) {
					return ResultMessage.FAILED;
				}
				
			}
		}
		return ResultMessage.FAILED;
	}

}
