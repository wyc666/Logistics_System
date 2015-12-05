import businesslogic.impl.user.CityInfo;
import data.enums.DataType;
import data.factory.DataServiceFactory;
import data.po.CityInfoPO;
import data.po.InstitutionPO;
import data.service.CompanyDataService;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by mist on 2015/12/5 0005.
 */
public class DataInitialization {
    public static void main(String[] args) {
        CityInfoPO[] city = new CityInfoPO[4];
        city[0] = new CityInfoPO(025);
        city[0].setName("�Ͼ�");
        city[0].addBusinessOffice(1002500001);
        city[0].addBusinessOffice(1002500002);
        city[0].addBusinessOffice(1002500003);
        city[0].addBusinessOffice(1002500004);
        city[0].setTransferCenter(10025);
        city[1] = new CityInfoPO(021);
        city[1].setName("�Ϻ�");
        city[1].addBusinessOffice(1002100001);
        city[1].addBusinessOffice(1002100002);
        city[1].addBusinessOffice(1002100003);
        city[1].addBusinessOffice(1002100004);
        city[1].setTransferCenter(10021);
        city[2] = new CityInfoPO(010);
        city[2].setName("����");
        city[2].addBusinessOffice(1001000001);
        city[2].addBusinessOffice(1001000002);
        city[2].addBusinessOffice(1001000003);
        city[2].addBusinessOffice(1001000004);
        city[2].setTransferCenter(10021);
        city[3] = new CityInfoPO(020);
        city[3].setName("����");
        city[3].addBusinessOffice(1002000001);
        city[3].addBusinessOffice(1002000002);
        city[3].addBusinessOffice(1002000003);
        city[3].addBusinessOffice(1002000004);
        city[3].setTransferCenter(10020);

        InstitutionPO[] institution = new InstitutionPO[20];

        // �Ͼ�
        institution[0] = new InstitutionPO(1002500001);
        institution[0].setName("��¥��");
        institution[0].setTargetCenter(10025);
        institution[1] = new InstitutionPO(1002500002);
        institution[1].setName("��ϼ��");
        institution[1].setTargetCenter(10025);
        institution[2] = new InstitutionPO(1002500003);
        institution[2].setName("������");
        institution[2].setTargetCenter(10025);
        institution[3] = new InstitutionPO(1002500004);
        institution[3].setName("�ػ���");
        institution[3].setTargetCenter(10025);
        institution[4] = new InstitutionPO(10025);
        institution[4].setName("�Ͼ���");

        // ����
        institution[5] = new InstitutionPO(1001000001);
        institution[5].setName("������");
        institution[5].setTargetCenter(10010);
        institution[6] = new InstitutionPO(1001000002);
        institution[6].setName("������");
        institution[6].setTargetCenter(10010);
        institution[7] = new InstitutionPO(1001000003);
        institution[7].setName("������");
        institution[7].setTargetCenter(10010);
        institution[8] = new InstitutionPO(1001000004);
        institution[8].setName("������");
        institution[8].setTargetCenter(10010);
        institution[9] = new InstitutionPO(10010);
        institution[9].setName("������");

        // �Ϻ�
        institution[10] = new InstitutionPO(1002100001);
        institution[10].setName("������");
        institution[10].setTargetCenter(10021);
        institution[11] = new InstitutionPO(1002100002);
        institution[11].setName("������");
        institution[11].setTargetCenter(10021);
        institution[12] = new InstitutionPO(1002100003);
        institution[12].setName("������");
        institution[12].setTargetCenter(10021);
        institution[13] = new InstitutionPO(1002100004);
        institution[13].setName("�ζ���");
        institution[13].setTargetCenter(10021);
        institution[14] = new InstitutionPO(10021);
        institution[14].setName("�Ϻ���");

        // ����
        institution[15] = new InstitutionPO(1002000001);
        institution[15].setName("������");
        institution[15].setTargetCenter(10020);
        institution[16] = new InstitutionPO(1002000002);
        institution[16].setName("�ܸ���");
        institution[16].setTargetCenter(10020);
        institution[17] = new InstitutionPO(1002000003);
        institution[17].setName("�����");
        institution[17].setTargetCenter(10020);
        institution[18] = new InstitutionPO(1002000004);
        institution[18].setName("������");
        institution[18].setTargetCenter(10020);
        institution[19] = new InstitutionPO(10020);
        institution[19].setName("������");

        CompanyDataService companyDataService = (CompanyDataService) DataServiceFactory.getDataServiceByType(DataType.CompanyDataService);

        try {
            for (CityInfoPO cityInfoPO: city) {
                companyDataService.add(cityInfoPO);
            }
            for (InstitutionPO institutionPO: institution) {
                companyDataService.add(institutionPO);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}