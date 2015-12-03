package businesslogic.impl.storage;

import java.rmi.RemoteException;
import java.util.ArrayList;

import mock.MockStorageDataService;
import mock.MockTransferDataService;
import businesslogic.impl.order.OrderBLController;
import businesslogic.impl.transfer.center.OrderList;
import businesslogic.impl.transfer.hall.ArrivalList;
import businesslogic.impl.user.InstitutionInfo;
import businesslogic.service.storage.StorageInService;
import data.enums.POType;
import data.message.ResultMessage;
import data.po.ArrivalPO;
import data.po.OrderPO;
import data.po.StorageInListPO;
import data.service.StorageDataService;
import data.service.TransferDataService;
import data.vo.ArrivalListVO;
import data.vo.ArrivalVO;
import data.vo.BriefArrivalAndStorageInVO;
import data.vo.StorageInVO;

public class StorageIn implements StorageInService{

	// ��ӿ�
	TransferDataService transferData;
	StorageDataService storageData;
	InstitutionInfo user;
	StorageInfo storageInfo;
	ArrivalList arrivalList;
	StorageList storageInList;
	OrderList orderList;
	/**
	 * 
	 * һ���µ������1.�½�һ����ⵥ��2.���������������ⵥ��������
	 * @return ���ﵥ�б�������������ⵥ�б�
	 */
	public BriefArrivalAndStorageInVO newStorageIn() {
		// listID+date
		String[][] arrivelistInfo = null;
		String[][] storageInListInfo = null;
		storageInListInfo = storageInList.getBriefStorageList();
		try {
			ArrivalListVO arrivals = arrivalList.getCheckedArrivals(user.getCenterID());
			arrivelistInfo = arrivals.info;
		} catch (RemoteException e) {
			System.out.println("���������ж�");
			e.printStackTrace();
			return null;
		}

		return new BriefArrivalAndStorageInVO(arrivelistInfo, storageInListInfo);

	}

	/**
	 * 
	 * Ҫ��鿴һ�����ﵥ��ϸ��Ϣ
	 * 
	 * @param arriveListcode
	 * @return ���ﵥ��ʾ��Ϣ
	 * @throws RemoteException
	 */
	public ArrivalVO getArriveList(long arriveListcode){
		ArrivalPO a=  arrivalList.chooseArrival(arriveListcode);
		return new ArrivalVO(a);
	}
	
	
	

	/**
	 * 
	 *
	 * Ҫ��鿴һ����ⵥ��Ϣ
	 * 
	 * @param storageInCode
	 * @return ��ⵥ��ʾ��Ϣ
	 * @throws RemoteException
	 */
	public StorageInVO getStorageIn(long storageInID)  {
	try {
		StorageInListPO storageInListPO = (StorageInListPO) storageInList.getCheckedStorageList(storageInID);
		return new StorageInVO(storageInListPO);
	} catch (RemoteException e) {
		System.out.println("���������ж�");
		e.printStackTrace();
		return null;
	}
	}

	/**
	 * �������������λ��
	 * 
	 * 
	 * @param institute
	 * @return ��ⵥչʾ��Ϣ
	 * @throws RemoteException
	 */
	public StorageInVO sort(ArrivalVO arrival) {
		long[] orderID = arrivalList.getOrderID(arrival);
		ArrayList<OrderPO> order = orderList.getOrderList(orderID);
		try {
			return storageInfo.allocateSpace(order);
		} catch (RemoteException e) {
			System.out.println("���������ж�");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �޸ĵ��ﵥ״̬Ϊ�����
	 */
	private void modifyArriveListState() {
		arrivalList.modifyArriveListState();
	}

	/**
	 * �޸Ķ���������Ϣ(δ��)
	 */
	private void modifyOrder(ArrayList<Long> orderID) {
		orderList.modifyOrder(orderID);
	}

	/**
	 * 
	 * �Ե�ǰ������ⵥ����������
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public ResultMessage doStorageIn(long StorageInID){
			StorageInListPO storageIn = null;
			try {
				storageIn = (StorageInListPO) storageInList.getStorageList(StorageInID);
			} catch (RemoteException e) {
				e.printStackTrace();
				return ResultMessage.FAILED;
			}
			modifyOrder(storageIn.getOrder());
		return ResultMessage.SUCCESS;
	}

	/**
	 * ȷ�����󣬸��¿����Ϣ�������ﵥ״̬�޸�Ϊ�����
	 * @return result
	 * @throws RemoteException
	 */
	public ResultMessage saveStorageInList(StorageInVO vo){
		modifyArriveListState();
		try {
			return storageInList.saveStorageInList(vo);
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.FAILED;
		}
	}

	public StorageIn(InstitutionInfo user,StorageInfo storageInfo,StorageDataService storageData) throws RemoteException {
//		transferData = (TransferDataService) DataServiceFactory
//				.getDataServiceByType(DataType.TransferDataService);
		transferData = new MockTransferDataService();
		this.storageData = storageData;
		this.user = user;
		this.storageInfo = storageInfo;
		orderList = new OrderList();
		arrivalList = new ArrivalList(transferData);
		storageInList = new StorageList(storageData, user.getCenterID(), POType.STORAGEINLIST);
	}



}