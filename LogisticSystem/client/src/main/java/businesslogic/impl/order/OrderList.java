package businesslogic.impl.order;

import java.rmi.RemoteException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

import businesslogic.service.order.OrderListService;
import data.enums.DataType;
import data.enums.POType;
import data.enums.StorageArea;
import data.message.LoginMessage;
import data.message.ResultMessage;
import utils.DataServiceFactory;
import utils.Timestamper;
import data.po.DataPO;
import data.po.LogisticInfoPO;
import data.po.OrderPO;
import data.service.OrderDataService;
import data.vo.BriefOrderVO;

public class OrderList implements OrderListService {
	ArrayList<OrderPO> orders;
	OrderDataService orderDataService;
	LoginMessage loginMessage = null;
	
	public ArrayList<OrderPO> getOrderList(long[] orderID) {
		ArrayList<OrderPO> order = new Order(loginMessage).search(orderID);
		return order;
	}
	
	public double getWeight(long[] orderID){
		ArrayList<OrderPO> order = new Order(loginMessage).search(orderID);
		orders = order;
		double weight = 0;
		for(int i = 0 ; i < order.size();i++){
			weight+= order.get(i).getWeight();
		}
		return weight;
	}
	
	public int getNum(){
		return orders.size();
	}
	
	public void modifyOrderTransferType(long[] id,StorageArea type) throws RemoteException{
		orders = getOrderList(id);
		for(int i = 0 ; i < orders.size();i++){
			orders.get(i).setTransferType(type);
			orderDataService.modify(orders.get(i));
			System.out.println("修改为"+type);
		}
	}
	
	public OrderList(LoginMessage loginMessage){
		orderDataService = (OrderDataService) DataServiceFactory.getDataServiceByType(DataType.OrderDataService);
		this.loginMessage = loginMessage;
	}

	public void modifyOrder(long[] orderNumL,String info) throws RemoteException {

		ArrayList<OrderPO> order = new Order(loginMessage).search(orderNumL);
		for (int i = 0; i < order.size(); i++) {
			// 修改订单物流信息
			LogisticInfoPO log = (LogisticInfoPO) orderDataService.search(POType.LOGISTICINFO, order.get(i).getSerialNum());
			System.out.println("物流信息："+log.getSerialNum());
			log.addInfo(Timestamper.getTimeBySecond(), info);
			orderDataService.modify(log);
		}
	}
	
	public void modifyOrderPosition(long[] orderID) throws RemoteException{
		orders = new Order(loginMessage).search(orderID);
			for (int i = 0;i< orders.size();i++) {
				System.out.println("modify next position");
					orders.get(i).updateRoutine();
					orderDataService.modify(orders.get(i));
			}
		
	}

	public BriefOrderVO getFreshOrder(long institution, long destID) {
		orders = new ArrayList<>();
		orderDataService = (OrderDataService) DataServiceFactory
				.getDataServiceByType(DataType.OrderDataService);
		if (orderDataService == null)
			return null;
		try {
			for (DataPO data : orderDataService.getPOList(POType.ORDER)) {
				OrderPO order = (OrderPO) data;
				if (order.isFresh()
						&& order.getPresentLocation() == institution) {
					orders.add(order);
				}
			}
			System.out.println("all order:"+ orders.size());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		for (int i = 0 ; i < orders.size();i++) {
			System.out.println(orders.get(i).getNextDestination());
			System.out.println(destID);
			if (orders.get(i).getNextDestination() != destID) {
				System.out.println(orders.get(i).getNextDestination());
				orders.remove(i);
				--i;
			}
		}

		Vector<Vector<String>> info = new Vector<Vector<String>>();
		for (int i = 0; i < orders.size(); i++) {
			Vector<String> row = new Vector<String>();
			OrderPO oo = orders.get(i);
			row.add(oo.getSerialNum() + "");
			row.add(oo.getWeight() + "");
			info.add(row);
		}
		return new BriefOrderVO(info);
	}

	@Override
	public ArrayList<OrderPO> search(long[] order) {
		ArrayList<OrderPO> result = new ArrayList<>();
		for (long sn : order) {
			OrderPO tmp = search(sn);
			if (tmp != null) {
				result.add(tmp);
			}
		}
		return result;
	}

	public OrderPO search(long sn) {
		OrderDataService orderDataService = (OrderDataService) DataServiceFactory
				.getDataServiceByType(DataType.OrderDataService);
		if (orderDataService == null)
			return null;
		OrderPO result = null;
		try {
			result = (OrderPO) orderDataService.search(POType.ORDER, sn);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String[][] getOrderAndFee(long senderID, String date) {
		getOrderByCourierAndDate(senderID, date);
		String[][] info = null;
		if (orders != null) {
			info = new String[orders.size()][2];
			for(int i = 0 ; i < orders.size();i++){
				String[] s = {orders.get(i).getSerialNum()+"",orders.get(i).getFee()+""};
				info[i] = s;
			}
		}
		return info;
	}
	private void getOrderByCourierAndDate(long courier, String date) {
		ArrayList<OrderPO> result = new ArrayList<>();
		orderDataService = (OrderDataService) DataServiceFactory.getDataServiceByType(DataType.OrderDataService);
		if (orderDataService == null) return;
		ArrayList<DataPO> orders = null;
		try {
			orders = orderDataService.getPOList(POType.ORDER);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		for (DataPO data: orders) {
			if (((OrderPO) data).getCourier() == courier && Timestamper.getTimeByDate(((OrderPO) data).getGenDate()).equals(date)) {
				result.add((OrderPO) data);
			}
		}
		this.orders = result;
	}

}
