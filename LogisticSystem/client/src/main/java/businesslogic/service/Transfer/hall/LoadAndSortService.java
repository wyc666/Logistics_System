package businesslogic.service.Transfer.hall;

import java.rmi.RemoteException;
import java.util.ArrayList;

import data.message.ResultMessage;
import data.vo.BriefEntruckListVO;
import data.vo.BriefOrderVO;
import data.vo.EntruckListVO;

/**
 * ��ݷּ��װ��
 * @author xu
 *
 */
public interface LoadAndSortService {
	
	
	public ResultMessage doEntruck();
	
	
	public EntruckListVO chooseEntruckList(long id);
	/**
	 * �����������װ����
	 * @return
	 * @throws RemoteException 
	 */
	public BriefEntruckListVO getEntruckList() throws RemoteException;

	
	
	/**
	 * ���Ŀ����������б�����������combobox
	 * @return
	 * @throws RemoteException
	 */
	public String[]  getDestination() throws RemoteException;
	
	/**
	 * ���ݻ������ּ��ݣ���ȡ�Դ˻���ΪĿ�ĵصĶ����б�
	 * @param des ������
	 * @return
	 */
	public BriefOrderVO chooseDestination(String des);
	
	/**
	 * �Զ�����װ����
	 * @param orders
	 * @return
	 */
	public EntruckListVO createEntruckList(String[][] orders);
	
	/**
	 * ����װ����
	 * @param entruckList
	 * @return
	 * @throws RemoteException 
	 */
	public ResultMessage saveEntruckList(EntruckListVO entruckList) throws RemoteException;
	
}