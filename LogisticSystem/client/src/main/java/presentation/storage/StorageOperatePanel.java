/*
 * Created by JFormDesigner on Thu Dec 03 15:28:21 CST 2015
 */

package presentation.storage;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import data.enums.POType;
import data.message.ResultMessage;
import data.vo.StorageInVO;
import data.vo.StorageInfoVO;
import data.vo.StorageListVO;
import data.vo.StorageOutVO;
import businesslogic.service.storage.StorageOperateService;

/**
 * @author sunxu
 */
public class StorageOperatePanel extends JPanel {
	StorageOperateService storageOperate;
	StorageListVO storageList;
	StorageInVO storageInVO;
	StorageOutVO storageOutVO;
	public StorageOperatePanel(StorageOperateService storageOperate) {
		this.storageOperate = storageOperate;
		initComponents();
		setPercent();
		setStorageInfoTable();
		selectStorageListButton.setEnabled(false);
		this.repaint();
	}

	private void clearInitInput() {
		numInput.setText("");
		shelfInput.setText("");
		planeInput.setText("");
		trainInput.setText("");
		truckInput.setText("");
		flexibleInput.setText("");
		alarmPercentInput.setText("0.90");
		this.updateUI();
		this.repaint();
	}

	private void setPercent(){
		double[] percent = storageOperate.showSpace();
		if (percent != null) {
			planePercent.setText(percent[0]+"");
			trainPercent.setText(percent[1]+"");
			truckPercent.setText(percent[2]+"");
		}
	}
	
	private void setStorageInfoTable(){
		StorageInfoVO storage = storageOperate.storageCheck();
		if (storage == null) {
			saveStorageCheck.setEnabled(false);
			return ;
		}
		DefaultTableModel model = new DefaultTableModel(storage.orderAndPostition,storage.header);
		storageInfoTable.setModel(model);
		storageInfoTable.updateUI();
	}
	
	private boolean setInitInput() {
		int num = 0, shelf = 0, planeRow = 0, trainRow = 0, truckRow = 0, flexibleRow = 0;
		double alarmPercent = 0.0;
		try {
			num = Integer.parseInt(numInput.getText());
			shelf = Integer.parseInt(shelfInput.getText());
			planeRow = Integer.parseInt(planeInput.getText());
			trainRow = Integer.parseInt(trainInput.getText());
			truckRow = Integer.parseInt(truckInput.getText());
			flexibleRow = Integer.parseInt(flexibleInput.getText());
			alarmPercent = Double.parseDouble(alarmPercentInput.getText());
		} catch (NumberFormatException e) {
			return false;
		}
		ResultMessage result = storageOperate.inputStorageInitInfo(num, shelf,
				planeRow, trainRow, truckRow, flexibleRow, alarmPercent);
		if (result == ResultMessage.SUCCESS)
			return true;
		else
			return false;
	}

	// ==============================监听===================================
	private void storageInMouseClicked(MouseEvent e) {
		storageIn.setSelected(true);
		storageOut.setSelected(false);
	}

	private void storageOutMouseClicked(MouseEvent e) {
		storageIn.setSelected(false);
		storageOut.setSelected(true);
	}

	private void InitSureButtonMouseClicked(MouseEvent e) {
		if (InitSureButton.isEnabled()) {
			boolean r = setInitInput();
			if (r) {
				JOptionPane.showMessageDialog(null, "初始化成功", "提示信息",
						JOptionPane.INFORMATION_MESSAGE);
				numInput.setEditable(false);//当前界面所有控件失效
				shelfInput.setEditable(false);
				alarmPercentInput.setEditable(false);
				planeInput.setEditable(false);
				trainInput.setEditable(false);
				truckInput.setEditable(false);
				flexibleInput.setEditable(false);
				InitSureButton.setEnabled(false);
				reInputButton.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(null, "初始化失败", "提示信息",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void reInputButtonMouseClicked(MouseEvent e) {
		clearInitInput();
	}

	private void listTableMouseClicked(MouseEvent e) {
		selectStorageListButton.setEnabled(true);
	}

	private void searchListButtonMouseReleased(MouseEvent e) {
		int sy = -1;
		int sm = -1;
		int sd = -1;
		int ey = -1;
		int em = -1;
		int ed = -1;
		try {
			sy = Integer.parseInt(startY.getText());
			sm = Integer.parseInt(startM.getText());
			sd = Integer.parseInt(startD.getText());
			ey = Integer.parseInt(endY.getText());
			em = Integer.parseInt(endM.getText());
			ed = Integer.parseInt(endD.getText());
		} catch (NumberFormatException e2) {
			JOptionPane.showMessageDialog(null, "输入格式有误", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String start = String.format("%04d", sy)+"/"+String.format("%02d", sm)+"/"+String.format("%02d", sd);
		String end = String.format("%04d", ey)+"/"+String.format("%02d", em)+"/"+String.format("%02d", ed);

		try {
			if (storageIn.isSelected()) {
				storageList = storageOperate.getStorageInList(start, end, POType.STORAGEINLIST);
			}else {
				storageList = storageOperate.getStorageInList(start, end, POType.STORAGEOUTLIST);
			}

		} catch (RemoteException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "网络连接中断", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ParseException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "时间输入有误","提示",JOptionPane.INFORMATION_MESSAGE);
		}
		if (storageList != null) {
			setStorageList();
		} else {
			JOptionPane.showMessageDialog(null, "未能获取单据", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void setStorageList() {
		// 设置入库单，出库单列表
	}
	
	private void setStorageOutVO(){
		//设置出库单显示信息
	}

	private void setStorageInVO(){
		//设置入库单显示信息
	}
	
	private void selectStorageListButtonMouseReleased(MouseEvent e) {
		if (selectStorageListButton.isEnabled()) {
			int row = listTable.getSelectedRow();
			long id = Long.parseLong((String) listTable.getValueAt(row, 0));
			if (storageIn.isSelected()) {
				storageInVO = storageOperate.getStorageIn(id);
				if (storageInVO == null){
					JOptionPane.showMessageDialog(null, "未能正确获取入库单", "异常", JOptionPane.ERROR_MESSAGE);
					return;
				}
				setStorageInVO();
			}else{
				storageOutVO = storageOperate.getStorageOut(id);
				if (storageOutVO == null){
					JOptionPane.showMessageDialog(null, "未能正确获取出库单", "异常", JOptionPane.ERROR_MESSAGE);
					return;
				}
				setStorageOutVO();
			}
		}
	}

	private void saveStorageCheckMouseReleased(MouseEvent e) {
		if (saveStorageCheck.isEnabled()) {
			try {
				ResultMessage result = storageOperate.saveStorageInfo();
				if(result == ResultMessage.FAILED){
					JOptionPane.showMessageDialog(null, "仓库未初始化，请先初始化仓库", "提示", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "网络连接中断", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}


 	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		operatePane = new JTabbedPane();
		showPanel = new JPanel();
		scrollPane1 = new JScrollPane();
		listTable = new JTable();
		label9 = new JLabel();
		label10 = new JLabel();
		startY = new JTextField();
		label11 = new JLabel();
		label12 = new JLabel();
		startM = new JTextField();
		label13 = new JLabel();
		startD = new JTextField();
		label14 = new JLabel();
		endY = new JTextField();
		label15 = new JLabel();
		endM = new JTextField();
		label16 = new JLabel();
		endD = new JTextField();
		searchListButton = new JButton();
		selectStorageListButton = new JButton();
		storageIn = new JRadioButton();
		storageOut = new JRadioButton();
		checkPanel = new JPanel();
		scrollPane2 = new JScrollPane();
		storageInfoTable = new JTable();
		saveStorageCheck = new JButton();
		exportExcel = new JButton();
		adjustPanel = new JPanel();
		label17 = new JLabel();
		label18 = new JLabel();
		label19 = new JLabel();
		planePercent = new JTextField();
		trainPercent = new JTextField();
		truckPercent = new JTextField();
		enlargePlane = new JButton();
		enlargeTrain = new JButton();
		enlargeTruck = new JButton();
		initPanel = new JPanel();
		label1 = new JLabel();
		textField1 = new JTextField();
		label2 = new JLabel();
		numInput = new JTextField();
		label3 = new JLabel();
		label4 = new JLabel();
		shelfInput = new JTextField();
		label5 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		label8 = new JLabel();
		planeInput = new JTextField();
		trainInput = new JTextField();
		truckInput = new JTextField();
		flexibleInput = new JTextField();
		InitSureButton = new JButton();
		reInputButton = new JButton();
		label20 = new JLabel();
		alarmPercentInput = new JTextField();
		label23 = new JLabel();

		//======== this ========
		setLayout(new BorderLayout());

		//======== operatePane ========
		{

			//======== showPanel ========
			{

				//======== scrollPane1 ========
				{

					//---- listTable ----
					listTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
					listTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							listTableMouseClicked(e);
						}
					});
					scrollPane1.setViewportView(listTable);
				}

				//---- label9 ----
				label9.setText("\u8d77\u59cb\u65e5\u671f");

				//---- label10 ----
				label10.setText("\u7ec8\u6b62\u65e5\u671f");

				//---- label11 ----
				label11.setText("\u5e74");

				//---- label12 ----
				label12.setText("\u6708");

				//---- label13 ----
				label13.setText("\u65e5");

				//---- label14 ----
				label14.setText("\u5e74");

				//---- label15 ----
				label15.setText("\u6708");

				//---- label16 ----
				label16.setText("\u65e5");

				//---- searchListButton ----
				searchListButton.setText("\u67e5\u627e");
				searchListButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						searchListButtonMouseReleased(e);
					}
				});

				//---- selectStorageListButton ----
				selectStorageListButton.setText("\u67e5\u770b");
				selectStorageListButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						selectStorageListButtonMouseReleased(e);
					}
				});

				//---- storageIn ----
				storageIn.setText("\u5165\u5e93\u5355");
				storageIn.setSelected(true);
				storageIn.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						storageInMouseClicked(e);
					}
				});

				//---- storageOut ----
				storageOut.setText("\u51fa\u5e93\u5355");
				storageOut.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						storageOutMouseClicked(e);
					}
				});

				GroupLayout showPanelLayout = new GroupLayout(showPanel);
				showPanel.setLayout(showPanelLayout);
				showPanelLayout.setHorizontalGroup(
					showPanelLayout.createParallelGroup()
						.addGroup(showPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(showPanelLayout.createParallelGroup()
								.addComponent(label9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(label10, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(showPanelLayout.createParallelGroup()
								.addComponent(startY, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
								.addComponent(endY, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addGroup(showPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(label14, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
								.addComponent(label11, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(showPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addGroup(showPanelLayout.createSequentialGroup()
									.addComponent(startM, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(label12, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
								.addGroup(showPanelLayout.createSequentialGroup()
									.addComponent(endM, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(label15, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
							.addGroup(showPanelLayout.createParallelGroup()
								.addGroup(showPanelLayout.createSequentialGroup()
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(startD, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
								.addGroup(showPanelLayout.createSequentialGroup()
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(endD, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(showPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(showPanelLayout.createSequentialGroup()
									.addComponent(label16)
									.addGap(22, 22, 22)
									.addComponent(searchListButton))
								.addGroup(showPanelLayout.createSequentialGroup()
									.addComponent(label13)
									.addGap(18, 18, 18)
									.addComponent(storageIn)))
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addGroup(showPanelLayout.createParallelGroup()
								.addComponent(storageOut)
								.addComponent(selectStorageListButton))
							.addContainerGap(313, Short.MAX_VALUE))
						.addComponent(scrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
				);
				showPanelLayout.setVerticalGroup(
					showPanelLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, showPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(showPanelLayout.createParallelGroup()
								.addGroup(showPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(label11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(startM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(label12, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
									.addComponent(startD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(label13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(storageIn)
									.addComponent(storageOut))
								.addGroup(showPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(startY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(label9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(showPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label10, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
								.addComponent(endY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label14)
								.addComponent(endM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label15)
								.addComponent(endD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label16)
								.addComponent(searchListButton)
								.addComponent(selectStorageListButton))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
				);
			}
			operatePane.addTab("\u5e93\u5b58\u67e5\u770b", showPanel);

			//======== checkPanel ========
			{

				//======== scrollPane2 ========
				{

					//---- storageInfoTable ----
					storageInfoTable.setShowHorizontalLines(false);
					storageInfoTable.setEnabled(false);
					scrollPane2.setViewportView(storageInfoTable);
				}

				//---- saveStorageCheck ----
				saveStorageCheck.setText("\u4fdd\u5b58");
				saveStorageCheck.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						saveStorageCheckMouseReleased(e);
					}
				});

				//---- exportExcel ----
				exportExcel.setText("\u5bfc\u51faexcel");

				GroupLayout checkPanelLayout = new GroupLayout(checkPanel);
				checkPanel.setLayout(checkPanelLayout);
				checkPanelLayout.setHorizontalGroup(
					checkPanelLayout.createParallelGroup()
						.addGroup(checkPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(saveStorageCheck, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(exportExcel, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(560, Short.MAX_VALUE))
						.addComponent(scrollPane2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
				);
				checkPanelLayout.setVerticalGroup(
					checkPanelLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, checkPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(checkPanelLayout.createParallelGroup()
								.addComponent(saveStorageCheck, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
								.addComponent(exportExcel, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
							.addContainerGap())
				);
			}
			operatePane.addTab("\u5e93\u5b58\u76d8\u70b9", checkPanel);

			//======== adjustPanel ========
			{

				//---- label17 ----
				label17.setText("\u822a\u8fd0\u533a\uff1a");

				//---- label18 ----
				label18.setText("\u94c1\u8fd0\u533a\uff1a");

				//---- label19 ----
				label19.setText("\u6c7d\u8fd0\u533a\uff1a");

				//---- planePercent ----
				planePercent.setEditable(false);
				planePercent.setEnabled(false);

				//---- trainPercent ----
				trainPercent.setEditable(false);
				trainPercent.setEnabled(false);

				//---- truckPercent ----
				truckPercent.setEditable(false);
				truckPercent.setEnabled(false);

				//---- enlargePlane ----
				enlargePlane.setText("\u6269\u5145");

				//---- enlargeTrain ----
				enlargeTrain.setText("\u6269\u5145");

				//---- enlargeTruck ----
				enlargeTruck.setText("\u6269\u5145");

				GroupLayout adjustPanelLayout = new GroupLayout(adjustPanel);
				adjustPanel.setLayout(adjustPanelLayout);
				adjustPanelLayout.setHorizontalGroup(
					adjustPanelLayout.createParallelGroup()
						.addGroup(adjustPanelLayout.createSequentialGroup()
							.addGap(235, 235, 235)
							.addGroup(adjustPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(label17, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
								.addComponent(label18, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
								.addComponent(label19, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(adjustPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(planePercent, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
								.addComponent(trainPercent, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
								.addComponent(truckPercent, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(adjustPanelLayout.createParallelGroup()
								.addComponent(enlargePlane)
								.addComponent(enlargeTrain)
								.addComponent(enlargeTruck))
							.addContainerGap(324, Short.MAX_VALUE))
				);
				adjustPanelLayout.setVerticalGroup(
					adjustPanelLayout.createParallelGroup()
						.addGroup(adjustPanelLayout.createSequentialGroup()
							.addGap(63, 63, 63)
							.addGroup(adjustPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label17, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(planePercent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(enlargePlane))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(adjustPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label18, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(trainPercent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(enlargeTrain))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(adjustPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label19, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(truckPercent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(enlargeTruck))
							.addContainerGap(203, Short.MAX_VALUE))
				);
			}
			operatePane.addTab("\u5e93\u5b58\u8c03\u6574", adjustPanel);

			//======== initPanel ========
			{

				//---- label1 ----
				label1.setText("\u533a\u57df\u6570\u91cf");

				//---- textField1 ----
				textField1.setText("4");
				textField1.setEditable(false);

				//---- label2 ----
				label2.setText("\u8d27\u67b6\u89c4\u683c\uff08\u4f4d\uff09");

				//---- label4 ----
				label4.setText("\u6bcf\u6392\u67b6\u6570\uff08\u67b6\uff09");

				//---- label5 ----
				label5.setText("\u822a\u8fd0\u533a\uff08\u6392\uff09");

				//---- label6 ----
				label6.setText("\u94c1\u8fd0\u533a\uff08\u6392\uff09");

				//---- label7 ----
				label7.setText("\u6c7d\u8fd0\u533a\uff08\u6392\uff09");

				//---- label8 ----
				label8.setText("\u673a\u52a8\u533a\uff08\u6392\uff09");

				//---- InitSureButton ----
				InitSureButton.setText("\u786e\u8ba4");
				InitSureButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						InitSureButtonMouseClicked(e);
					}
				});

				//---- reInputButton ----
				reInputButton.setText("\u91cd\u65b0\u8f93\u5165");
				reInputButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						reInputButtonMouseClicked(e);
					}
				});

				//---- label20 ----
				label20.setText("\u62a5\u8b66\u6bd4\u4f8b\uff080.xx\uff09");

				GroupLayout initPanelLayout = new GroupLayout(initPanel);
				initPanel.setLayout(initPanelLayout);
				initPanelLayout.setHorizontalGroup(
					initPanelLayout.createParallelGroup()
						.addGroup(initPanelLayout.createSequentialGroup()
							.addGroup(initPanelLayout.createParallelGroup()
								.addGroup(initPanelLayout.createSequentialGroup()
									.addGap(229, 229, 229)
									.addComponent(label3, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
									.addGap(26, 26, 26)
									.addGroup(initPanelLayout.createParallelGroup()
										.addComponent(reInputButton, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
										.addGroup(initPanelLayout.createSequentialGroup()
											.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addComponent(label8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(label7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(label6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(label5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
												.addComponent(truckInput, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
												.addComponent(trainInput, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
												.addComponent(planeInput, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
												.addComponent(flexibleInput, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
											.addGap(8, 8, 8)
											.addComponent(label23, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))))
								.addGroup(initPanelLayout.createSequentialGroup()
									.addGap(87, 87, 87)
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(label1, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
										.addComponent(label2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(label4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addGap(18, 18, 18)
									.addGroup(initPanelLayout.createParallelGroup()
										.addComponent(shelfInput, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
										.addComponent(numInput, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addComponent(textField1, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)))
								.addGroup(initPanelLayout.createSequentialGroup()
									.addGap(87, 87, 87)
									.addComponent(label20, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(initPanelLayout.createParallelGroup()
										.addComponent(InitSureButton, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
										.addComponent(alarmPercentInput, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))))
							.addContainerGap(289, Short.MAX_VALUE))
				);
				initPanelLayout.setVerticalGroup(
					initPanelLayout.createParallelGroup()
						.addGroup(initPanelLayout.createSequentialGroup()
							.addGap(13, 13, 13)
							.addGroup(initPanelLayout.createParallelGroup()
								.addGroup(initPanelLayout.createSequentialGroup()
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label1)
										.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label2)
										.addComponent(numInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label4)
										.addComponent(shelfInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(alarmPercentInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(label20, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(label3))
								.addGroup(initPanelLayout.createSequentialGroup()
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label5)
										.addComponent(planeInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label6)
										.addComponent(trainInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label7)
										.addComponent(truckInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label8)
										.addComponent(flexibleInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(label23))))
							.addGap(42, 42, 42)
							.addGroup(initPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(reInputButton)
								.addComponent(InitSureButton))
							.addContainerGap(169, Short.MAX_VALUE))
				);
			}
			operatePane.addTab("\u521d\u59cb\u5316", initPanel);
		}
		add(operatePane, BorderLayout.NORTH);
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JTabbedPane operatePane;
	private JPanel showPanel;
	private JScrollPane scrollPane1;
	private JTable listTable;
	private JLabel label9;
	private JLabel label10;
	private JTextField startY;
	private JLabel label11;
	private JLabel label12;
	private JTextField startM;
	private JLabel label13;
	private JTextField startD;
	private JLabel label14;
	private JTextField endY;
	private JLabel label15;
	private JTextField endM;
	private JLabel label16;
	private JTextField endD;
	private JButton searchListButton;
	private JButton selectStorageListButton;
	private JRadioButton storageIn;
	private JRadioButton storageOut;
	private JPanel checkPanel;
	private JScrollPane scrollPane2;
	private JTable storageInfoTable;
	private JButton saveStorageCheck;
	private JButton exportExcel;
	private JPanel adjustPanel;
	private JLabel label17;
	private JLabel label18;
	private JLabel label19;
	private JTextField planePercent;
	private JTextField trainPercent;
	private JTextField truckPercent;
	private JButton enlargePlane;
	private JButton enlargeTrain;
	private JButton enlargeTruck;
	private JPanel initPanel;
	private JLabel label1;
	private JTextField textField1;
	private JLabel label2;
	private JTextField numInput;
	private JLabel label3;
	private JLabel label4;
	private JTextField shelfInput;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JLabel label8;
	private JTextField planeInput;
	private JTextField trainInput;
	private JTextField truckInput;
	private JTextField flexibleInput;
	private JButton InitSureButton;
	private JButton reInputButton;
	private JLabel label20;
	private JTextField alarmPercentInput;
	private JLabel label23;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
