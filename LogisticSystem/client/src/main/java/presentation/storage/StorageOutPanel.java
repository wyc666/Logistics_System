/*
 * Created by JFormDesigner on Tue Nov 24 22:44:48 CST 2015
 */

package presentation.storage;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import data.message.ResultMessage;
import data.vo.BriefTransferAndStorageOutVO;
import data.vo.StorageOutVO;
import data.vo.TransferListVO;
import businesslogic.service.storage.StorageOutService;

/**
 * @author sunxu
 */
public class StorageOutPanel extends JPanel {
	StorageOutService storageOut;
	TransferListVO transferListVO;
	StorageOutVO out;
	BriefTransferAndStorageOutVO briefTransferAndStorageOutVO;
	public StorageOutPanel(StorageOutService storageOut) {
		this.storageOut	 = storageOut;
		initComponents();
		setList();
		this.setVisible(true);
	}
	
	private void setTransferListDisabled(){
		listID.setEnabled(false);
		centerID.setEnabled(false);
		centerName.setEnabled(false);
		destID.setEnabled(false);
		destName.setEnabled(false);
		date.setEnabled(false);
		vehicleID.setEnabled(false);
		staffName.setEnabled(false);
		driverName.setEnabled(false);
		loadTable.setEnabled(false);
	}
	
	private void setTransferList(TransferListVO transferList){
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
			vehicleID.setText(transferList.vehicleCode);
			staffName.setText(transferList.staff);
			if (transferList.transferType.equals("汽运")) {
				driverName.setText(transferList.driver);
				label12.setText("押运员");
				label10.setText("车辆编号");
			}else{
			driverName.setText(transferList.fee);
			label15.setText("费用");
			label13.setText("班次");
			}
			DefaultTableModel model = new DefaultTableModel(transferList.orderAndPosition,transferList.header);
			loadTable.setModel(model);
			loadTable.updateUI();
			remove(startPane);
			add(transferListPane,BorderLayout.CENTER);
			transferListPane.updateUI();
			transferListPane.setVisible(true);
		}
	}
	
	private void setList(){
		selectStorageOut.setEnabled(false);
		selectTransfer.setEnabled(false);
		briefTransferAndStorageOutVO = storageOut.newStorageOut();
		DefaultTableModel storageOutModel  = new DefaultTableModel(briefTransferAndStorageOutVO.getStorageOutList(),briefTransferAndStorageOutVO.getStorageOutListHeader());
		DefaultTableModel transferListModel = new DefaultTableModel(briefTransferAndStorageOutVO.getTransferList(),briefTransferAndStorageOutVO.getTransferListHeader());
		storageOutTable.setModel(storageOutModel);
		transferListTable.setModel(transferListModel);
		storageOutList.repaint();
		transferListTable.repaint();
	}
	
	private void setStorageOut(){
		if(out != null){
			transferListID.setText(out.getTransferListNum());
			outDate.setText(out.getDate());
			transferType.setText(out.getTransferType());
			DefaultTableModel model = new DefaultTableModel(out.getOrderAndPosition(),out.getHeader());
			outTable.setModel(model);
			outTable.updateUI();
			outTable.repaint();

		}
		
	}

	private void doStorageOutMouseClicked(MouseEvent e) {
		try {
			ResultMessage result = storageOut.doStorageOut();
			if(result == ResultMessage.SUCCESS){
				JOptionPane.showMessageDialog(null, "出库完成", "提示", JOptionPane.INFORMATION_MESSAGE);
				int row = storageOutTable.getSelectedRow();
				DefaultTableModel model = (DefaultTableModel) storageOutTable.getModel();
				model.removeRow(row);
				storageOutTable.setModel(model);
				storageOutTable.updateUI();
				remove(storageOutPane);
				add(startPane,BorderLayout.CENTER);
				startPane.updateUI();
				startPane.setVisible(true);
			}else{
				JOptionPane.showMessageDialog(null, "操作失败", "提示", JOptionPane.INFORMATION_MESSAGE);	
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

	private void storageOutTableMouseClicked(MouseEvent e) {
		selectStorageOut.setEnabled(true);
	}

	private void transferListTableMouseClicked(MouseEvent e) {
		selectTransfer.setEnabled(true);
	}

	private void cancelLoadMouseReleased(MouseEvent e) {
		remove(transferListPane);
		add(startPane,BorderLayout.CENTER);
		startPane.updateUI();
		startPane.setVisible(true);
	}

	private void getTransferButtonMouseReleased(MouseEvent e) {
		saveStorageOut.setVisible(true);
		doStorageOut.setVisible(false);
		int row = transferListTable.getSelectedRow();
		String s = (String) transferListTable.getValueAt(row, 0);
		long id = Long.parseLong(s);
		 transferListVO =storageOut.getTransferList(id);
		setTransferList(transferListVO);
	}

	private void createStorageOutMouseReleased(MouseEvent e) {
		out = storageOut.createStorageOutList();
		setStorageOut();
		setStorageOutDisabled();
		remove(transferListPane);
		add(storageOutPane,BorderLayout.CENTER);
		storageOutPane.updateUI();
		storageOutPane.setVisible(true);
	}
	
	private void setStorageOutDisabled(){
		transferListID.setEnabled(false);
		transferType.setEnabled(false);
		outDate.setEnabled(false);
	}

	private void storageOutCancelMouseReleased(MouseEvent e) {
		remove(storageOutPane);
		add(transferListPane,BorderLayout.CENTER);
		transferListPane.setVisible(true);
	}

	private void selectStorageOutMouseReleased(MouseEvent e) {
		if(selectStorageOut.isEnabled()){
			saveStorageOut.setVisible(false);
			storageOutCancel.setVisible(false);
			int row = storageOutTable.getSelectedRow();
			DefaultTableModel model = (DefaultTableModel)storageOutTable.getModel();
			long id = Long.parseLong((String) model.getValueAt(row, 0));
			try {
				out = storageOut.getStorageOut(id);
				setStorageOut();
				setStorageOutDisabled();
				remove(startPane);
				add(storageOutPane,BorderLayout.CENTER);
				storageOutPane.updateUI();
				storageOutPane.setVisible(true);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "网络连接中断", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
			
		}
	}

	private void saveStorageOutMouseReleased(MouseEvent e) {
		ResultMessage result = storageOut.saveStroageOut(out);
		if (result == ResultMessage.SUCCESS) {
			JOptionPane.showMessageDialog(null, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
			int row = transferListTable.getSelectedRow();
			DefaultTableModel model = (DefaultTableModel) transferListTable.getModel();
			model.removeRow(row);
			transferListTable.setModel(model);
			transferListTable.updateUI();
			remove(storageOutPane);
			add(startPane,BorderLayout.CENTER);
			startPane.updateUI();
			startPane.setVisible(true);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		startPane = new JTabbedPane();
		storageOutList = new JPanel();
		scrollPane1 = new JScrollPane();
		storageOutTable = new JTable();
		selectStorageOut = new JButton();
		textField1 = new JTextField();
		button3 = new JButton();
		transferList = new JPanel();
		scrollPane2 = new JScrollPane();
		transferListTable = new JTable();
		selectTransfer = new JButton();
		textField2 = new JTextField();
		searchTransfer = new JButton();
		getTransferButton = new JButton();
		storageOutPane = new JTabbedPane();
		storageOutVO = new JPanel();
		scrollPane4 = new JScrollPane();
		outTable = new JTable();
		storageOutCancel = new JButton();
		saveStorageOut = new JButton();
		label6 = new JLabel();
		outDate = new JTextField();
		label1 = new JLabel();
		transferListID = new JTextField();
		label3 = new JLabel();
		transferType = new JTextField();
		doStorageOut = new JButton();
		transferListPane = new JTabbedPane();
		DeliveryListPanel = new JPanel();
		scrollPane5 = new JScrollPane();
		loadTable = new JTable();
		cancelLoad = new JButton();
		label8 = new JLabel();
		label9 = new JLabel();
		label10 = new JLabel();
		label11 = new JLabel();
		label12 = new JLabel();
		listID = new JTextField();
		centerID = new JTextField();
		centerName = new JTextField();
		destID = new JTextField();
		destName = new JTextField();
		label13 = new JLabel();
		label14 = new JLabel();
		label15 = new JLabel();
		vehicleID = new JTextField();
		staffName = new JTextField();
		driverName = new JTextField();
		label16 = new JLabel();
		date = new JTextField();
		createStorageOut = new JButton();

		//======== this ========
		setLayout(new BorderLayout());

		//======== startPane ========
		{
			startPane.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//======== storageOutList ========
			{

				//======== scrollPane1 ========
				{

					//---- storageOutTable ----
					storageOutTable.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
					storageOutTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
					storageOutTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							storageOutTableMouseClicked(e);
						}
					});
					scrollPane1.setViewportView(storageOutTable);
				}

				//---- selectStorageOut ----
				selectStorageOut.setText("\u67e5\u770b");
				selectStorageOut.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				selectStorageOut.setIcon(new ImageIcon(getClass().getResource("/icons/see_24x24.png")));
				selectStorageOut.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						selectStorageOutMouseReleased(e);
					}
				});

				//---- textField1 ----
				textField1.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- button3 ----
				button3.setText("\u641c\u7d22");
				button3.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				button3.setIcon(new ImageIcon(getClass().getResource("/icons/search_16x16.png")));

				GroupLayout storageOutListLayout = new GroupLayout(storageOutList);
				storageOutList.setLayout(storageOutListLayout);
				storageOutListLayout.setHorizontalGroup(
					storageOutListLayout.createParallelGroup()
						.addGroup(storageOutListLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(textField1, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(button3, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addGap(438, 438, 438)
							.addComponent(selectStorageOut, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
							.addContainerGap())
						.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
				);
				storageOutListLayout.setVerticalGroup(
					storageOutListLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, storageOutListLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(storageOutListLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(button3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(selectStorageOut))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
				);
			}
			startPane.addTab("\u5df2\u5ba1\u6279\u51fa\u5e93\u5355", storageOutList);

			//======== transferList ========
			{

				//======== scrollPane2 ========
				{

					//---- transferListTable ----
					transferListTable.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
					transferListTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							transferListTableMouseClicked(e);
						}
					});
					scrollPane2.setViewportView(transferListTable);
				}

				//---- selectTransfer ----
				selectTransfer.setText("select");

				//---- textField2 ----
				textField2.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- searchTransfer ----
				searchTransfer.setText("\u641c\u7d22");
				searchTransfer.setIcon(new ImageIcon(getClass().getResource("/icons/search_16x16.png")));
				searchTransfer.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- getTransferButton ----
				getTransferButton.setText("\u67e5\u770b");
				getTransferButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						getTransferButtonMouseReleased(e);
					}
				});

				GroupLayout transferListLayout = new GroupLayout(transferList);
				transferList.setLayout(transferListLayout);
				transferListLayout.setHorizontalGroup(
					transferListLayout.createParallelGroup()
						.addGroup(transferListLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(transferListLayout.createParallelGroup()
								.addGroup(transferListLayout.createSequentialGroup()
									.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 786, GroupLayout.PREFERRED_SIZE)
									.addGap(32644, 32644, 32644)
									.addComponent(selectTransfer, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
								.addGroup(transferListLayout.createSequentialGroup()
									.addComponent(textField2, GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(searchTransfer)
									.addGap(261, 261, 261)
									.addComponent(getTransferButton, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
									.addGap(0, 0, Short.MAX_VALUE)))
							.addContainerGap())
				);
				transferListLayout.setVerticalGroup(
					transferListLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, transferListLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(transferListLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(searchTransfer, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(getTransferButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(transferListLayout.createParallelGroup()
								.addGroup(transferListLayout.createSequentialGroup()
									.addGap(0, 236, Short.MAX_VALUE)
									.addComponent(selectTransfer, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
								.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
							.addContainerGap())
				);
			}
			startPane.addTab("\u5df2\u5ba1\u6279\u4e2d\u8f6c\u5355", transferList);
		}
		add(startPane, BorderLayout.CENTER);

		//======== storageOutPane ========
		{
			storageOutPane.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//======== storageOutVO ========
			{

				//======== scrollPane4 ========
				{
					scrollPane4.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
					scrollPane4.setViewportView(outTable);
				}

				//---- storageOutCancel ----
				storageOutCancel.setText("\u53d6\u6d88");
				storageOutCancel.setIcon(new ImageIcon(getClass().getResource("/icons/cancel_24x24.png")));
				storageOutCancel.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				storageOutCancel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						storageOutCancelMouseReleased(e);
					}
				});

				//---- saveStorageOut ----
				saveStorageOut.setText("\u4fdd\u5b58");
				saveStorageOut.setIcon(new ImageIcon(getClass().getResource("/icons/save_24x24.png")));
				saveStorageOut.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				saveStorageOut.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						saveStorageOutMouseReleased(e);
					}
				});

				//---- label6 ----
				label6.setText("\u65e5\u671f");
				label6.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- outDate ----
				outDate.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- label1 ----
				label1.setText("\u4e2d\u8f6c\u5355\u7f16\u53f7");
				label1.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- transferListID ----
				transferListID.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- label3 ----
				label3.setText("\u8fd0\u8f93\u65b9\u5f0f");
				label3.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- transferType ----
				transferType.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- doStorageOut ----
				doStorageOut.setText("\u51fa\u5e93");
				doStorageOut.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				doStorageOut.setIcon(new ImageIcon(getClass().getResource("/icons/storageout_24x24.png")));
				doStorageOut.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						doStorageOutMouseClicked(e);
					}
				});

				GroupLayout storageOutVOLayout = new GroupLayout(storageOutVO);
				storageOutVO.setLayout(storageOutVOLayout);
				storageOutVOLayout.setHorizontalGroup(
					storageOutVOLayout.createParallelGroup()
						.addGroup(storageOutVOLayout.createSequentialGroup()
							.addGroup(storageOutVOLayout.createParallelGroup()
								.addGroup(storageOutVOLayout.createSequentialGroup()
									.addContainerGap()
									.addComponent(label1, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
									.addGap(10, 10, 10)
									.addComponent(transferListID, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(label3, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(transferType, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(label6)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(outDate, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addGap(0, 0, Short.MAX_VALUE))
								.addGroup(storageOutVOLayout.createSequentialGroup()
									.addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 690, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(storageOutVOLayout.createParallelGroup()
										.addComponent(storageOutCancel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(saveStorageOut, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(doStorageOut, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
							.addContainerGap())
				);
				storageOutVOLayout.setVerticalGroup(
					storageOutVOLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, storageOutVOLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(storageOutVOLayout.createParallelGroup()
								.addComponent(label1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addGroup(storageOutVOLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(transferListID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(label3, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
									.addComponent(transferType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(label6, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
									.addComponent(outDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
							.addGroup(storageOutVOLayout.createParallelGroup()
								.addGroup(GroupLayout.Alignment.TRAILING, storageOutVOLayout.createSequentialGroup()
									.addComponent(doStorageOut)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(saveStorageOut)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(storageOutCancel)
									.addContainerGap())
								.addComponent(scrollPane4, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)))
				);
			}
			storageOutPane.addTab("\u51fa\u5e93\u5355", storageOutVO);
		}

		//======== transferListPane ========
		{
			transferListPane.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//======== DeliveryListPanel ========
			{

				//======== scrollPane5 ========
				{

					//---- loadTable ----
					loadTable.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
					scrollPane5.setViewportView(loadTable);
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

				//---- label8 ----
				label8.setText("\u4e2d\u8f6c\u4e2d\u5fc3\u7f16\u53f7");

				//---- label9 ----
				label9.setText("\u4e2d\u8f6c\u5355\u7f16\u53f7");

				//---- label10 ----
				label10.setText("\u76ee\u7684\u5730");

				//---- label11 ----
				label11.setText("\u76ee\u7684\u5730\u7f16\u53f7");

				//---- label12 ----
				label12.setText("\u4e2d\u8f6c\u4e2d\u5fc3");

				//---- label13 ----
				label13.setText("\u73ed\u6b21  ");

				//---- label14 ----
				label14.setText("\u76d1\u88c5\u5458");

				//---- label15 ----
				label15.setText("\u62bc\u8fd0\u5458");

				//---- label16 ----
				label16.setText("\u88c5\u8fd0\u65e5\u671f");

				//---- createStorageOut ----
				createStorageOut.setText("\u51fa\u5e93");
				createStorageOut.setIcon(new ImageIcon(getClass().getResource("/icons/new_24x24.png")));
				createStorageOut.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				createStorageOut.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						createStorageOutMouseReleased(e);
					}
				});

				GroupLayout DeliveryListPanelLayout = new GroupLayout(DeliveryListPanel);
				DeliveryListPanel.setLayout(DeliveryListPanelLayout);
				DeliveryListPanelLayout.setHorizontalGroup(
					DeliveryListPanelLayout.createParallelGroup()
						.addGroup(DeliveryListPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addComponent(scrollPane5)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGroup(DeliveryListPanelLayout.createParallelGroup()
										.addGroup(DeliveryListPanelLayout.createSequentialGroup()
											.addComponent(label9)
											.addGap(32, 32, 32)
											.addComponent(listID, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
											.addGap(135, 135, 135)
											.addComponent(label16)
											.addGap(10, 10, 10)
											.addComponent(date, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
										.addGroup(DeliveryListPanelLayout.createSequentialGroup()
											.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label10)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(destName, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label11)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(destID, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label12)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(centerName, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label8)
													.addGap(18, 18, 18)
													.addComponent(centerID, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
											.addGap(135, 135, 135)
											.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label15, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
													.addGap(18, 18, 18)
													.addComponent(driverName, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
												.addGroup(DeliveryListPanelLayout.createSequentialGroup()
													.addComponent(label14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addGap(18, 18, 18)
													.addComponent(staffName, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
												.addComponent(label13, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)))
										.addGroup(DeliveryListPanelLayout.createSequentialGroup()
											.addGap(365, 365, 365)
											.addComponent(vehicleID, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
									.addGap(221, 221, 221)))
							.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(cancelLoad, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
								.addComponent(createStorageOut, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
							.addContainerGap())
				);
				DeliveryListPanelLayout.setVerticalGroup(
					DeliveryListPanelLayout.createParallelGroup()
						.addGroup(DeliveryListPanelLayout.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(createStorageOut, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(cancelLoad, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addGap(26, 26, 26))
						.addGroup(GroupLayout.Alignment.TRAILING, DeliveryListPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(label9)
									.addComponent(listID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGap(3, 3, 3)
									.addComponent(label16))
								.addComponent(date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(GroupLayout.Alignment.TRAILING, DeliveryListPanelLayout.createSequentialGroup()
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(vehicleID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGap(9, 9, 9)
									.addGroup(DeliveryListPanelLayout.createParallelGroup()
										.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(label8)
											.addComponent(centerID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addComponent(label13))))
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGap(6, 6, 6)
									.addGroup(DeliveryListPanelLayout.createParallelGroup()
										.addComponent(label12)
										.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(staffName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(label14))))
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(centerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addGap(12, 12, 12)
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addGroup(DeliveryListPanelLayout.createSequentialGroup()
									.addGap(3, 3, 3)
									.addGroup(DeliveryListPanelLayout.createParallelGroup()
										.addComponent(label11)
										.addGroup(DeliveryListPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(driverName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(label15))))
								.addComponent(destID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(DeliveryListPanelLayout.createParallelGroup()
								.addComponent(label10)
								.addComponent(destName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(scrollPane5, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE))
				);
			}
			transferListPane.addTab("\u4e2d\u8f6c\u5355", DeliveryListPanel);
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JTabbedPane startPane;
	private JPanel storageOutList;
	private JScrollPane scrollPane1;
	private JTable storageOutTable;
	private JButton selectStorageOut;
	private JTextField textField1;
	private JButton button3;
	private JPanel transferList;
	private JScrollPane scrollPane2;
	private JTable transferListTable;
	private JButton selectTransfer;
	private JTextField textField2;
	private JButton searchTransfer;
	private JButton getTransferButton;
	private JTabbedPane storageOutPane;
	private JPanel storageOutVO;
	private JScrollPane scrollPane4;
	private JTable outTable;
	private JButton storageOutCancel;
	private JButton saveStorageOut;
	private JLabel label6;
	private JTextField outDate;
	private JLabel label1;
	private JTextField transferListID;
	private JLabel label3;
	private JTextField transferType;
	private JButton doStorageOut;
	private JTabbedPane transferListPane;
	private JPanel DeliveryListPanel;
	private JScrollPane scrollPane5;
	private JTable loadTable;
	private JButton cancelLoad;
	private JLabel label8;
	private JLabel label9;
	private JLabel label10;
	private JLabel label11;
	private JLabel label12;
	private JTextField listID;
	private JTextField centerID;
	private JTextField centerName;
	private JTextField destID;
	private JTextField destName;
	private JLabel label13;
	private JLabel label14;
	private JLabel label15;
	private JTextField vehicleID;
	private JTextField staffName;
	private JTextField driverName;
	private JLabel label16;
	private JTextField date;
	private JButton createStorageOut;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
