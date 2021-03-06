package businesslogic.impl.transfer.center;

import java.rmi.RemoteException;

import data.enums.StorageArea;
import data.message.ResultMessage;
import data.vo.StoragePositionAndOrderID;
import data.vo.TransferListVO;

/**
 * ���ѭ����������ѭ��������ԭ��
 * @author xu
 *
 */
public interface StorageInfoService {
	public void refreshStorageInfo() throws RemoteException;
	public StoragePositionAndOrderID getOrderID(StorageArea transferType) ;
	public ResultMessage modifyStorageInfo(TransferListVO transferList) throws RemoteException;
}
