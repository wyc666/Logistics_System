package businesslogic.service.Transfer.hall;

import java.rmi.RemoteException;

import data.message.ResultMessage;
import data.vo.ArrivalListVO;
import data.vo.ArrivalVO;
import data.vo.DeliveryListVO;
import data.vo.EntruckListVO;
import data.vo.SendListVO;
import data.vo.TransferListVO;

public interface EntruckReceiveService {
	/**
	 * ����������ĵ��ﵥ
	 * @return
	 * @throws RemoteException 
	 */
	public ArrivalListVO getCheckedArrivals() throws RemoteException;
	
	
	public ArrivalVO chooseArrival(long arrivalID);
	/**
	 * ����װ����
	 * @param listID
	 * @return
	 * @throws RemoteException 
	 */
	public TransferListVO searchTransferList(long listID) throws RemoteException;
	
	public EntruckListVO searchEntruckList(long listID) throws RemoteException;
	/**
	 * ���ɵ��ﵥ
	 * @return
	 */
	public ArrivalVO createArrival(DeliveryListVO vo);
	/**
	 * ���浽�ﵥ
	 * @param arrival
	 * @return
	 */
	public ResultMessage saveArrival(ArrivalVO arrival);
	/**
	 * �����ɼ���
	 * @return
	 */
	public SendListVO createSendList(long arrivalID);
	/**
	 * �����ɼ���
	 * @return
	 */
	public ResultMessage saveSendList(SendListVO sendList);
	
	
	public ResultMessage doArrive();
}