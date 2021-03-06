package businesslogic.service.Transfer.center;

import java.rmi.RemoteException;

import data.enums.POType;
import data.message.ResultMessage;
import data.vo.ArrivalListVO;
import data.vo.ArrivalVO;
import data.vo.DeliveryListVO;
import data.vo.EntruckListVO;
import data.vo.TransferListVO;
/**
 * 中转接收相关服务
 * @author xu
 *
 */
public interface TransferReceiveService {
	public ResultMessage modifyTransferType(ArrivalVO a) throws RemoteException;
	
	public ResultMessage doArrive() throws RemoteException;
	/**
	 * 获取所有审批过的到达单
	 * @return 到达单列表
	 */
	public ArrivalListVO getCheckedArrivalList();
	
	/**
	 * 根据单号获取到达单详细
	 * @param listID 到达单号
	 * @return 到达单详细信息
	 */
	public ArrivalVO chooseArrival(long listID);
	
	/**
	 * 获取中转单信息
	 * @param code 中转单号
	 * @return 中转单显示信息
	 */
	public TransferListVO getTransferList(long code);

	/**
	 * 获取装车单
	 * @param code 装车单号
	 * @return 装车单详细信息
	 */
	public EntruckListVO getEntruckList(long code);

	/**
	 * 生成到达单
	 * @param type 中转单？装车单？
	 * @param deliveryList
	 * @return 到达单详细信息
	 */
	public ArrivalVO createArriveList(POType type, DeliveryListVO deliveryList);

	/**
	 * 保存到达单
	 * @param arrival 到达单详细信息
	 * @return 保存结果
	 */
	public ResultMessage saveArriveList(ArrivalVO arrival);
}
