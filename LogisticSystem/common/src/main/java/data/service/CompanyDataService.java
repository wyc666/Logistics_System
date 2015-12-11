package data.service;

import data.enums.POType;
import data.message.ResultMessage;
import data.po.CityInfoPO;
import data.po.CityTransPO;
import data.po.DataPO;
import data.po.SalaryPO;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * 公司相关数据接口。
 *
 * @author Mouse
 */
public interface CompanyDataService extends DataService {

    DataPO searchCity(String cityName) throws RemoteException;

    /**
     * 根据机构名称查找工资信息
     * @param insititution 机构名称
     * @return  该机构SalaryPO的引用
     * @throws RemoteException
     */
    SalaryPO searchByInstitution(String insititution) throws RemoteException;

    /**
     * 根据城市名字查找两城市间物流信息
     * @param fromCity  起始城市名字
     * @param toCity  结束城市名字
     * @return  两城市之间的CityTransInfoPO的引用
     * @throws RemoteException
     */
    CityTransPO searchByCityName(String fromCity, String toCity) throws RemoteException;
    
    long searchBusinessOffice(String boName) throws RemoteException;
}
