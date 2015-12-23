/*
 * Created by JFormDesigner on Mon Nov 30 21:09:47 CST 2015
 */

package presentation.transfer.center;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import data.enums.StorageArea;
import data.message.ResultMessage;
import data.vo.TransferListVO;
import data.vo.TransferLoadVO;
import businesslogic.impl.transfer.TransferInfo;
import businesslogic.service.Transfer.center.TransferLoadService;

/**
 * @author sunxu
 */
public class TransferLoadPanel extends JPanel {
	TransferLoadService transferLoad;
	TransferLoadVO order;
	TransferListVO transferList;

	public TransferLoadPanel(TransferLoadService transferLoad) {
		this.transferLoad = transferLoad;
		initComponents();
		setTransferTypeBox();
		// 在未选择运输类型和目的地时，本面板上三个按钮均无效
		getOrderButton.setEnabled(false);
		removeOrderButton.setEnabled(false);
		createTransferButton.setEnabled(false);
		destBox.setEnabled(false);
	}

	private void setTransferTypeBox() {
		String[] type = TransferInfo.transferType;
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(
				type);
		transferTypeBox.setModel(model);
		transferTypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {// 监听，获取所选项，获取相应的目的地
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int item = transferTypeBox.getSelectedIndex();
					String s = (String) transferTypeBox.getItemAt(item);
					StorageArea type = TransferInfo.getTypeByString(s);
					setDestBox(type);
				}
			}
		});
		transferTypeBox.validate();
		transferTypeBox.updateUI();
		transferTypeBox.repaint();
	}

	private void setDestBox(StorageArea type) {// 根据所选运输类型，获取目的地
		ArrayList<String> des = transferLoad.chooseTransferType(type);
		String[] desArray = new String[des.size()];
		des.toArray(desArray);
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(
				desArray);
		destBox.setModel(model);
		destBox.validate();
		destBox.updateUI();
		destBox.repaint();
		getOrderButton.setEnabled(true);// 获取订单信息按钮生效
		destBox.setEnabled(true);// 目的地列表生效
	}

	private void setOrderList() {
		DefaultTableModel model = new DefaultTableModel(order.getOrderInfo(),
				order.header);
		orderTable.setModel(model);
		orderTable.validate();
		orderTable.updateUI();
		orderTable.repaint();
	}

	private void getOrderButtonMouseReleased(MouseEvent e) {
		sort();
	}
	
	private void sort(){
		if (getOrderButton.isEnabled()) {
			int item = destBox.getSelectedIndex();
			if (item >= 0) {
				String desName = (String) destBox.getItemAt(item);
				System.out.println(desName);
				order = transferLoad.getOrder(desName);
				if (order != null) {
					setOrderList();
					removeOrderButton.setEnabled(true);// 移除按钮生效
					createTransferButton.setEnabled(true);// 生成中转单按钮生效
				} else {
					JOptionPane.showMessageDialog(null, "未能正确获取仓库信息", "提示",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	private void createTransferButtonMouseReleased(MouseEvent e) {
		if (createTransferButton.isEnabled()) {
			if (order == null) {
				JOptionPane.showMessageDialog(null, "未能正确获取仓库信息，无法生成中转单", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			Vector<Vector<String>> info = order.getOrderInfo();
			if (info.size()== 0) {
				JOptionPane.showMessageDialog(null, "订单列表为空，无法生成中转单", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			} else {
		
				transferList = transferLoad.createTransferList(order);
				setTransferList();
				setDisabled();
			}
		}
	}
	
	private void setDisabled(){
		listID.setEnabled(false);
		centerID.setEnabled(false);
		centerName.setEnabled(false);
		destID.setEnabled(false);
		destName.setEnabled(false);
	}
	
	private void setTransferList(){
		if (transferList != null) {
			if(transferList.transferListID != null){
			listID.setText(transferList.transferListID);
			}
			else {
				listID.setText("保存后生成");
			}
			centerID.setText(transferList.transferCenterID);
			centerName.setText(transferList.transferCenter);
			destID.setText(transferList.target);
			destName.setText(transferList.targetName);
			date.setText(transferList.date);
			vehicleID.setText("请输入班次");
			staffName.setText(transferList.staff);
			if (transferList.transferType.equals("汽运")) {
				driverName.setText("请输入司机姓名");
				label12.setText("押运员");
				label10.setText("车辆编号");
			}else{
			driverName.setText(transferList.fee);
			label12.setText("费用");
			label10.setText("班次");
			}
			DefaultTableModel model = new DefaultTableModel(transferList.orderAndPosition,transferList.header);
			loadTable.setModel(model);
			loadTable.updateUI();
			
			remove(loadPane);
			add(transferListPane,BorderLayout.CENTER);
			transferListPane.updateUI();
			transferListPane.setVisible(true);
		}
	}

	private void cancelLoadMouseReleased(MouseEvent e) {
		remove(transferListPane);
		add(loadPane,BorderLayout.CENTER);
		loadPane.updateUI();
		loadPane.setVisible(true);
	}

	private void saveListMouseReleased(MouseEvent e) {
		if(transferList.transferType.equals("汽运")){
			transferList.driver = driverName.getText();
		}else{
			transferList.fee = driverName.getText();
		}
		transferList.date = date.getText();
		transferList.vehicleCode = vehicleID.getText();
		if(transferList.fee.equals("") || transferList.fee.equals(0.0+"")|| transferList.vehicleCode.equals("请输入班次")|| transferList.vehicleCode.equals("")){
			JOptionPane.showMessageDialog(null, "请正确输入运费和班次", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			ResultMessage result = transferLoad.saveTransferList(transferList);
			if (result == ResultMessage.SUCCESS) {
				sort();
				JOptionPane.showMessageDialog(null, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
				remove(transferListPane);
				add(loadPane,BorderLayout.CENTER);
				loadPane.updateUI();
				loadPane.setVisible(true);
			}else {
				JOptionPane.showMessageDialog(null, "操作失败", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (RemoteException e1) {
			JOptionPane.showMessageDialog(null, "网络连接中断", "提示", JOptionPane.INFORMATION_MESSAGE);
			e1.printStackTrace();
		}
	}

	private void removeOrderButtonMouseReleased(MouseEvent e) {
		DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
		Vector<Vector<String>> info = order.getOrderInfo();
		int[] rows = orderTable.getSelectedRows();
		for(int i = (rows.length-1) ; i >= 0  ; i--){
			System.out.println(rows[i]);
			info.remove(rows[i]);
		}
		model.setDataVector(info, order.header);
		orderTable.setModel(model);
		orderTable.updateUI();
		orderTable.repaint();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		loadPane = new JTabbedPane();
		loadPanel = new JPanel();
		scrollPane1 = new JScrollPane();
		orderTable = new JTable();
		label1 = new JLabel();
		transferTypeBox = new JComboBox();
		label2 = new JLabel();
		destBox = new JComboBox();
		getOrderButton = new JButton();
		createTransferButton = new JButton();
		removeOrderButton = new JButton();
		transferListPane = new JTabbedPane();
		DeliveryListPanel = new JPanel();
		scrollPane2 = new JScrollPane();
		loadTable = new JTable();
		cancelLoad = new JButton();
		saveList = new JButton();
		label5 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		label8 = new JLabel();
		label9 = new JLabel();
		listID = new JTextField();
		centerID = new JTextField();
		centerName = new JTextField();
		destID = new JTextField();
		destName = new JTextField();
		label10 = new JLabel();
		label11 = new JLabel();
		label12 = new JLabel();
		vehicleID = new JTextField();
		staffName = new JTextField();
		driverName = new JTextField();
		label4 = new JLabel();
		date = new JTextField();

		//======== this ========
		setLayout(new BorderLayout());

		//======== loadPane ========
		{
			loadPane.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//======== loadPanel ========
			{

				//======== scrollPane1 ========
				{

					//---- orderTable ----
					orderTable.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
					scrollPane1.setViewportView(orderTable);
				}

				//---- label1 ----
				label1.setText("\u8fd0\u8f93\u7c7b\u578b\uff1a");
				label1.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- transferTypeBox ----
				transferTypeBox.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- label2 ----
				label2.setText("\u76ee\u7684\u5730\uff1a");
				label2.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- destBox ----
				destBox.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- getOrderButton ----
				getOrderButton.setText("\u641c\u7d22\u5e93\u5b58");
				getOrderButton.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				getOrderButton.setIcon(new ImageIcon(getClass().getResource("/icons/search_16x16.png")));
				getOrderButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						getOrderButtonMouseReleased(e);
					}
				});

				//---- createTransferButton ----
				createTransferButton.setText("\u751f\u6210");
				createTransferButton.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				createTransferButton.setIcon(new ImageIcon(getClass().getResource("/icons/new_24x24.png")));
				createTransferButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						createTransferButtonMouseReleased(e);
					}
				});

				//---- removeOrderButton ----
				removeOrderButton.setText("\u79fb\u9664");
				removeOrderButton.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				removeOrderButton.setIcon(new ImageIcon(getClass().getResource("/icons/delete_24x24.png")));
				removeOrderButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						removeOrderButtonMouseReleased(e);
					}
				});

				GroupLayout loadPanelLayout = new GroupLayout(loadPanel);
				loadPanel.setLayout(loadPanelLayout);
				loadPanelLayout.setHorizontalGroup(
					loadPanelLayout.createParallelGroup()
						.addGroup(loadPanelLayout.createSequentialGroup()
							.addGroup(loadPanelLayout.createParallelGroup()
								.addGroup(loadPanelLayout.createSequentialGroup()
									.addComponent(label1)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(transferTypeBox, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(label2)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(destBox, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(getOrderButton, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
									.addGap(0, 243, Short.MAX_VALUE))
								.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(loadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(removeOrderButton, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
								.addComponent(createTransferButton, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
							.addContainerGap())
				);
				loadPanelLayout.setVerticalGroup(
					loadPanelLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, loadPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(loadPanelLayout.createParallelGroup()
								.addComponent(label1, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(transferTypeBox)
								.addComponent(label2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(loadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
									.addComponent(destBox)
									.addComponent(getOrderButton, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(loadPanelLayout.createParallelGroup()
								.addGroup(loadPanelLayout.createSequentialGroup()
									.addComponent(createTransferButton)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(removeOrderButton))
								.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
			}
			loadPane.addTab("\u4e2d\u8f6c\u88c5\u8fd0", loadPanel);
		}
		add(loadPane, BorderLayout.CENTER);

		//======== transferListPane ========
		{
			transferListPane.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//======== DeliveryListPanel ========
			{

				//======== scrollPane2 ========
				{

					//---- loadTable ----
					loadTable.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
					scrollPane2.setViewportView(loadTable);
				}

				//---- cancelLoad ----
				cancelLoad.setText("\u53d6\u6d88");
				cancelLoad.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				cancelLoad.setIcon(new ImageIcon(getClass().getResource("/icons/cancel_24x24.png")));
				cancelLoad.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						cancelLoadMouseReleased(e);
					}
				});

				//---- saveList ----
				saveList.setText("\u4fdd\u5b58");
				saveList.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				saveList.setIcon(new ImageIcon(getClass().getResource("/icons/save_24x24.png")));
				saveList.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						saveListMouseReleased(e);
					}
				});

				//---- label5 ----
				label5.setText("\u4e2d\u8f6c\u4e2d\u5fc3\u7f16\u53f7");

				//---- label6 ----
				label6.setText("\u4e2d\u8f6c\u5355\u7f16\u53f7");

				//---- label7 ----
				label7.setText("\u76ee\u7684\u5730");

				//---- label8 ----
				label8.setText("\u76ee\u7684\u5730\u7f16\u53f7");

				//---- label9 ----
				label9.setText("\u4e2d\u8f6c\u4e2d\u5fc3");

				//---- label10 ----
				label10.setText("\u73ed\u6b21  ");

				//---- label11 ----
				label11.setText("\u76d1\u88c5\u5458");

				//---- label12 ----
				label12.setText("\u62bc\u8fd0\u5458");

				//---- label4 ----
				label4.setText("\u88c5\u8fd0\u65e5\u671f");

				GroupLayout DeliveryListPanelLayout = new GroupLayout(DeliveryListPanel);
				DeliveryListPanel.setLayout(DeliveryListPanelLayout);
				DeliveryListPanelLayout.setHorizontalGroup(
					DeliveryListPanelLayout.createParallelGroup()
						.addGroup(DeliveryListPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(GroupLayout.Alignment.TRAILING, DeliveryListPanelLayout.createSequentialGroup()
									.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(cancelLoad, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
										.addComponent(saveList, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGroup(DeliveryListPanelLayout.createParallelGroup()
										.addGroup(DeliveryListPanelLayout.createSequentialGroup()
											.addComponent(label6)
											.addGap(32, 32, 32)
											.addComponent(listID, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
											.addGap(135, 135, 135)
											.addComponent(label4)
											.addGap(10, 10, 10)
											.addComponent(date, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
										.addGroup(DeliveryListPanelLayout.createSequentialGroup()
											.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label7)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(destName, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label8)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(destID, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label9)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(centerName, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label5)
													.addGap(18, 18, 18)
													.addComponent(centerID, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
											.addGap(135, 135, 135)
											.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label12, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
													.addGap(18, 18, 18)
													.addComponent(driverName, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addGap(18, 18, 18)
													.addComponent(staffName, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
												.addComponent(label10, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)))
										.addGroup(DeliveryListPanelLayout.createSequentialGroup()
											.addGap(365, 365, 365)
											.addComponent(vehicleID, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
									.addGap(0, 330, Short.MAX_VALUE)))
							.addContainerGap())
				);
				DeliveryListPanelLayout.setVerticalGroup(
					DeliveryListPanelLayout.createParallelGroup()
						.addGroup(DeliveryListPanelLayout.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(saveList)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(cancelLoad)
							.addContainerGap())
						.addGroup(GroupLayout.Alignment.TRAILING, DeliveryListPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(label6)
									.addComponent(listID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGap(3, 3, 3)
									.addComponent(label4))
								.addComponent(date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(GroupLayout.Alignment.TRAILING, DeliveryListPanelLayout.createSequentialGroup()
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(vehicleID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGap(9, 9, 9)
									.addGroup(DeliveryListPanelLayout.createParallelGroup()
										.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(label5)
											.addComponent(centerID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addComponent(label10))))
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGap(6, 6, 6)
									.addGroup(DeliveryListPanelLayout.createParallelGroup()
										.addComponent(label9)
										.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(staffName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(label11))))
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(centerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGap(3, 3, 3)
									.addGroup(DeliveryListPanelLayout.createParallelGroup()
										.addComponent(label8)
										.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(driverName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(label12))))
								.addComponent(destID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addComponent(label7)
								.addComponent(destName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE))
				);
			}
			transferListPane.addTab("\u4e2d\u8f6c\u5355", DeliveryListPanel);
		}
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JTabbedPane loadPane;
	private JPanel loadPanel;
	private JScrollPane scrollPane1;
	private JTable orderTable;
	private JLabel label1;
	private JComboBox transferTypeBox;
	private JLabel label2;
	private JComboBox destBox;
	private JButton getOrderButton;
	private JButton createTransferButton;
	private JButton removeOrderButton;
	private JTabbedPane transferListPane;
	private JPanel DeliveryListPanel;
	private JScrollPane scrollPane2;
	private JTable loadTable;
	private JButton cancelLoad;
	private JButton saveList;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JLabel label8;
	private JLabel label9;
	private JTextField listID;
	private JTextField centerID;
	private JTextField centerName;
	private JTextField destID;
	private JTextField destName;
	private JLabel label10;
	private JLabel label11;
	private JLabel label12;
	private JTextField vehicleID;
	private JTextField staffName;
	private JTextField driverName;
	private JLabel label4;
	private JTextField date;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
