package data.impl;

import data.enums.POType;
import data.po.DataPO;
import data.po.OrderPO;
import data.po.SignPO;
import data.service.OrderDataService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * OrderDataServiceʵ��
 * Created by mist on 2015/11/10 0010.
 */
public class OrderDataSerializableImpl extends UnicastRemoteObject implements OrderDataService {
    HashMap<POType, ArrayList<DataPO>> poLists = new HashMap<>();
    ArrayList<DataPO> newlyApproved = new ArrayList<>();

    public OrderDataSerializableImpl() throws RemoteException {
        super();
        poLists.put(POType.ORDER, new ArrayList<DataPO>());
        poLists.put(POType.LOGISTICINFO, new ArrayList<DataPO>());
        init();
    }


    /**
     * key���������֣���һ���ֱ�ʾ�����ص�ַ���ڶ����ֱ�ʾ����ص�ַ���ÿո������������������
     * ����Գ����ػ��ߵ���ز���Ҫ������һ������Ϊ�ַ���*����
     * ���ط���Ҫ���PO��ɵ�POList
     */
    public ArrayList<DataPO> searchByLoc(String key) throws RemoteException {
        String locs[] = key.split("\\-");
        ArrayList<DataPO> result = new ArrayList<>();
        for (DataPO dataPO : poLists.get(POType.ORDER)) {
            OrderPO order = (OrderPO) dataPO;
            boolean addable = true;
            if (!locs[1].equals("*") && !locs[1].equals(order.getSInfo(OrderPO.SADDRESS).substring(0, 3)))
                addable = false;
            if (!locs[2].equals("*") && !locs[2].equals(order.getSInfo(OrderPO.SADDRESS).substring(0, 3)))
                addable = false;
            if (addable) result.add(order);
        }
        return result;
    }

    @Override
    public DataPO searchSignInfo(long ordersn) throws RemoteException {
        for (DataPO dataPO : poLists.get(POType.SIGN)) {
            SignPO signPO = (SignPO) dataPO;
            if (signPO.getOrder() == ordersn) return signPO;
        }
        return null;
    }

    public ArrayList<DataPO> getPOList(POType type) throws RemoteException {
        if (!poLists.containsKey(type)) return null;
        else return poLists.get(type);
    }

    @Override
    public HashMap<POType, ArrayList<DataPO>> getHashMap() throws RemoteException {
        return poLists;
    }

    @Override
    public ArrayList<DataPO> getNewlyApproved() throws RemoteException {
        return newlyApproved;
    }

}