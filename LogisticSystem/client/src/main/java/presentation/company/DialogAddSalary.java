package presentation.company;

import businesslogic.impl.company.CompanyBLController;
import data.message.ResultMessage;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * ����Ա������ʱ����ʱ����
 */
public class DialogAddSalary extends JDialog{
	JDialog jdialog = null;
	String  [] institutions = null;
	JComboBox<String> institution = null;
	JComboBox<String> type = null;
	String [] types = null;
	JTextField salary = null;
	JButton finish = null;
	JLabel labelInstituion = null;
	JLabel labelSalary = null;
	JLabel labelType = null;
    companyManage company = null;
	String stringSalary,stringInstitution,stringType;
	ResultMessage resultMessage = null;
	CompanyBLController controller = null;
	public DialogAddSalary(companyManage company){
		controller = new CompanyBLController();
		this.company = company;
		jdialog = new JDialog(company,"���ӹ���");
		institutions = new String[]{"���Ա","������Ա","Ӫҵ��ҵ��Ա","��ת����ҵ��Ա","�ֿ����Ա","������ʻԱ"};
		institution = new JComboBox<String>(institutions);
		institution.setBounds(65, 35, 100, 30);
		types = new String[]{"�ƴ�","�¸�"};
		type = new JComboBox<String>(types);
		type.setBounds(330, 35, 55, 30);
		salary = new JTextField();
		salary.setBounds(205, 35, 60, 30);
		finish = new JButton("ȷ��");
		finish.setBounds(175, 85, 66, 30);
		labelInstituion = new JLabel("����ѡ��");
		labelInstituion.setBounds(5, 25, 60, 50);
		labelSalary = new JLabel("����");
		labelSalary.setBounds(175, 25, 60, 50);
		labelType = new JLabel("���㷽ʽ");
		labelType.setBounds(270, 25, 60, 50);
		// �����пؼ����ӵ�JDialog��
		jdialog.setSize(410, 200);
		jdialog.add(labelInstituion);
		jdialog.add(institution);
		jdialog.add(labelSalary);
		jdialog.add(salary);
		jdialog.add(labelType);
		jdialog.add(type);
		jdialog.add(finish);
		jdialog.setModal(true);
		jdialog.setLocationRelativeTo(null);
		jdialog.setLayout(null);
		jdialog.setResizable(false);
		finish.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				buttonEnsure(e);
			}
		});
		jdialog.setVisible(true);	
	}
	
	private void buttonEnsure(MouseEvent e){
		stringSalary = salary.getText();
		stringInstitution = (String) institution.getSelectedItem();
		stringType = (String) type.getSelectedItem();
		resultMessage = controller.addSalary(stringInstitution,stringSalary,stringType);
		//����resultMessage���ͶԽ���������
		if(resultMessage== ResultMessage.SUCCESS){
			company.labelSalarySuccess.setText("���ӳɹ�!");
			company.initSalaryTable();
		}
		else if(resultMessage== ResultMessage.EXIST){
            company.labelSalarySuccess.setText("");
			JOptionPane.showMessageDialog(null,"�ù��������Ѵ���,�����ظ�����","",JComponent.ERROR);
		}
		else if(resultMessage== ResultMessage.NOTCONNECTED){
			company.labelSalarySuccess.setText("");
			JOptionPane.showMessageDialog(null,"�������...","",JComponent.ERROR);
		}
		jdialog.dispose();
	}
}