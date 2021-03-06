/*
 * Created by JFormDesigner on Mon Nov 30 22:28:21 CST 2015
 */

package presentation.transfer.hall;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import org.jb2011.lnf.windows2.Windows2LookAndFeel;

import data.message.LoginMessage;
import data.message.ResultMessage;
import businesslogic.impl.transfer.hall.TransferHallController;
import businesslogic.service.Transfer.hall.TransferHallService;

/**
 * @author sunxu
 */
public class TransferHallFrame extends JFrame {
	TransferHallService transferHall;
	EntruckReceivePanel entruckReceivePanel;// 装车接收面板
	LoadAndSortPanel loadAndSortPanel;// 分拣装车面板
	DriverPanel driverPanel;// 司机信息管理面板
	TruckPanel truckPanel;// 车辆信息管理面板
	ReceiveMoneyPanel receiveMoneyPanel;
	ArrayList<JButton> buttons;

	public TransferHallFrame(LoginMessage login) {
		try {
			UIManager.setLookAndFeel(new Windows2LookAndFeel());
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		initComponents();
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
			//	closeDialog.setVisible(true);
				boolean loadClear = true;
				boolean entruckClear = true;
				if (loadAndSortPanel != null) {
					if (!loadAndSortPanel.isClear()) {
						loadClear = false;
					}
				}
				
				if(entruckReceivePanel != null){
					if(!entruckReceivePanel.isClear()){
						entruckClear = false;
					}
				}
				if(loadClear && entruckClear){
					System.exit(DISPOSE_ON_CLOSE);
				}else{
					JOptionPane.showMessageDialog(null, "有已审批到达单或装车单未处理,请处理完后再退出", "提示", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		buttons = new ArrayList<JButton>();
		buttons.add(receiveButton);
		buttons.add(loadButton);
		buttons.add(driverButton);
		buttons.add(truckButton);
		buttons.add(moneyButton);

		try {
			transferHall = new TransferHallController(login);
			String[] user = transferHall.getUserInfo().split("-");
			staff.setText(staff.getText()+user[0]);
			institution.setText(institution.getText()+user[1]);
		} catch (Exception e) {
			e.printStackTrace();
			errorDialog.validate();
			errorDialog.repaint();
			errorDialog.setVisible(true);
		}
		entruckReceiveStart();
		this.setVisible(true);
	}

	public void entruckReceiveStart() {
		receiveButton.setSelected(true);
		receiveButton.setEnabled(false);

		if (entruckReceivePanel == null)
			try {
				entruckReceivePanel = new EntruckReceivePanel(
						transferHall.startEntruckReceive());
			} catch (Exception e) {
				e.printStackTrace();
				errorDialog.setVisible(true);
			}

		Container container = getContentPane();
		container.remove(emptyPanel);
		if (loadAndSortPanel != null)
			container.remove(loadAndSortPanel);
		if (truckPanel != null)
			container.remove(truckPanel);
		if (driverPanel != null)
			container.remove(driverPanel);
		if(receiveMoneyPanel != null)
			container.remove(receiveMoneyPanel);
		container.add(entruckReceivePanel, BorderLayout.CENTER);
		container.repaint();

		entruckReceivePanel.validate();
		entruckReceivePanel.updateUI();
		entruckReceivePanel.setVisible(true);
	}

	// ==============================监听=================================
	
	private void setButtons(int index){
		for(int i = 0 ; i < buttons.size();i++){
			JButton b = buttons.get(i);
			if(i == index){
				b.setSelected(true);
				b.setEnabled(false);
			}else{
				b.setSelected(false);
				b.setEnabled(true);
			}
		}
	}
	
	
	private void receiveButtonMouseClicked(MouseEvent e) {
		setButtons(0);

		entruckReceiveStart();
	}

	private void loadButtonMouseClicked(MouseEvent e) {
		setButtons(1);

		if (loadAndSortPanel == null)
			try {
				loadAndSortPanel = new LoadAndSortPanel(
						transferHall.startOrderSort());
			} catch (RemoteException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,"网络连接中断，请稍后再试"	, "提示", JOptionPane.INFORMATION_MESSAGE);
			}

		Container container = getContentPane();
		container.remove(emptyPanel);
		container.remove(entruckReceivePanel);
		if (truckPanel != null) {
			container.remove(truckPanel);
		}
		if (driverPanel != null) {
			container.remove(driverPanel);
		}
		if(receiveMoneyPanel != null){
			container.remove(receiveMoneyPanel);
		}
		if(loadAndSortPanel != null){
			container.remove(loadAndSortPanel);
		}
		
		container.add(loadAndSortPanel, BorderLayout.CENTER);
		container.repaint();

		loadAndSortPanel.validate();
		loadAndSortPanel.updateUI();
		loadAndSortPanel.setVisible(true);
	}

	private void errorSureMouseClicked(MouseEvent e) {
		System.exit(0);
	}

	private void driverButtonMouseClicked(MouseEvent e) {
		setButtons(2);

		if (driverPanel == null) {
			driverPanel = new DriverPanel(transferHall.startDriverManagement());
		}

		Container container = getContentPane();
		container.remove(emptyPanel);
		container.remove(entruckReceivePanel);
		if (truckPanel != null)
			remove(truckPanel);
		if (loadAndSortPanel != null)
			remove(loadAndSortPanel);
		if(receiveMoneyPanel != null)
			remove(receiveMoneyPanel);
		if(entruckReceivePanel != null)
			remove(entruckReceivePanel);
		container.add(driverPanel, BorderLayout.CENTER);
		container.repaint();

		driverPanel.validate();
		driverPanel.updateUI();
		driverPanel.setVisible(true);
	}

	private void truckButtonMouseClicked(MouseEvent e) {
		setButtons(3);

		if (truckPanel == null) {
			truckPanel = new TruckPanel(transferHall.startTruckManagement());
		}

		Container container = getContentPane();
		container.remove(emptyPanel);
		container.remove(entruckReceivePanel);
		if (driverPanel != null)
			remove(driverPanel);
		if (loadAndSortPanel != null)
			remove(loadAndSortPanel);
		if (entruckReceivePanel != null) 
			remove(entruckReceivePanel);
		if(receiveMoneyPanel != null)
			remove(receiveMoneyPanel);
		
		container.add(truckPanel, BorderLayout.CENTER);
		container.repaint();

		truckPanel.validate();
		truckPanel.updateUI();
		truckPanel.setVisible(true);
	}

	private void moneyButtonMouseReleased(MouseEvent e) {
		setButtons(4);
		
		if(receiveMoneyPanel == null){
			receiveMoneyPanel = new ReceiveMoneyPanel(transferHall.startReceive());
		}
		
		Container container = getContentPane();
		container.remove(emptyPanel);
		if(driverPanel != null){
			container.remove(driverPanel);
		}
		if (truckPanel != null) {
			container.remove(truckPanel);
		}
		if(loadAndSortPanel != null){
			container.remove(loadAndSortPanel);
		}
		if(entruckReceivePanel != null){
			container.remove(entruckReceivePanel);
		}
		container.add(receiveMoneyPanel,BorderLayout.CENTER);
		receiveMoneyPanel.validate();
		receiveMoneyPanel.updateUI();
		receiveMoneyPanel.setVisible(true);
		container.repaint();
		}

		private void thisWindowClosed(WindowEvent e) {
			closeDialog.setVisible(true);
		}

		private void button1MouseReleased(MouseEvent e) {
			closeDialog.setVisible(false);
		}

		private void button2MouseReleased(MouseEvent e) {
			System.exit(DISPOSE_ON_CLOSE);
		}



	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		menuBar1 = new JMenuBar();
		option = new JMenu();
		help = new JMenu();
		receiveButton = new JButton();
		loadButton = new JButton();
		driverButton = new JButton();
		truckButton = new JButton();
		moneyButton = new JButton();
		label1 = new JLabel();
		label2 = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();
		label6 = new JLabel();
		label11 = new JLabel();
		staff = new JLabel();
		institution = new JLabel();
		emptyPanel = new JPanel();
		tabbedPane1 = new JTabbedPane();
		label7 = new JLabel();
		errorDialog = new JDialog();
		panel2 = new JPanel();
		label8 = new JLabel();
		label9 = new JLabel();
		errorSure = new JButton();
		closeDialog = new JDialog();
		panel3 = new JPanel();
		label3 = new JLabel();
		label10 = new JLabel();
		button1 = new JButton();
		button2 = new JButton();

		//======== this ========
		setTitle("\u8425\u4e1a\u5385");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				thisWindowClosed(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== panel1 ========
		{
			panel1.setFont(new Font("\u65b9\u6b63\u7b49\u7ebf", Font.PLAIN, 14));

			//======== menuBar1 ========
			{
				menuBar1.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//======== option ========
				{
					option.setText("\u9009\u9879");
					option.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				}
				menuBar1.add(option);

				//======== help ========
				{
					help.setText("\u5e2e\u52a9");
					help.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				}
				menuBar1.add(help);
			}

			//---- receiveButton ----
			receiveButton.setIcon(new ImageIcon(getClass().getResource("/icons/receive.png")));
			receiveButton.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
			receiveButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					receiveButtonMouseClicked(e);
				}
			});

			//---- loadButton ----
			loadButton.setIcon(new ImageIcon(getClass().getResource("/icons/load.png")));
			loadButton.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
			loadButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					loadButtonMouseClicked(e);
				}
			});

			//---- driverButton ----
			driverButton.setIcon(new ImageIcon(getClass().getResource("/icons/man.png")));
			driverButton.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
			driverButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					driverButtonMouseClicked(e);
				}
			});

			//---- truckButton ----
			truckButton.setIcon(new ImageIcon(getClass().getResource("/icons/truck.png")));
			truckButton.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
			truckButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					truckButtonMouseClicked(e);
				}
			});

			//---- moneyButton ----
			moneyButton.setIcon(new ImageIcon(getClass().getResource("/icons/money.png")));
			moneyButton.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
			moneyButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					moneyButtonMouseReleased(e);
				}
			});

			//---- label1 ----
			label1.setText("\u88c5\u8f66\u63a5\u6536");
			label1.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//---- label2 ----
			label2.setText("\u5206\u62e3\u88c5\u8f66");
			label2.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//---- label4 ----
			label4.setText("\u53f8\u673a\u4fe1\u606f");
			label4.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//---- label5 ----
			label5.setText("\u8f66\u8f86\u4fe1\u606f");
			label5.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//---- label6 ----
			label6.setText("\u7ed3\u7b97");
			label6.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

			//---- label11 ----
			label11.setText("text");
			label11.setIcon(new ImageIcon(getClass().getResource("/icons/Man_with_mackbook_on_desk_72px_1186629_easyicon.net.png")));

			//---- staff ----
			staff.setText("\u59d3\u540d\uff1a");
			staff.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 14));

			//---- institution ----
			institution.setText("\u673a\u6784\uff1a");
			institution.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 14));

			GroupLayout panel1Layout = new GroupLayout(panel1);
			panel1.setLayout(panel1Layout);
			panel1Layout.setHorizontalGroup(
				panel1Layout.createParallelGroup()
					.addGroup(panel1Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(panel1Layout.createParallelGroup()
							.addComponent(receiveButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addGroup(panel1Layout.createSequentialGroup()
								.addGap(10, 10, 10)
								.addComponent(label1)))
						.addGap(18, 18, 18)
						.addGroup(panel1Layout.createParallelGroup()
							.addComponent(loadButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addGroup(panel1Layout.createSequentialGroup()
								.addGap(10, 10, 10)
								.addComponent(label2)))
						.addGap(18, 18, 18)
						.addGroup(panel1Layout.createParallelGroup()
							.addComponent(driverButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addGroup(panel1Layout.createSequentialGroup()
								.addGap(10, 10, 10)
								.addComponent(label4, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)))
						.addGap(18, 18, 18)
						.addGroup(panel1Layout.createParallelGroup()
							.addGroup(panel1Layout.createSequentialGroup()
								.addGap(10, 10, 10)
								.addComponent(label5)
								.addGap(60, 60, 60)
								.addComponent(label6)
								.addGap(49, 456, Short.MAX_VALUE))
							.addGroup(panel1Layout.createSequentialGroup()
								.addComponent(truckButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(moneyButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
								.addGap(166, 166, 166)
								.addComponent(label11, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(panel1Layout.createParallelGroup()
									.addComponent(staff, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
									.addComponent(institution, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
								.addContainerGap())))
					.addComponent(menuBar1, GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE)
			);
			panel1Layout.setVerticalGroup(
				panel1Layout.createParallelGroup()
					.addGroup(panel1Layout.createSequentialGroup()
						.addComponent(menuBar1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(loadButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(receiveButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(driverButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(truckButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(moneyButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
								.addComponent(staff)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(institution))
							.addComponent(label11, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(panel1Layout.createParallelGroup()
							.addComponent(label5)
							.addComponent(label1)
							.addComponent(label2)
							.addComponent(label4)
							.addComponent(label6))
						.addContainerGap(22, Short.MAX_VALUE))
			);
		}
		contentPane.add(panel1, BorderLayout.NORTH);

		//======== emptyPanel ========
		{
			emptyPanel.setMaximumSize(new Dimension(1280, 720));
			emptyPanel.setFont(new Font("\u5b8b\u4f53", Font.ITALIC, 14));
			emptyPanel.setLayout(new BorderLayout());

			//======== tabbedPane1 ========
			{
				tabbedPane1.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- label7 ----
				label7.setText("text");
				label7.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				tabbedPane1.addTab("text", label7);
			}
			emptyPanel.add(tabbedPane1, BorderLayout.CENTER);
		}
		contentPane.add(emptyPanel, BorderLayout.CENTER);
		setSize(930, 590);
		setLocationRelativeTo(getOwner());

		//======== errorDialog ========
		{
			errorDialog.setTitle("\u7f51\u7edc\u5f02\u5e38");
			Container errorDialogContentPane = errorDialog.getContentPane();
			errorDialogContentPane.setLayout(new BorderLayout());

			//======== panel2 ========
			{

				//---- label8 ----
				label8.setText("\u7f51\u7edc\u8fde\u63a5\u4e2d\u65ad");
				label8.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- label9 ----
				label9.setText("\u8bf7\u7a0d\u540e\u518d\u8bd5");
				label9.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- errorSure ----
				errorSure.setText("\u786e\u5b9a");
				errorSure.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				errorSure.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						errorSureMouseClicked(e);
					}
				});

				GroupLayout panel2Layout = new GroupLayout(panel2);
				panel2.setLayout(panel2Layout);
				panel2Layout.setHorizontalGroup(
					panel2Layout.createParallelGroup()
						.addGroup(panel2Layout.createSequentialGroup()
							.addGroup(panel2Layout.createParallelGroup()
								.addGroup(panel2Layout.createSequentialGroup()
									.addGap(88, 88, 88)
									.addGroup(panel2Layout.createParallelGroup()
										.addGroup(panel2Layout.createSequentialGroup()
											.addGap(10, 10, 10)
											.addComponent(label9, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
										.addComponent(label8, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)))
								.addGroup(panel2Layout.createSequentialGroup()
									.addGap(109, 109, 109)
									.addComponent(errorSure)))
							.addContainerGap(88, Short.MAX_VALUE))
				);
				panel2Layout.setVerticalGroup(
					panel2Layout.createParallelGroup()
						.addGroup(panel2Layout.createSequentialGroup()
							.addGap(46, 46, 46)
							.addComponent(label8)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(label9)
							.addGap(18, 18, 18)
							.addComponent(errorSure)
							.addContainerGap(18, Short.MAX_VALUE))
				);
			}
			errorDialogContentPane.add(panel2, BorderLayout.CENTER);
			errorDialog.setSize(290, 200);
			errorDialog.setLocationRelativeTo(null);
		}

		//======== closeDialog ========
		{
			closeDialog.setTitle("\u63d0\u793a");
			Container closeDialogContentPane = closeDialog.getContentPane();
			closeDialogContentPane.setLayout(new BorderLayout());

			//======== panel3 ========
			{

				//---- label3 ----
				label3.setText("\u8bf7\u68c0\u67e5\u662f\u5426\u5df2\u7ecf\u5904\u7406");
				label3.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- label10 ----
				label10.setText("\u5168\u90e8\u5df2\u5ba1\u6279\u88c5\u8f66\u5355\u548c\u5230\u8fbe\u5355");
				label10.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));

				//---- button1 ----
				button1.setText("\u672a\u5904\u7406\u5b8c");
				button1.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				button1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						button1MouseReleased(e);
					}
				});

				//---- button2 ----
				button2.setText("\u786e\u8ba4\u5173\u95ed");
				button2.setFont(new Font("\u7b49\u7ebf", Font.PLAIN, 14));
				button2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						button2MouseReleased(e);
					}
				});

				GroupLayout panel3Layout = new GroupLayout(panel3);
				panel3.setLayout(panel3Layout);
				panel3Layout.setHorizontalGroup(
					panel3Layout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
							.addContainerGap(57, Short.MAX_VALUE)
							.addGroup(panel3Layout.createParallelGroup()
								.addGroup(panel3Layout.createSequentialGroup()
									.addGap(10, 10, 10)
									.addComponent(button1, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
									.addContainerGap())
								.addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
									.addComponent(label10, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
									.addGap(50, 50, 50))))
						.addGroup(panel3Layout.createSequentialGroup()
							.addGroup(panel3Layout.createParallelGroup()
								.addGroup(panel3Layout.createSequentialGroup()
									.addGap(79, 79, 79)
									.addComponent(label3))
								.addGroup(panel3Layout.createSequentialGroup()
									.addGap(94, 94, 94)
									.addComponent(button2, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
							.addContainerGap(64, Short.MAX_VALUE))
				);
				panel3Layout.setVerticalGroup(
					panel3Layout.createParallelGroup()
						.addGroup(panel3Layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(label3, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(label10, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(button1, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(button2, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
				);
			}
			closeDialogContentPane.add(panel3, BorderLayout.CENTER);
			closeDialog.pack();
			closeDialog.setLocationRelativeTo(closeDialog.getOwner());
		}
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JPanel panel1;
	private JMenuBar menuBar1;
	private JMenu option;
	private JMenu help;
	private JButton receiveButton;
	private JButton loadButton;
	private JButton driverButton;
	private JButton truckButton;
	private JButton moneyButton;
	private JLabel label1;
	private JLabel label2;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JLabel label11;
	private JLabel staff;
	private JLabel institution;
	private JPanel emptyPanel;
	private JTabbedPane tabbedPane1;
	private JLabel label7;
	private JDialog errorDialog;
	private JPanel panel2;
	private JLabel label8;
	private JLabel label9;
	private JButton errorSure;
	private JDialog closeDialog;
	private JPanel panel3;
	private JLabel label3;
	private JLabel label10;
	private JButton button1;
	private JButton button2;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
