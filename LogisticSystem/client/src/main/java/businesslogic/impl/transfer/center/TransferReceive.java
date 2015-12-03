package businesslogic.impl.transfer.center;

import java.rmi.RemoteException;

import businesslogic.impl.transfer.hall.ArrivalList;
import businesslogic.impl.user.InstitutionInfo;
import businesslogic.service.Transfer.center.TransferReceiveService;
import data.enums.DataType;
import data.enums.POType;
import data.factory.DataServiceFactory;
import data.message.ResultMessage;
import data.po.ArrivalPO;
import data.po.EntruckPO;
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

	TransferListPO transferList;// ȷ��
	EntruckPO entruckList;// ȷ��
	
	InstitutionInfo center;
	ArrivalList arrivalList;
	/**
	 * ��ȡ��ת��
	 * 
	 * @param code
	 * @return ��ת����ϸ��Ϣ
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
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return new TransferListVO(transferList);
	}

	public EntruckListVO getEntruckList(long code) {
		try {
			entruckList = (EntruckPO) transferData.search(POType.ENTRUCK, code);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return new EntruckListVO(entruckList);
	}

	/**
	 * ����Ĭ�ϵ��ﵥ
	 * 
	 * @return ���ﵥ��Ϣ
	 */
	public ArrivalVO createArriveList(POType type,DeliveryListVO deliveryList) {
		if (type == POType.TRANSFERLIST) {
			return arrivalList.createArrival((TransferListVO) deliveryList);
		} else {
			return arrivalList.createArrival((EntruckListVO) deliveryList);
		}
	}

//	/**
//	 * �޸ĵ��ﵥ��Ϣ
//	 * 
//	 * @param vo
//	 */
//	public void modifyArriveList(ArrivalVO vo) {
//		arrival.setDate(vo.getDate());
//		arrival.setStockStatus(vo.getStatus());
//	}

	/**
	 * ���浽�ﵥ
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public ResultMessage saveArriveList(ArrivalVO arrival) {
		try {
			return arrivalList.saveArrival(arrival);
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.FAILED;
		}
	}
	
	
	public TransferReceive(InstitutionInfo center) {
		//transferData = (TransferDataService) DataServiceFactory
		//		.getDataServiceByType(DataType.TransferDataService);
		arrivalList = new ArrivalList(transferData);
		this.center = center;
	}
	
	public ResultMessage doArrive(){
		long[] order = null;
		try {
		order =  arrivalList.doArrive();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultMessage.FAILED;
		}
		//�޸Ķ�����Ϣ
		
		return ResultMessage.SUCCESS;
	}

}