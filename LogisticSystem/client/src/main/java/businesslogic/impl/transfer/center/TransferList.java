package businesslogic.impl.transfer.center;

import java.rmi.RemoteException;
import java.util.ArrayList;

import data.enums.POType;
import data.po.DataPO;
import data.po.TransferListPO;
import data.service.TransferDataService;
import data.vo.TransferListVO;

public class TransferList {
	TransferListPO transfer;
	TransferDataService transferData;
	ArrayList<DataPO> transferList;
	
	
	public TransferListVO searchTransferList(long listID) throws RemoteException{
		transfer =  (TransferListPO) transferData.search(POType.TRANSFERLIST, listID);
		return new TransferListVO(transfer);
	}
	
	public TransferListPO getTransferList(){
		return transfer;
	}
	
	public TransferListVO getCheckedTransferList(long listID){
		TransferListPO transfer = null;
		for(DataPO d : transferList){
			if(d.getSerialNum() == listID){
				transfer = (TransferListPO) d;
				return new TransferListVO(transfer);
			}
		}
		
		return null;
	}
	
	
	public String[][] getBriefTranserList(long center) throws RemoteException{
		transferList = transferData.searchCheckedList(POType.TRANSFERLIST, center);
		String[][] transferInfo = new String[2][transferList.size()];
		for(int i = 0;i < transferList.size();i++){
			TransferListPO transfer = (TransferListPO) transferList.get(i);
			String[] in ={transfer.getSerialNum()+"",transfer.getDate()};
			transferInfo[i] = in;
		}	
		return transferInfo;
	}
	
	public TransferList(TransferDataService transferData){
		this.transferData = transferData;
	}
}