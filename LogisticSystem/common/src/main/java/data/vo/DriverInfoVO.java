package data.vo;

import data.po.DriverInfoPO;

/**
 * 
 * @author xu
 *
 */
public class DriverInfoVO {
	public String[] statusArray = {"空闲","货运"};
	public String driverID;//（城市编号（电话号码区号南京025）+营业厅编号（000三位数字）+000三位数字
    public String IDCard;
    public String name;
    public String engaged;
    public String gender ;
    public String phoneNum;
    public String bornDate /* yyyy/mm/dd */;
    public String timeLimit /* yyyy/mm/dd */;
    public String bornY;
    public String bornM;
    public String bornD;
    public String licenseY;
    public String licenseM;
    public String licenseD;
    
    public String busy(){
    	return statusArray[0];
    }
    
    public String idle(){
    	return statusArray[1];
    }
	public DriverInfoVO(DriverInfoPO po){
		IDCard = po.getIDCard()+"";
		name = po.getName();
		engaged = po.getEngagedString();
		gender = po.getGenderString();
		phoneNum = po.getPhoneNum();
		bornDate = po.getBornDate();
	
		String[] born = bornDate.split("/");
		bornY = born[0];
		bornM = born[1];
		bornD = born[2];
		timeLimit = po.getTimeLimit();
		String[] license = timeLimit.split("/");
		licenseY = license[0];
		licenseM = license[1];
		licenseD = license[2];
		driverID = po.getSerialNum()+"";
	}
	
	public DriverInfoVO(){
		
	}
}
