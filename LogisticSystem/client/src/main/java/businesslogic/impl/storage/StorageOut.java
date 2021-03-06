package businesslogic.impl.storage;

import java.rmi.RemoteException;

import businesslogic.impl.order.OrderList;
import businesslogic.impl.transfer.center.TransferList;
import businesslogic.impl.user.InstitutionInfo;
import businesslogic.service.storage.StorageOutService;
import data.enums.DataType;
import data.enums.POType;
import data.message.LoginMessage;
import utils.DataServiceFactory;
import utils.Timestamper;
import data.message.ResultMessage;
import data.po.StorageListPO;
import data.po.StorageOutListPO;
import data.po.TransferListPO;
import data.service.StorageDataService;
import data.service.TransferDataService;
import data.vo.BriefTransferAndStorageOutVO;
import data.vo.StorageOutVO;
import data.vo.TransferListVO;

/**
 * 出库 实现类
 * @author xu
 *
 */
public class StorageOut implements StorageOutService{
	StorageDataService storageData ;
	
	InstitutionInfo user;
	TransferList transferList;
	StorageList storageOutList;
	StorageInfo storageInfo;
	OrderList orderList;
	
	/**
	 * 对当前所有已审批出库单进行出库操作
	 * @return
	 * @throws RemoteException 
	 */
	public ResultMessage doStorageOut() throws RemoteException{
		long[] o = storageOutList.getOrderID();
		System.out.println("修改物流信息 订单数："+o.length);
	//	orderList.modifyOrder(o,"从"+user.getInstitutionName()+"中转中心发出");
	//	orderList.modifyOrderPosition(o);
		return ResultMessage.SUCCESS;
	}
	


	/**
	 * 修改仓库信息
	 * @param storageOut
	 * @return
	 * @throws RemoteException
	 */
	private ResultMessage modifyStorageInfo(StorageOutListPO storageOut) throws RemoteException{
		return storageInfo.modifyStorageInfo(storageOut);
	}
	
	/**
	 * 保存出库单，修改仓库信息
	 * @param vo
	 * @return
	 * @throws RemoteException
	 */
	public ResultMessage saveStroageOut(StorageOutVO vo){
		StorageOutListPO storageOut = new StorageOutListPO(vo);
		storageOut.setSerialNum(user.getInstitutionID()*10000+storageOut.getSerialNum());
		try {
			//modifyStorageInfo(storageOut);在中转中心装车时完成
			return storageData.add(storageOut);
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.FAILED;
		}
		
	}
	
	
	public StorageOutVO getStorageOut(long id) throws RemoteException{
		StorageListPO o =  storageOutList.getCheckedStorageList(id);
		StorageOutVO s =  new StorageOutVO((StorageOutListPO) o);
		s.setDate(Timestamper.getTimeByDate());
		return s;
	}

	/**
	 * 查看中转单
	 *
	 * @return 中转单详细信息
	 * @throws RemoteException
	 */
	public TransferListVO getTransferList(long listID){
		return transferList.getCheckedTransferList(listID);
	}
	/**
	 * 发起一次新的出库活动，显示新中转单
	 * @return 装车单，中转单列表
	 * @throws RemoteException 
	 */
	public BriefTransferAndStorageOutVO  newStorageOut() throws RemoteException {
		String[][] transferInfo;
		try {
			transferInfo = transferList.getBriefTranserList(user.getCenterID());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
		String[][] storageOutInfo = storageOutList.getBriefStorageList();

		return new BriefTransferAndStorageOutVO(transferInfo,storageOutInfo);	
	}
	
	/**
	 * 由选中的中转单生成默认出库单
	 * 
	 * @return StorageOutvo
	 */
	public StorageOutVO createStorageOutList() {
		TransferListPO transfer = transferList.getTransferList();
		return storageInfo.createStorageOutList(transfer);
	}
	
	public StorageOut(InstitutionInfo user,StorageInfo storageInfo,StorageDataService storageData) throws RemoteException{
		this.user = user;
		TransferDataService transferData = (TransferDataService) DataServiceFactory.getDataServiceByType(DataType.TransferDataService);
		this.storageData = storageData;
		transferList = new TransferList(transferData);
		orderList = new OrderList(new LoginMessage(ResultMessage.SUCCESS));
		this.storageInfo = storageInfo;
		storageOutList = new StorageList(storageData, user.getCenterID(), POType.STORAGEOUTLIST);
	}
}
