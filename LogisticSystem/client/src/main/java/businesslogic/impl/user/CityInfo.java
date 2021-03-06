package businesslogic.impl.user;

import java.rmi.RemoteException;
import java.util.ArrayList;

import data.enums.DataType;
import data.enums.POType;
import data.enums.StorageArea;
import utils.DataServiceFactory;
import data.po.CityInfoPO;
import data.po.CityTransPO;
import data.po.DataPO;
import data.po.InstitutionPO;
import data.service.CompanyDataService;

/**
 * 保存用户所在城市相关信息
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

	public CityInfo(long centerID)
			throws RemoteException {
		this.companyData = (CompanyDataService) DataServiceFactory.getDataServiceByType(DataType.CompanyDataService);
		allCitys = new ArrayList<CityInfoPO>();
		ArrayList<DataPO> citys = companyData.getPOList(POType.CITYINFO);
		for (DataPO d : citys) {
			CityInfoPO c = (CityInfoPO) d;
			allCitys.add(c);
			if (c.getTransferCenterID() == centerID) {
				city = c;
			}

		}

	}
	
	/**
	 * 获取本中转中心到目标中转中心的运费
	 * @param targetCenterName 目标中转中心名
	 * @param transferType 运输方式
	 * @return 费用
	 * @throws RemoteException
	 */
	public double getTransferFee(String targetCenterName,StorageArea transferType) throws RemoteException{
		CityTransPO cityTrans = companyData.searchByCityName(city.getName(), targetCenterName);
		if(cityTrans != null){
		if(transferType == StorageArea.PLANE) return cityTrans.getPlanePrice();
		else if(transferType == StorageArea.TRAIN) return cityTrans.getTrainPrice();
		else return cityTrans.getTrunkPrice();
		}else{
			return 0;
		}
	}
	
	

	/**
	 * 获取本城市下中转中心编号
	 * @return
	 */
	public long getTransferCenterID(){
		return city.getTransferCenterID();
	}
	
	/**
	 * 获取除本城市外其他城市名
	 * @return
	 */
	public ArrayList<String> getOtherCitys(){
		ArrayList<String> center = new ArrayList<String>();
		for(CityInfoPO c: allCitys){
			if(c.getName().equals(city.getName())) continue;
			else {
				center.add(c.getName());
			}
		}
		
		return center;
	}


	/**
	 * 获取本城市所有营业厅名
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
	 * 根据机构名获取机构编号
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
