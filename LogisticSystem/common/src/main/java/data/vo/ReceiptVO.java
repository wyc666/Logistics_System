package data.vo;

/**
 * Created by apple on 2015/11/15.
 */

public class ReceiptVO {
	long id;
	long senderID;
	long hallID;
	double money;
	String date;
	String people;
	String institution;
	String address;
	boolean isCount;
	String sender;

	public boolean isCount() {
		return isCount;
	}

	

	public long getSenderID() {
		return senderID;
	}

	public void setSenderID(long senderID) {
		this.senderID = senderID;
	}

	public long getHallID() {
		return hallID;
	}

	public void setHallID(long hallID) {
		this.hallID = hallID;
	}
	public void setCount(boolean isCount) {
		this.isCount = isCount;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ReceiptVO() {
		// TODO Auto-generated constructor stub
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}