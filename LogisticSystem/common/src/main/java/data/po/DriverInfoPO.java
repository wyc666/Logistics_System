package data.po;


import data.enums.POType;
import data.vo.DriverInfoVO;

/**
 * 
 * @author xu
 *
 */
public class DriverInfoPO extends DataPO {

    private static final long serialVersionUID = 6;

   //司机编号：（城市编号（电话号码区号南京025）+营业厅编号（000三位数字）+000三位数字
    long IDCard;
    String name;
    boolean engaged;
    boolean gender /* true = female, false = male */;
    String phoneNum;
    String bornDate /* yyyy/mm/dd */;
    String timeLimit /* yyyy/mm/dd */;
    
    public String getGenderString(){
    	if(gender == true) {
    		return "女";
    	}
    	else {
			return "男";
		}
    }
    
//    public void ModifyDriver(DriverInfoVO vo){
//    	this.bornDate = vo.bornDate;
//    	if (vo.engaged.equals(vo.busy())) {
//			this.engaged = true;
//		}else{
//			this.engaged = false;
//		}
//    	if (vo.gender.equals("男")) {
//			this.gender = false;
//		}else {
//			this.gender = true;
//		}
//    	this.IDCard =Long.parseLong(vo.IDCard);
//    	this.name = vo.name;
//    	this.phoneNum = vo.phoneNum;
//    	this.timeLimit = vo.timeLimit;
//    }
    
    public DriverInfoPO(){
    	super(POType.DRIVERINFO);
    }

    public DriverInfoPO(DriverInfoVO vo){
    	super(POType.DRIVERINFO);
    	this.bornDate = vo.bornDate;
    	this.engaged = false;
    	if (vo.gender.equals("男")) {
			this.gender = false;
		}else {
			this.gender = true;
		}
    	this.IDCard =Long.parseLong(vo.IDCard);
    	this.name = vo.name;
    	this.phoneNum = vo.phoneNum;
    	this.timeLimit = vo.timeLimit;
    	
    }
    public DriverInfoPO(long ID, String name, boolean gender, String phoneNum, String bornDate, String timeLimit) {
        super(POType.DRIVERINFO);
        this.IDCard = ID;
        this.phoneNum = phoneNum;
        this.bornDate = bornDate;
        this.timeLimit = timeLimit;
        this.gender = gender;
        engaged = false;
    }
    
    public boolean getEngaged(){
    	return engaged;
    }
    
    public String getEngagedString() {
		if(engaged) return "货运";
		else return "空闲";
	}
    
    public String getName(){
    	return name;
    }

    public long getSerialNum() {
        return serialNum;
    }

    public long getIDCard() {
        return IDCard;
    }

    public void setIDCard(long IDCard) {
        this.IDCard = IDCard;
    }

    public boolean isEngaged() {
        return engaged;
    }

    public void setEngaged(boolean engaged) {
        this.engaged = engaged;
    }

    public boolean getGender() {
        return gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBornDate() {
        return bornDate;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public boolean setBornDate(String bornDate) {
        if (bornDate.matches("[0-9]{4}[/][0-9]{2}[/][0-9]{2}")) {
            this.bornDate = bornDate;
            return true;
        }
        return false;
    }

    public boolean setTimeLimit(String timeLimit) {
        if (timeLimit.matches("[0-9]{4}[/][0-9]{2}[/][0-9]{2}")) {
            this.timeLimit = timeLimit;
            return true;
        }
        return false;
    }
}