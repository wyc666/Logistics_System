package data.impl;

import data.enums.POType;
import data.po.*;
import data.service.CompanyDataService;
import utils.Log;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Mouse on 2015/11/4 0004.
 */
public class CompanyDataSerializableImpl extends UnicastRemoteObject implements CompanyDataService {

    HashMap<POType, ArrayList<DataPO>> poLists = new HashMap<>();
    ArrayList<DataPO> newlyApproved = new ArrayList<>();
    protected static ArrayList<DataPO> unapproved = new ArrayList<>();

    public CompanyDataSerializableImpl() throws RemoteException {
        super();
        poLists.put(POType.SALARY, new ArrayList<DataPO>());
        poLists.put(POType.STAFF, new ArrayList<DataPO>());
        poLists.put(POType.INSTITUTION, new ArrayList<DataPO>());
        poLists.put(POType.CITYINFO, new ArrayList<DataPO>());
        poLists.put(POType.CITYTRANS, new ArrayList<DataPO>());
        init();
    }


    public ArrayList<DataPO> getPOList(POType type) {
        if (!poLists.containsKey(type)) return null;
        return poLists.get(type);
    }

    @Override
    public DataPO searchCity(String cityName) {
        Log.log("调用" + this.getClass().getSimpleName());
        for (DataPO city: poLists.get(POType.CITYINFO)) {
            if (((CityInfoPO)city).getName().equals(cityName)) {
                return city;
            }
        }
        return null;
    }

    public long searchBusinessOffice(String boName) throws RemoteException {
        Log.log("调用" + this.getClass().getSimpleName());
        for (DataPO institution: poLists.get(POType.INSTITUTION)) {
            if (((InstitutionPO)institution).getName().equals(boName)) {
                return institution.getSerialNum();
            }
        }
        return 0;
    }


    @Override
    public SalaryPO searchByInstitution(String institution) throws RemoteException {
        Log.log("调用" + this.getClass().getSimpleName());
        SalaryPO salaryPO;
        for (DataPO data: poLists.get(POType.SALARY)) {
            salaryPO = (SalaryPO) data;
            if (salaryPO.getInstitution().equals(institution)) return salaryPO;
        }
        return null;
    }

    @Override
    public CityTransPO searchByCityName(String fromCity, String toCity) throws RemoteException {
        Log.log("调用" + this.getClass().getSimpleName());
        CityTransPO cityTransPO;
        for (DataPO dataPO: poLists.get(POType.CITYTRANS)) {
            cityTransPO = (CityTransPO) dataPO;
            if(cityTransPO.getFromCity().equals(fromCity) && cityTransPO.getToCity().equals(toCity))
                return cityTransPO;
        }
        return null;
    }

    @Override
    public ArrayList<DataPO> getUnapprovedPO(POType type) {
        Log.log("调用" + this.getClass().getSimpleName());
        ArrayList<DataPO> result = new ArrayList<>();
        for (DataPO data: unapproved) {
            if (data.getPOType() == type) {
                result.add(data);
            }
        }
        return result;
    }

    @Override
    public HashMap<POType, ArrayList<DataPO>> hkasfkjhkjash() throws RemoteException {
        return poLists;
    }

    @Override
    public ArrayList<DataPO> asdfghjkl() throws RemoteException {
        return newlyApproved;
    }


	@Override
	public ArrayList<StaffPO> searchSenders(long institutionID) {
		ArrayList<StaffPO> senders = new ArrayList<StaffPO>();
		ArrayList<DataPO> all = getPOList(POType.STAFF);
		for (int i = 0; i < all.size(); i++) {
			StaffPO s = (StaffPO) all.get(i);
			if (s.getInstitution() == institutionID) {
				senders.add(s);
			}
		}
		return senders;
	}
}
