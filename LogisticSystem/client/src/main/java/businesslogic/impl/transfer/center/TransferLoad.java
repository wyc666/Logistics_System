package businesslogic.impl.transfer.center;

import java.rmi.RemoteException;
import java.util.ArrayList;
import businesslogic.impl.order.OrderBLController;
import businesslogic.impl.storage.StorageInfo;
import businesslogic.impl.transfer.TransferInfo;
import businesslogic.impl.user.CityInfo;
import businesslogic.impl.user.InstitutionInfo;
import businesslogic.service.Transfer.center.TransferLoadService;
import businesslogic.service.order.OrderBLService;
import data.enums.DataType;
import data.enums.StorageArea;
import data.factory.DataServiceFactory;
import data.message.ResultMessage;
import data.po.OrderPO;
import data.po.TransferListPO;
import data.service.CompanyDataService;
import data.service.StorageDataService;
import data.service.TransferDataService;
import data.vo.StoragePositionAndOrderID;
import data.vo.TransferListVO;
import data.vo.TransferLoadVO;

public class TransferLoad implements TransferLoadService{
	CityInfo city;
	InstitutionInfo center;
	StorageArea transferType;
	
	StorageInfo storageInfo;
	TransferListPO transferList;//ȷ��
	ArrayList<OrderPO> orders;
	String targetInstitutionName;
	long desID;
	double transferFee;
	
	
	/**
	 * ������ѡ�������� ȷ��Ŀ�ĵ��б�
	 * @param type
	 * @return Ŀ�ĵ��б� name+ID
	 * @throws RemoteException
	 */
	public ArrayList<String> chooseTransferType(StorageArea type){
		transferType = type;
		
		//����Ϊ���˻����ˣ�����������ת������Ϣ
		if(type == StorageArea.PLANE || type == StorageArea.TRAIN){
			return city.getOtherCitys();
		}else{//���ر���ת��������Ӫҵ����Ϣ !!!!!!!!!!!!!!!!!!!!!!!!!!!!�Լ�������ת����
			ArrayList<String> dest = new ArrayList<String>();
			ArrayList<String> otherCitys = city.getOtherCitys();
			ArrayList<String> halls;
			try {
				halls = city.getHalls();
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
			dest.addAll(otherCitys);
			dest.addAll(halls);
			return dest;
		}
	}


	
	
	public TransferLoad(InstitutionInfo user) throws RemoteException{
		this.center = user;
		//CompanyDataService companyData = (CompanyDataService) DataServiceFactory.getDataServiceByType(DataType.CompanyDataService);
		//StorageDataService storageData = (StorageDataService) DataServiceFactory.getDataServiceByType(DataType.StorageDataService);
		//this.city = new CityInfo(companyData, user.getInstitutionID());
		//storageInfo = new StorageInfo(storageData, user.getCenterID());
		orders = new ArrayList<OrderPO>();
	}
	/**
	 * ����Ŀ�ĵػ�ȡ�����б�
	 * 
	 * @param des
	 * @return
	 * @throws RemoteException 
	 */
	public TransferLoadVO getOrder(String desName){
		targetInstitutionName = desName+"��ת����";

		StoragePositionAndOrderID positionAndOrderID = storageInfo.getOrderID(transferType);	//�̶�������������
		OrderBLService orderBL = new OrderBLController();
		ArrayList<Long> orderID = positionAndOrderID.order;//ĳ�������ж���
		ArrayList<String> positionInfo = positionAndOrderID.position;//�ֿ�λ����Ϣ
		//ת��
		long[] orderIDArray = new long[orderID.size()];
		for(int i = 0 ;i < orderID.size();i++){
			orderIDArray[i] = orderID.get(i);
		}
		ArrayList<OrderPO> order = orderBL.search(orderIDArray);//����
		
		
		ArrayList<String> orderVO = new ArrayList<String>();//����Ҫ��Ķ�����Ϣ
		desID = city.getDestID(desName);
	
		
		//ɨ�趩����ȡ������Ŀ�ĵصģ�
		int index = 0;
		for(OrderPO o:order){
			long[] routine = o.getRoutine();
			if(routine[0] == desID){
				orderVO.add (o.getSerialNum()+"-"+positionInfo.get(index)+"-"+o.getWeight());
				index ++;
			}
		}		
	return new TransferLoadVO(orderVO);	
	}
	/**
	 * ���ݵ�ǰѡ�ж�������Ƿ񳬹�����������������
	 * @param vo
	 * @return
	 */
	public boolean checkCapacity(TransferLoadVO vo){
		double weight = 0;
		int counter = 0;
		String[][] order = vo.getOrderInfo();
		for(int i = 0 ; i < order.length;i++){
			if(order[i][1] == "true"){
				long orderID = Long.parseLong(order[i][0]);
				for(OrderPO o: orders){
					if(o.getSerialNum() == orderID)
						counter ++;
						weight+= o.getWeight();
				}
			}
		}
		//������
		if (transferType == StorageArea.PLANE) 
			if(counter > TransferInfo.planeCapacity) return false;
			else ;
		else if(transferType == StorageArea.TRAIN)
			if(counter > TransferInfo.trainCapacity) return false;
			else ;
		else
			if(transferType == StorageArea.TRUCK) 
				if(counter > TransferInfo.truckCapacity) return false;
				else ;
		
		//�������
		if(transferType == StorageArea.PLANE)
			if(weight <= (TransferInfo.planeCapacity * TransferInfo.Eachweight))
				return true;
			else return false;
		else if(transferType == StorageArea.TRAIN)
			if(weight <= (TransferInfo.trainCapacity * TransferInfo.Eachweight))
				return true;
			else return false;
		else {
			if(weight <= (TransferInfo.truckCapacity * TransferInfo.Eachweight))
				return true;
			else return false;
		}
	}
	/**
	 * ���ݵ�ǰѡ�ж���������ת��
	 * @param load
	 * @return
	 */
	public TransferListVO createTransferList(TransferLoadVO load,String staffName,long staffID,String centerName,long centerID){
		transferList = new TransferListPO();
		try {
			transferFee = city.getTransferFee(targetInstitutionName,transferType);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		transferList.setAccount(false);
		transferList.setDate("");//ʱ��δ����
		transferList.setFee(0.0);//���δ����
		transferList.setStaffName(staffName);
		transferList.setStaff(staffID);
		transferList.setStorageOut(false);
		transferList.setTarget(desID);
		transferList.setTransferType(transferType);
		transferList.setTransferCenter(centerID);
		transferList.setTransferCenterName(centerName);
		String[][] info = load.getOrderInfo();
		long[] order = new long[info.length];
		String[] position = new String[info.length];
		String area = "0";
		if(transferType == StorageArea.TRAIN) area = "1";
		else area = "2";
		for(int i = 0 ; i < info.length;i++){
			order[i] = Long.parseLong(info[i][0]);
			if(info[i][1].equals("������")) area = "3";
			position[i] = area+"-"+info[i][2]+"-"+info[i][3]+"-"+info[i][4];
		}
		transferList.setOrder(order);
		transferList.setStoragePosition(position);
		
		return new TransferListVO(transferList);
		}
	/**
	 * ȷ�ϱ�����ת��
	 * @param input
	 * @return
	 * @throws RemoteException 
	 */
	public ResultMessage saveTransferList(TransferListVO vo){
		TransferDataService transferData = (TransferDataService) DataServiceFactory.getDataServiceByType(DataType.TransferDataService);
		try {
			return transferData.add(transferList);
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.FAILED;
		}
		}
}