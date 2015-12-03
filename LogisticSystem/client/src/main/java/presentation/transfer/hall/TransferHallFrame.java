/*
 * Created by JFormDesigner on Mon Nov 30 22:28:21 CST 2015
 */

package presentation.transfer.hall;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.*;

import org.jb2011.lnf.windows2.Windows2LookAndFeel;

import data.message.LoginMessage;
import data.message.ResultMessage;
import businesslogic.impl.transfer.TransferHallController;
import businesslogic.service.Transfer.hall.TransferHallService;

/**
 * @author sunxu
 */
public class TransferHallFrame extends JFrame {
	TransferHallService transferHall;
	EntruckReceivePanel entruckReceivePanel;
	LoadAndSortPanel loadAndSortPanel;
	
	public static void main(String[] args) {
		TransferHallFrame transferHallFrame = new TransferHallFrame(new LoginMessage(ResultMessage.SUCCESS,100000));
	}
	
	
	
	public TransferHallFrame(LoginMessage login) {
		try {
			UIManager.setLookAndFeel(new Windows2LookAndFeel());
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		initComponents();
		
		try {
			transferHall = new TransferHallController(login);
		} catch (Exception e) {
			e.printStackTrace();
			errorDialog.validate();
			errorDialog.repaint();
			errorDialog.setVisible(true);
		}
		entruckReceiveStart();
		this.setVisible(true);
	}
	
	
	public void entruckReceiveStart(){
		receiveButton.setSelected(true);
		receiveButton.setEnabled(false);
		
		if(entruckReceivePanel == null)
			try {
				entruckReceivePanel = new EntruckReceivePanel(transferHall.startEntruckReceive());
			} catch (Exception e) {
				e.printStackTrace();
				errorDialog.setVisible(true);
			}
		
		Container container = getContentPane();
		container.remove(emptyPanel);
		if(loadAndSortPanel != null)
		container.remove(loadAndSortPanel);
		
		container. add(entruckReceivePanel,BorderLayout.CENTER);
		container.repaint();
		
		entruckReceivePanel.validate();
		entruckReceivePanel.updateUI();
		entruckReceivePanel.setVisible(true);
	}

	private void receiveButtonMouseClicked(MouseEvent e) {
		receiveButton.setSelected(true);
		loadButton.setSelected(false);
		driverButton.setSelected(false);
		truckButton.setSelected(false);
		moneyButton.setSelected(false);
		
		receiveButton.setEnabled(false);
		loadButton.setEnabled(true);
		driverButton.setEnabled(true);
		truckButton.setEnabled(true);
		moneyButton.setEnabled(true);
		
		entruckReceiveStart();
	}

	private void loadButtonMouseClicked(MouseEvent e) {
		receiveButton.setSelected(false);
		loadButton.setSelected(true);
		driverButton.setSelected(false);
		truckButton.setSelected(false);
		moneyButton.setSelected(false);
		
		receiveButton.setEnabled(true);
		loadButton.setEnabled(false);
		driverButton.setEnabled(true);
		truckButton.setEnabled(true);
		moneyButton.setEnabled(true);

		
		if(loadAndSortPanel == null)
			try {
				loadAndSortPanel = new LoadAndSortPanel(transferHall.startOrderSort());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				entruckReceiveStart();
			}
		
		
		Container container = getContentPane();
		container.remove(emptyPanel);
		container.remove(entruckReceivePanel);
		container. add(loadAndSortPanel,BorderLayout.CENTER);
		container.repaint();
		
		loadAndSortPanel.validate();
		loadAndSortPanel.updateUI();
		loadAndSortPanel.setVisible(true);
	}



	private void errorSureMouseClicked(MouseEvent e) {
		System.exit(0);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
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
        emptyPanel = new JPanel();
        tabbedPane1 = new JTabbedPane();
        label7 = new JLabel();
        errorDialog = new JDialog();
        panel2 = new JPanel();
        label8 = new JLabel();
        label9 = new JLabel();
        errorSure = new JButton();
        label3 = new JLabel();

        //======== this ========
        setTitle("\u8425\u4e1a\u5385");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {

            //======== menuBar1 ========
            {

                //======== option ========
                {
                    option.setText("\u9009\u9879");
                }
                menuBar1.add(option);

                //======== help ========
                {
                    help.setText("\u5e2e\u52a9");
                }
                menuBar1.add(help);
            }

            //---- receiveButton ----
            receiveButton.setText("text");
            receiveButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    receiveButtonMouseClicked(e);
                }
            });

            //---- loadButton ----
            loadButton.setText("text");
            loadButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    loadButtonMouseClicked(e);
                }
            });

            //---- driverButton ----
            driverButton.setText("text");

            //---- truckButton ----
            truckButton.setText("text");

            //---- moneyButton ----
            moneyButton.setText("text");

            //---- label1 ----
            label1.setText("\u88c5\u8f66\u63a5\u6536");

            //---- label2 ----
            label2.setText("\u5206\u62e3\u88c5\u8f66");

            //---- label4 ----
            label4.setText("\u53f8\u673a\u4fe1\u606f");

            //---- label5 ----
            label5.setText("\u8f66\u8f86\u4fe1\u606f");

            //---- label6 ----
            label6.setText("\u7ed3\u7b97");

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(menuBar1, GroupLayout.PREFERRED_SIZE, 794, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addComponent(receiveButton, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(label1)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addComponent(loadButton, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(label2)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addComponent(driverButton, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label4, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addComponent(truckButton, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(label5)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(label6))
                            .addComponent(moneyButton, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(394, Short.MAX_VALUE))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(menuBar1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(receiveButton, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(loadButton, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(driverButton, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(truckButton, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(moneyButton, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label1)
                            .addComponent(label2)
                            .addComponent(label4)
                            .addComponent(label5)
                            .addComponent(label6))
                        .addContainerGap())
            );
        }
        contentPane.add(panel1, BorderLayout.NORTH);

        //======== emptyPanel ========
        {
            emptyPanel.setLayout(new BorderLayout());

            //======== tabbedPane1 ========
            {

                //---- label7 ----
                label7.setText("text");
                tabbedPane1.addTab("text", label7);
            }
            emptyPanel.add(tabbedPane1, BorderLayout.CENTER);
        }
        contentPane.add(emptyPanel, BorderLayout.CENTER);
        setSize(800, 500);
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

                //---- label9 ----
                label9.setText("\u8bf7\u7a0d\u540e\u518d\u8bd5");

                //---- errorSure ----
                errorSure.setText("\u786e\u5b9a");
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

        //---- label3 ----
        label3.setText("\u5bf9\u8bdd\u6846\u53ef\u4ee5\u76f4\u63a5\u7528 JOptionPane.showMessageDialog(null, \"\u7f51\u7edc\u8fde\u63a5\u4e2d\u65ad\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\u3002\". \"LCS\u7269\u6d41\u7ba1\u7406\u7cfb\u7edf\", JOptionPane.INFORMATION_MESSAGE)");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
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
    private JPanel emptyPanel;
    private JTabbedPane tabbedPane1;
    private JLabel label7;
    private JDialog errorDialog;
    private JPanel panel2;
    private JLabel label8;
    private JLabel label9;
    private JButton errorSure;
    private JLabel label3;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}