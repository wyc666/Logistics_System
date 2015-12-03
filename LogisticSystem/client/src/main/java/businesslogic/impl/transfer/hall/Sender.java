package businesslogic.impl.transfer.hall;

import java.rmi.RemoteException;
import java.util.ArrayList;

import data.enums.POType;
import data.po.DataPO;
import data.po.SenderPO;
import data.service.TransferDataService;
import data.vo.SenderVO;

/**
 * ���Ա��
 * @author xu
 *
 */
public class Sender {
	long hall;
	TransferDataService transferData;
	ArrayList<DataPO> senders;
	public Sender(TransferDataService transferData, long institutionID){
		this.transferData = transferData;
		this.hall = institutionID;
	}
	
	public SenderVO getAvailableSender() throws RemoteException{
		senders = transferData.searchList(POType.STAFF, hall);
		for(int i = 0 ; i < senders.size();i++){
			SenderPO sender = (SenderPO) senders.get(i);
			if(sender.isAvailable()){
				SenderVO s = new SenderVO();
				s.ID = sender.getSerialNum();
				s.name = sender.getName();
				return s;
			}
		}
		
		return null;
	}
		
	
}