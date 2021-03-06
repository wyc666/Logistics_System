package businesslogic.impl.transfer.hall;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import data.enums.DataType;
import data.message.LoginMessage;
import utils.DataServiceFactory;
import data.message.ResultMessage;
import data.po.DataPO;
import data.po.OrderPO;
import data.service.OrderDataService;
import data.vo.BriefEntruckListVO;
import data.vo.BriefOrderVO;
import data.vo.EntruckListVO;
import businesslogic.impl.order.OrderList;
import businesslogic.impl.user.CityInfo;
import businesslogic.impl.user.InstitutionInfo;
import businesslogic.service.Transfer.hall.LoadAndSortService;
import businesslogic.service.order.OrderListService;

public class OrderSort implements LoadAndSortService {
	DriverManagement drivers;
	TruckManagement trucks;
	InstitutionInfo user; // 构造时传入
	CityInfo city;// getDestination时new,城市相关服务
	EntruckList entruckList;//装车单相关服务
	String desName;//目的地名
	OrderListService orderData;//z订单相关服务
	long desID;//目的地ID

	@Override
	public ResultMessage doEntruck() throws RemoteException {
		long[] order = entruckList.getOrder();
		System.out.println("orderlist lenth:"+order.length);
		// 修改订单物流信息
		orderData.modifyOrder(order, "已从"+user.getInstitutionName()+"营业厅"+"发出");//更新物流信息
		return ResultMessage.SUCCESS;
	}

	@Override
	public BriefEntruckListVO getEntruckList() throws RemoteException {
		return entruckList.getEntruckList(user.getInstitutionID());

	}

	@Override
	public EntruckListVO chooseEntruckList(long id) {
		return entruckList.chooseEntruckList(id);
	}

	@Override
	public String[] getDestination() throws RemoteException {
		try {
			city = new CityInfo(user.getCenterID());

		} catch (RemoteException e) {
			System.out.println("未能获取城市信息");
			e.printStackTrace();
			return null;
		}

		
		ArrayList<String> halls = city.getHalls();//获取本城市下辖所有营业厅（包括自己）
		halls.add(user.getCenterName());// 增加本营业厅目标中转中心
		for (int i = 0; i < halls.size(); i++) {
			if (halls.get(i).equals(user.getInstitutionName())) {
				halls.remove(i);// 删掉本营业厅
				break;
			}
		}
		String[] h = new String[halls.size()];
		halls.toArray(h);//转成数组
		return h;
	}

	@Override
	public BriefOrderVO chooseDestination(String des) {
		desName = des;
		desID = city.getHallID(des);
		if (desID == -1) {
			desID = user.getCenterID();
		}
		return orderData.getFreshOrder(user.getInstitutionID(),desID);//根据营业厅编号搜索新订单
	}

	@Override
	//生成装车单
	public EntruckListVO createEntruckList(String[][] orders) throws RemoteException {
		drivers= null;
		trucks = null;
		try {
			drivers = new DriverManagement(user);
			trucks = new TruckManagement(user);
		} catch (RemoteException e) {
			System.err.println("网络连接中断");
			e.printStackTrace();
			return null;
		}
		String s = drivers.getAvailableDriver();
		if (s == null) {
			return null;
		}
		String[] driverNameAndID = s.split("-");
		if (driverNameAndID == null) {
			return null;
		}
		String driverID = driverNameAndID[0];
		String driverName = driverNameAndID[1];
		long vehicle = trucks.getAvailableTruck();
		if (vehicle == -1) {
			return null;
		}else {
			String vehicleID = vehicle+"";
			return entruckList.createEntruckList(orders, user, desName, desID+"",
					driverID, driverName, vehicleID);
		}
		
	}

	@Override
	//保存装车单
	public ResultMessage saveEntruckList(EntruckListVO entruckList)
			throws RemoteException {
		String[][] info = entruckList.info;
		long[] order = new long[info.length];
		for(int i = 0 ; i < info.length;i++){
			long id = Long.parseLong(info[i][0]);
			order[i] = id;
		}
		drivers.useDriver(Long.parseLong(entruckList.driverID));
		trucks.useTruck(Long.parseLong(entruckList.vehicleID));
		orderData.modifyOrderPosition(order);//修改订单位置信息
		return this.entruckList.saveEntruckList(entruckList);

	}
	
	public OrderSort(InstitutionInfo user) {
		entruckList = new EntruckList();
		orderData = new OrderList(new LoginMessage(ResultMessage.SUCCESS));
		this.user = user;
	}


}
