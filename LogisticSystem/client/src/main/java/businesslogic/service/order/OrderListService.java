package businesslogic.service.order;

import data.po.OrderPO;

import java.util.ArrayList;

/**
 * Created by mist on 2015/12/11 0011.
 */
public interface OrderListService {
	public ArrayList<OrderPO> getFreshOrder(long institution) ;
	
    ArrayList<OrderPO> search(long[] order);
}