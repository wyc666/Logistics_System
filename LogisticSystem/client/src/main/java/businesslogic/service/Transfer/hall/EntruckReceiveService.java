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
	 * 获得已审批的到达单
	 * @return
	 * @throws RemoteException 
	 */
	public ArrivalListVO getCheckedArrivals() throws RemoteException;
	
	/**
	 * 在获得的到达单里选择一条并显示详细
	 * @param arrivalID
	 * @return
	 */
	public ArrivalVO chooseArrival(long arrivalID);
	/**
	 * 搜索中转单
	 * @param listID
	 * @return
	 * @throws RemoteException 
	 */
	public TransferListVO searchTransferList(long listID) throws RemoteException;
	
	/**
	 * 搜索装车单
	 * @param listID
	 * @return
	 * @throws RemoteException
	 */
	public EntruckListVO searchEntruckList(long listID) throws RemoteException;
	/**
	 * 生成到达单
	 * @return
	 */
	public ArrivalVO createArrival(DeliveryListVO vo);
	/**
	 * 保存到达单
	 * @param arrival
	 * @return
	 */
	public ResultMessage saveArrival(ArrivalVO arrival);
	/**
	 * 生成派件单
	 * @return
	 */
	public SendListVO createSendList(long arrivalID);
	/**
	 * 保存派件单
	 * @return
	 */
	public ResultMessage saveSendList(SendListVO sendList);
	
	/**
	 * 修改订单物流信息
	 * @return
	 * @throws RemoteException
	 */
	public ResultMessage doArrive() throws RemoteException;
}
