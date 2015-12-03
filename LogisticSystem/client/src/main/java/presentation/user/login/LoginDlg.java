/*
 * Created by JFormDesigner on Tue Nov 24 20:39:09 CST 2015
 */

package presentation.user.login;

import businesslogic.impl.user.UserBLImpl;
import businesslogic.service.user.UserBLService;
import data.message.LoginMessage;
import data.message.ResultMessage;
import data.properties.RememberedUserAccount;
import utils.Connection;
import utils.FileIOHelper;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author mist
 */
public class LoginDlg extends JDialog {
    LoginMessage loginMessage;


    public LoginDlg(Frame owner, LoginMessage loginMessage) {
        super(owner, "LCS", true);
        initComponents();
        this.loginMessage = loginMessage;
    }

    public LoginDlg(Dialog owner) {
        super(owner);
        initComponents();
    }

    /**
     * ��������¼����ѡ�����¼�
     * @param e
     */
    private void checkBox2MouseReleased(MouseEvent e) {
        if (this.chkAnotmousLogin.isSelected()) {
            this.cboxAccount.setEnabled(false);
            this.textPassword.setEnabled(false);
            this.chkRememberAccount.setEnabled(false);
        }
        else {
            this.cboxAccount.setEnabled(true);
            this.textPassword.setEnabled(true);
            this.chkRememberAccount.setEnabled(true);
        }
    }

    ArrayList<RememberedUserAccount> users = new ArrayList<>();

    public void getRememberedUsers() {

        users = (ArrayList<RememberedUserAccount>) FileIOHelper.getFromFile("user/SAVED.DAT");

        // ����ס���˺����ӵ���ѡ���б���
        for (RememberedUserAccount user: users) {
            cboxAccount.addItem(user.getSn());
        }
    }

    public void remember(RememberedUserAccount user) {
        users.add(user);
        FileIOHelper.saveToFile(users, "user/SAVED.DAT");
    }

    /**
     * ��¼��ť����¼�
     *
     * @param e
     */
    private void button1MouseReleased(MouseEvent e) {
        Connection.checkConnection();
        if (!Connection.connected) {
            JOptionPane.showMessageDialog(null, "����δ���ӣ���½ʧ��");
            return;
        }

        if (chkAnotmousLogin.isSelected()) {
            loginMessage.setResult(ResultMessage.SUCCESS);
            loginMessage.setUserSN(0);
            this.setVisible(false);
            return;
        }

        // ����˺ź������Ƿ�Ϊ�գ�Ϊ���򷵻ؼ�����д
        if (cboxAccount.getSelectedItem() == null || cboxAccount.getSelectedItem().toString().length() == 0) {
            JOptionPane.showMessageDialog(this, "�˺Ų���Ϊ��", "LCS��������ϵͳ", JOptionPane.INFORMATION_MESSAGE);
            cboxAccount.requestFocus();
            return;
        }

        if (textPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "���벻��Ϊ��", "LCS��������ϵͳ", JOptionPane.INFORMATION_MESSAGE);
            textPassword.requestFocus();
            return;
        }

        // ����˺��Ƿ��Ǵ�����,�����Ǵ������򷵻ؼ�����д
        String accountText = cboxAccount.getSelectedItem().toString();
        for (int i = 0; i < accountText.length(); i++) {
            if (accountText.charAt(i) < '0' || accountText.charAt(i) > '9') {
                JOptionPane.showMessageDialog(null, "�˺�ֻ����0~9����", "LCS��������ϵͳ", JOptionPane.INFORMATION_MESSAGE);
                cboxAccount.requestFocus();
                return;
            }
        }

        UserBLService userBLService = new UserBLImpl();
        LoginMessage loginMessage = null;

        loginMessage = userBLService.login(Long.parseLong(accountText), new String(textPassword.getPassword()));

        this.loginMessage.setResult(loginMessage.getResult());
        this.loginMessage.setUserSN(loginMessage.getUserSN());
        this.loginMessage.setUserRole(loginMessage.getUserRole());

        if (loginMessage.getResult() == ResultMessage.NOTCONNECTED) {
            System.err.println("�޷����ӵ����磬�޷�����û�������֤");
            JOptionPane.showMessageDialog(this, "����δ���ӣ���½ʧ��.", "LCS��������ϵͳ",JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (loginMessage.getResult() == ResultMessage.FAILED || loginMessage.getResult() == ResultMessage.NOTEXIST) {
            JOptionPane.showMessageDialog(this, "�û��������������", "LCS��������ϵͳ", JOptionPane.INFORMATION_MESSAGE);
            textPassword.requestFocus();
            return;
        }

        if (loginMessage.getResult() == ResultMessage.SUCCESS) {
            RememberedUserAccount userToRmb = new RememberedUserAccount();
            userToRmb.setSN(Long.parseLong((String) cboxAccount.getSelectedItem()));
            userToRmb.setPswd_md5(textPassword.getText());
            remember(userToRmb);
            this.setVisible(false);
        }
    }

    private void cboxAccountKeyPressed(KeyEvent e) {
        // TODO add your code here
    }

    private void loginButtonClicked(MouseEvent e) {
        // TODO add your code here
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        cboxAccount = new JComboBox();
        label3 = new JLabel();
        label1 = new JLabel();
        label2 = new JLabel();
        textPassword = new JPasswordField();
        chkRememberAccount = new JCheckBox();
        chkAnotmousLogin = new JCheckBox();
        button1 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));

            //---- cboxAccount ----
            cboxAccount.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            cboxAccount.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    cboxAccountKeyPressed(e);
                }
            });

            //---- label3 ----
            label3.setText("  ");
            label3.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));

            //---- label1 ----
            label1.setText("\u8d26\u53f7\uff1a");
            label1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));

            //---- label2 ----
            label2.setText("\u5bc6\u7801\uff1a");
            label2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));

            //---- textPassword ----
            textPassword.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));

            //---- chkRememberAccount ----
            chkRememberAccount.setText("\u8bb0\u4f4f\u8d26\u53f7");
            chkRememberAccount.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));

            //---- chkAnotmousLogin ----
            chkAnotmousLogin.setText("\u533f\u540d\u767b\u5f55");
            chkAnotmousLogin.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            chkAnotmousLogin.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    checkBox2MouseReleased(e);
                }
            });

            //---- button1 ----
            button1.setText("\u767b\u5f55");
            button1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            button1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    loginButtonClicked(e);
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    button1MouseReleased(e);
                }
            });

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addComponent(label3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(label2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(chkRememberAccount, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                                        .addComponent(chkAnotmousLogin))
                                    .addComponent(textPassword, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                                    .addComponent(cboxAccount, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
                                .addGap(6, 6, 6))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(button1, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                                .addContainerGap())))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(cboxAccount, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                            .addComponent(textPassword, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(chkAnotmousLogin)
                            .addComponent(chkRememberAccount))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button1)
                        .addContainerGap(7, Short.MAX_VALUE))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
        cboxAccount.setEditable(true);

        // ���������б���ѡ���Ѽ�ס���˺��ǣ��Զ���д����
        cboxAccount.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                for (RememberedUserAccount user: users) {
                    if (user.getSn() == (long)cboxAccount.getSelectedItem()) {
                        textPassword.setText(user.getPswd_md5());
                    }
                }
            }
        });
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JComboBox cboxAccount;
    private JLabel label3;
    private JLabel label1;
    private JLabel label2;
    private JPasswordField textPassword;
    private JCheckBox chkRememberAccount;
    private JCheckBox chkAnotmousLogin;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}