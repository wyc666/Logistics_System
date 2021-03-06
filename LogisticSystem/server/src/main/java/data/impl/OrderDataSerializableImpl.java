package data.impl;

import data.enums.POType;
import data.po.DataPO;
import data.po.OrderPO;
import data.po.SignPO;
import data.service.OrderDataService;
import utils.Log;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * OrderDataService实现
 * Created by mist on 2015/11/10 0010.
 */
public class OrderDataSerializableImpl extends UnicastRemoteObject implements OrderDataService {
    HashMap<POType, ArrayList<DataPO>> poLists = new HashMap<>();
    ArrayList<DataPO> newlyApproved = new ArrayList<>();

    public OrderDataSerializableImpl() throws RemoteException {
        super();
        poLists.put(POType.ORDER, new ArrayList<DataPO>());
        poLists.put(POType.LOGISTICINFO, new ArrayList<DataPO>());
        poLists.put(POType.SIGN, new ArrayList<DataPO>());
        init();
    }


    public ArrayList<DataPO> searchByLoc(String key) throws RemoteException {
        Log.log("调用" + this.getClass().getSimpleName());
        String locs[] = key.split("[ ]");
        ArrayList<DataPO> result = new ArrayList<>();
        for (DataPO dataPO : poLists.get(POType.ORDER)) {
            OrderPO order = (OrderPO) dataPO;
            boolean addable = true;
            if (!locs[0].equals("*")) {
                if (!locs[0].split("[-]")[0].equals(order.getSInfo(OrderPO.SADDRESS).split("[-]")[0]) ||
                    !locs[0].split("[-]")[1].equals(order.getSInfo(OrderPO.SADDRESS).split("[-]")[1]))
                addable = false;
            }
            if (!locs[1].equals("*"))
                if (!locs[1].split("[-]")[0].equals(order.getSInfo(OrderPO.RADDRESS).split("[-]")[0]) ||
                    !locs[1].split("[-]")[1].equals(order.getSInfo(OrderPO.RADDRESS).split("[-]")[1]))
                addable = false;
            if (addable) result.add(order);
        }
        return result;
    }

    @Override
    public DataPO searchSignInfo(long ordersn) throws RemoteException {
        Log.log("调用" + this.getClass().getSimpleName());
        for (DataPO dataPO : poLists.get(POType.SIGN)) {
            SignPO signPO = (SignPO) dataPO;
            if (signPO.getOrder() == ordersn) return signPO;
        }
        return null;
    }

    @Override
    public ArrayList<DataPO> searchByCourier(long sn, String date) {
        Log.log("调用" + this.getClass().getSimpleName());
        ArrayList<DataPO> result = new ArrayList<>();
        for (DataPO dataPO: poLists.get(POType.ORDER)) {
            if (((OrderPO) dataPO).getCourier() == sn) {
                result.add(dataPO);
            }
        }
        return result;
    }

    public ArrayList<DataPO> getPOList(POType type) throws RemoteException {
        if (!poLists.containsKey(type)) return null;
        else return poLists.get(type);
    }

    @Override
    public HashMap<POType, ArrayList<DataPO>> hkasfkjhkjash() throws RemoteException {
        return poLists;
    }

    @Override
    public ArrayList<DataPO> asdfghjkl() throws RemoteException {
        return newlyApproved;
    }

}
