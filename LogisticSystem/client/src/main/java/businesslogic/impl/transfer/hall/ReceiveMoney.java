package businesslogic.impl.transfer.hall;

import java.util.ArrayList;

import utils.Timestamper;
import data.message.ResultMessage;
import data.po.AccountPO;
import data.po.OrderPO;
import data.po.ReceiptPO;
import data.po.StaffPO;
import data.service.CompanyDataService;
import data.service.FinancialDataService;
import data.vo.ReceiptVO;

/**
 * 快递员结算
 * 
 * 未完成！！！
 * 
 * @author xu
 *
 */
public class ReceiveMoney {

	


	ArrayList<StaffPO> senders;
	StaffPO sender;
	ReceiptVO receiptVO;
	ReceiptPO receipt;
	AccountPO account;
	CompanyDataService companyData;
	FinancialDataService financialDataService;


	public String[] getSenders(long hall){
		return null;
	
	}
	public ReceiptVO chooseSender(String name){
		for(StaffPO s:senders){
			if(s.getName().equals(name))
				sender = s;
		}
		
		ArrayList<OrderPO> orders = new ArrayList<>();
		long[] orderNum = new long[orders.size()];
		double all = 0;
		for(int i = 0 ; i < orders.size();i++){
			OrderPO o = orders.get(i);
			orderNum[i] = o.getSerialNum();
			all += o.getFee();
		}
		receipt = new ReceiptPO();
		receipt.setSender(sender.getName());
		receipt.setMoney(account.getMoney()+all);
		ReceiptVO rec = new ReceiptVO();
		rec.setAddress(receipt.getInstitution());
		rec.setDate(receipt.getDate());
		rec.setInstitution(receipt.getInstitution());
		rec.setMoney(receipt.getMoney());
		rec.setSender(receipt.getSender());
		rec.setPeople(receipt.getSender());
		return rec;

	}
	
	public ResultMessage receiveMoney(){
		return ResultMessage.SUCCESS;
	}
	
	public ReceiveMoney(long bank){
	//	companyData = (CompanyDataService) DataServiceFactory.getDataServiceByType(DataType.CompanyDataService);
	//	financialDataService = (FinancialDataService) DataServiceFactory.getDataServiceByType(DataType.FinancialDataService);
	//	account = (AccountPO) financialDataService.search(POType.ACCOUNT, bank);
	}
}
