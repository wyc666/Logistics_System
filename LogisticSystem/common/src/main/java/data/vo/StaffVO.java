package data.vo;

public class StaffVO {

    public long institution;
    public String name;
    public boolean gender /* 0 - male , 1 - female */;
    public long phoneNum;
    public long id;
    public String idcardNum;

    public StaffVO() {

    }

    public long getInstitution() {
        return institution;
    }

    public void setInstitution(long institution) {
        this.institution = institution;
    }

    public long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdcardNum() {
        return idcardNum;
    }

    public void setIdcardNum(String idcardNum) {
        this.idcardNum = idcardNum;
    }
}