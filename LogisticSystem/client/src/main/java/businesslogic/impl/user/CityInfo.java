package businesslogic.impl.user;

import java.rmi.RemoteException;
import java.util.ArrayList;

import data.enums.POType;
import data.enums.StorageArea;
import data.po.CityInfoPO;
import data.po.CityTransPO;
import data.po.DataPO;
import data.po.InstitutionPO;
import data.service.CompanyDataService;

/**
 * �����û����ڳ��������Ϣ
 * 
 * @author xu
 *
 */
public class CityInfo {

	CityInfoPO city;
	
	ArrayList<InstitutionPO> halls;
	ArrayList<CityInfoPO> allCitys;
	CompanyDataService companyData;
	
	public long getHallID(String hallName){
		for(InstitutionPO h:halls){
			if(h.getName().equals(hallName))
				return h.getSerialNum();
		}
		
		return -1;
		
	}

	public CityInfo(CompanyDataService companyData,long centerID)
			throws RemoteException {
		this.companyData = companyData;
		allCitys = new ArrayList<CityInfoPO>();
		ArrayList<DataPO> citys = companyData.getPOList(POType.CITYINFO);
		for (DataPO d : citys) {
			CityInfoPO c = (CityInfoPO) d;
			allCitys.add(c);
			if (c.getTransferCenterID() == centerID) {
				city = c; break;
			}

		}

	}
	
	/**
	 * ��ȡ����ת���ĵ�Ŀ����ת���ĵ��˷�
	 * @param targetCenterName Ŀ����ת������
	 * @param transferType ���䷽ʽ
	 * @return ����
	 * @throws RemoteException
	 */
	public double getTransferFee(String targetCenterName,StorageArea transferType) throws RemoteException{
		CityTransPO cityTrans = companyData.searchByCityName(city.getName(), targetCenterName);
		if(transferType == StorageArea.PLANE) return cityTrans.getPlanePrice();
		else if(transferType == StorageArea.TRAIN) return cityTrans.getTrainPrice();
		else return cityTrans.getTrunkPrice();
	}
	
	

	/**
	 * ��ȡ����������ת���ı��
	 * @return
	 */
	public long getTransferCenterID(){
		return city.getTransferCenterID();
	}
	
	/**
	 * ��ȡ��������������������
	 * @return
	 */
	public ArrayList<String> getOtherCitys(){
		ArrayList<String> center = new ArrayList<String>();
		for(CityInfoPO c: allCitys){
			if(c.getName() == city.getName()) continue;
			else {
				center.add(c.getName());
			}
		}
		
		return center;
	}


	/**
	 * ��ȡ����������Ӫҵ����
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<String> getHalls() throws RemoteException{
		ArrayList<String> hall = new ArrayList<String>();
		ArrayList<Long> hallID = city.getBusinessOfficeList();
		halls = new ArrayList<InstitutionPO>();
		for(Long h: hallID){
			halls.add((InstitutionPO) companyData.search(POType.INSTITUTION, h));
		}
		for(InstitutionPO ins: halls){
			hall.add(ins.getName());
		}
		
		return hall;
	}

	
	/**
	 * ���ݻ�������ȡ�������
	 * @param name
	 * @return
	 */
	public long getDestID(String name){
		for(CityInfoPO c: allCitys){
			if(name.equals(c.getName()))
				return c.getTransferCenterID();
		}
		
		for(InstitutionPO hall: halls){
			if(name.equals(hall.getName()))
				return hall.getSerialNum();
		}
		
		return -1;
	}

}