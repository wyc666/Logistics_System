package businesslogic.impl.transfer.center;

import java.lang.Thread.State;
import java.rmi.RemoteException;
import java.util.ArrayList;

import businesslogic.impl.order.OrderList;
import businesslogic.impl.transfer.hall.ArrivalList;
import businesslogic.impl.user.InstitutionInfo;
import businesslogic.service.Transfer.center.TransferReceiveService;
import businesslogic.service.order.OrderListService;
import data.enums.DataType;
import data.enums.POType;
import data.enums.StockStatus;
import data.enums.StorageArea;
import utils.DataServiceFactory;
import data.message.LoginMessage;
import data.message.ResultMessage;
import data.po.ArrivalPO;
import data.po.EntruckPO;
import data.po.OrderPO;
import data.po.TransferListPO;
import data.service.TransferDataService;
import data.vo.ArrivalListVO;
import data.vo.ArrivalVO;
import data.vo.DeliveryListVO;
import data.vo.EntruckListVO;
import data.vo.TransferListVO;

/**
 * 
 * @author xu
 *
 */
public class TransferReceive implements TransferReceiveService {
	TransferDataService transferData;
	TransferListPO transferList;
	EntruckPO entruckList;
	InstitutionInfo center;
	ArrivalList arrivalList;
	/**
	 * 获取中转单
	 * 
	 * @param code
	 * @return 中转单详细信息
	 * @throws RemoteException
	 */
	
	public ArrivalListVO getCheckedArrivalList(){
		try {
			return arrivalList.getCheckedArrivals(center.getCenterID());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrivalVO chooseArrival(long listID){
		ArrivalPO a =   arrivalList.chooseArrival(listID);
		return new ArrivalVO(a);
	}
	
	public TransferListVO getTransferList(long code) {
		try {
			transferList = (TransferListPO) transferData.search(
					POType.TRANSFERLIST, code);
			if (transferList == null) {
				return null;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return new TransferListVO(transferList);
	}

	public EntruckListVO getEntruckList(long code) {
		try {
			entruckList = (EntruckPO) transferData.search(POType.ENTRUCK, code);
			if (entruckList == null) {
				return null;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return new EntruckListVO(entruckList);
	}

	/**
	 * 生成默认到达单
	 * 
	 * @return 到达单信息
	 */
	public ArrivalVO createArriveList(POType type,DeliveryListVO deliveryList) {
		if (type == POType.TRANSFERLIST) {
			return arrivalList.createArrival((TransferListVO) deliveryList);
		} else {
			return arrivalList.createArrival((EntruckListVO) deliveryList);
		}
		
		
	}


	/**
	 * 保存到达单
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public ResultMessage saveArriveList(ArrivalVO arrival) {
		try {
			return arrivalList.saveArrival(arrival,center.getInstitutionID());
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.FAILED;
		}
	}
	
	
	public TransferReceive(InstitutionInfo center) {
		this.transferData = (TransferDataService) DataServiceFactory.getDataServiceByType(DataType.TransferDataService);
		arrivalList = new ArrivalList(transferData);
		this.center = center;
	}
	
	public ResultMessage doArrive() throws RemoteException{
		long[] roundOrder = null;
		long[] lostOrder = null;
		try {
		roundOrder =  arrivalList.getOrder(StockStatus.ROUND);
		lostOrder = arrivalList.getOrder(StockStatus.LOST);
		
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.FAILED;
		}
		//修改订单信息
		OrderListService orderList = new OrderList(new LoginMessage(ResultMessage.SUCCESS));
		orderList.modifyOrder(roundOrder, "由"+center.getInstitutionName()+"接收");
		orderList.modifyOrder(lostOrder, "订单于"+center.getInstitutionName()+"丢失");
		return ResultMessage.SUCCESS;
		
	}

	@Override
	public ResultMessage modifyTransferType(ArrivalVO a) throws RemoteException {
		String[][] info = a.getOrderAndStatus();
		long[] id = new long[info.length];
		for(int i = 0 ; i < info.length;i++){
			id[i] = Long.parseLong(info[i][0]);
		}
		new OrderList(new LoginMessage(ResultMessage.SUCCESS)).modifyOrderTransferType(id, StorageArea.TRUCK);
		return ResultMessage.SUCCESS;
	}

}
