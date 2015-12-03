package businesslogic.service.Transfer.center;

import data.enums.POType;
import data.message.ResultMessage;
import data.vo.ArrivalListVO;
import data.vo.ArrivalVO;
import data.vo.DeliveryListVO;
import data.vo.EntruckListVO;
import data.vo.TransferListVO;

public interface TransferReceiveService {
	
	public ArrivalListVO getCheckedArrivalList();
	
	public ArrivalVO chooseArrival(long listID);
	
	public TransferListVO getTransferList(long code);

	public EntruckListVO getEntruckList(long code);

	public ArrivalVO createArriveList(POType type,DeliveryListVO deliveryList);

	public ResultMessage saveArriveList(ArrivalVO arrival);
}