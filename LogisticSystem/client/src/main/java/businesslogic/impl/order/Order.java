package businesslogic.impl.order;

import data.enums.*;
import data.factory.DataServiceFactory;
import data.message.ResultMessage;
import data.po.*;
import data.service.*;
import data.vo.OrderVO;
import utils.Connection;
import utils.Timestamper;

import java.rmi.RemoteException;
import java.util.*;

/**
 *
 * Created by mist on 2015/11/15 0015.
 */
public class Order {

    OrderDataService orderDataService = (OrderDataService) DataServiceFactory.getDataServiceByType(DataType.OrderDataService);
    CompanyDataService companyDataService = (CompanyDataService) DataServiceFactory.getDataServiceByType(DataType.CompanyDataService);

    public ResultMessage createOrder(OrderVO order) {
        OrderPO newOrder = new OrderPO();

        // ��¡������Ϣ
        newOrder.setInfo(OrderPO.RADDRESS, order.raddress);
        newOrder.setInfo(OrderPO.RCOMPANY, order.rcompany);
        newOrder.setInfo(OrderPO.RNAME, order.rname);
        newOrder.setInfo(OrderPO.RPHONE, order.rphone);
        newOrder.setInfo(OrderPO.SADDRESS, order.saddress);
        newOrder.setInfo(OrderPO.SCOMPANY, order.scompany);
        newOrder.setInfo(OrderPO.SNAME, order.sname);
        newOrder.setInfo(OrderPO.SPHONE, order.sphone);
        newOrder.setStockNum(order.stockNum, order.stockType);
        newOrder.setVolume(order.volume);
        newOrder.setWeight(order.weight);
        newOrder.setServiceType(order.serviceType);
        newOrder.setFee(generateFee(order));
        newOrder.setDestID(getDestID(order.raddress));
        newOrder.setEvaluatedTime(evaluateTime(order));

        // ����������Ϣ
        LogisticInfoPO logisticInfoPO =  new LogisticInfoPO(newOrder.getSerialNum());
        logisticInfoPO.addInfo(Timestamper.getTimeBySecond(), "���Ա��������");

        // �ύ����
        try {
            orderDataService.add(newOrder);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ResultMessage.SUCCESS;
    }

    /**
     * ���ݵ�ַ��������Ŀ��Ӫҵ����ַ
     *
     * @param address ��ַ��Ϣ��������� [����]-[��]-[��ϸ��ַ]�Ĺ淶
     * @return Ŀ��Ӫҵ�����
     */
    public long getDestID(String address) {
        if (!Connection.connected) {
            System.err.println("��δ���ӵ����������޷�����Ŀ��Ӫҵ����ַ" + Calendar.getInstance().getTime());
            return 0;
        }
        long destID;
        String addr[] = address.split("\\-");
        CityInfoPO cityInfoPO = null;
        CompanyDataService companyDataService = (CompanyDataService) DataServiceFactory.getDataServiceByType(DataType.CompanyDataService);

        // ���ݳ�������ȷ��Ŀ�����
        try {
            cityInfoPO = (CityInfoPO) companyDataService.searchCity(addr[0]);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // ������λ��Ϣȷ��Ӫҵ�����
        if (cityInfoPO == null) return 0;
        else {
            try {
                long tmp = companyDataService.searchBusinessOffice(addr[1]);
                return tmp;
            } catch (RemoteException e) {
                System.err.println("�������(" + Connection.RMI_PREFIX + ")�����ӶϿ� -" + Calendar.getInstance().getTime());
            }
        }
        return 0;
    }

    public int evaluateTime(OrderVO orderVO) {
        ArrayList<DataPO> orders = null;
        float time = 0.0f;
        if (!Connection.connected) {
            System.err.println("��δ���ӵ����������޷�Ԥ���ʹ�ʱ��" + Calendar.getInstance().getTime());
            return 0;
        }
        try {
            orders = orderDataService.searchByLoc(orderVO.saddress.substring(0, 3) + "-" + orderVO.raddress.substring(0, 3));
        } catch (RemoteException e) {
            System.err.println("�������(" + Connection.RMI_PREFIX + ")�����ӶϿ� -" + Calendar.getInstance().getTime());
            return 0;
        }

        if (orders.size() == 0) return 0;

        for (DataPO data : orders) {
            OrderPO orderPO = (OrderPO) data;
            Calendar sendDate = orderPO.getGenDate();
            SignPO signPO = null;
            try {
                signPO = (SignPO) orderDataService.searchSignInfo(orderPO.getSerialNum());
            } catch (RemoteException e) {
                System.err.println("�������(" + Connection.RMI_PREFIX + ")�����ӶϿ� -" + Calendar.getInstance().getTime());
                return 0;
            }
            Calendar signDate = signPO.getGenDate();
            time += (signDate.getTimeInMillis() - sendDate.getTimeInMillis()) / 1000.0f / 60.0f / 60.0f / 24.0f;
        }
        time /= orders.size();
        time *= 10;

        // ��������
        if ((int) time % 10 >= 5) time = ((int) time) / 10 * 10 + 10;
        else time = ((int) time) / 10 * 10;
        return (int) time;
    }

    public double generateFee(OrderVO orderVO) {
        return 0;
    }

    /**
     * �������˱�ţ�����������Ķ������ջ����
     *
     * @param sn ���˱�ţ���ת����ţ�
     * @return ResultMessage.SUCCESS��ʾ�ջ��ɹ���NOTEXIST��ʾ����ת�������ڣ�FAILED��ʾ�ջ�ʧ��
     */
    public ResultMessage receive(long sn) {

        if (!Connection.connected) {
            System.err.println("��δ���ӵ����������޷�����ջ�����" + Calendar.getInstance().getTime());
            return ResultMessage.FAILED;
        }

        TransferDataService transferDataService = (TransferDataService) DataServiceFactory.getDataServiceByType(DataType.TransferDataService);
        if (transferDataService == null) {
            System.err.println("");
            return ResultMessage.FAILED;
        }



        // �ӳ־û������ļ��л����ת����Ϣ
        TransferListPO transferListPO = null;
        try {
            transferListPO = (TransferListPO) transferDataService.search(POType.TRANSFERLIST, sn);
        } catch (RemoteException e) {
            System.err.println("�������(" + Connection.RMI_PREFIX + ")�����ӶϿ� -" + Calendar.getInstance().getTime());
            return ResultMessage.FAILED;
        }
        if (transferListPO == null) return ResultMessage.NOTEXIST;


        ArrayList<Long> orderList = transferListPO.getOrderArray();

        // ������ת����Ϣ���ɵ��ﵥ��Ϣ
        ArrivalPO arrivalPO = new ArrivalPO();
        for (long order : orderList) {
            arrivalPO.addOrder(order);
            arrivalPO.addStckStatus(StockStatus.ROUND);
        }
        arrivalPO.setDate(Timestamper.getTimeByDate());
        arrivalPO.setTransferList(sn);
        arrivalPO.setFrom(transferListPO.getTransferCenter());

        try {
            transferDataService.add(arrivalPO);
        } catch (RemoteException e) {
            System.err.println("�������(" + Connection.RMI_PREFIX + ")�����ӶϿ� -" + Calendar.getInstance().getTime());
            return ResultMessage.FAILED;
        }
        return ResultMessage.SUCCESS;
    }

    /**
     * ǩ�ղ���
     *
     * @param sn    ��Ҫǩ�յĶ�����
     * @param name  ǩ��������
     * @param phone ǩ���ߵ绰
     * @return SUCCESS��ʾǩ�ճɹ���FAILED��ʾ�����Ѿ���ǩ�ա�NOTEXIST��ʾ�����Ų����ڡ�
     */
    public ResultMessage sign(long sn, String name, String phone) {
        LogisticInfoPO logisticInfo = null;
        OrderPO order = null;
        try {
            // ��ȡ������������Ϣ
            order = (OrderPO) orderDataService.search(POType.ORDER, sn);
            logisticInfo = (LogisticInfoPO) orderDataService.search(POType.LOGISTICINFO, sn);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (order == null) return ResultMessage.NOTEXIST;
        logisticInfo.addInfo(Timestamper.getTimeBySecond(), "����ѱ� " + name + " ǩ��");

        // ����ǩ����Ϣ
        SignPO signPO = new SignPO(sn);
        signPO.setSdate(name);
        signPO.setSphone(phone);
        try {
            orderDataService.modify(logisticInfo);
            orderDataService.add(signPO);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ResultMessage.SUCCESS;
    }

    public ArrayList<OrderPO> search(long[] order) {
        ArrayList<OrderPO> result = new ArrayList<>();
        for (long ordersn : order) {
            try {
                OrderPO orderPO = (OrderPO) orderDataService.search(POType.ORDER, ordersn);
                if (orderPO != null) result.add(orderPO);
            } catch (RemoteException e) {
                System.err.println("�������(" + Connection.RMI_PREFIX + ")�����ӶϿ� -" + Calendar.getInstance().getTime());
            }
        }
        return result;
    }
}