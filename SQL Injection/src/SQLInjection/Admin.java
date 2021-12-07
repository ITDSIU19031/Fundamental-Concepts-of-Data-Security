/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLInjection;

import static SQLInjection.LoginAdmin.txtUsername;
import java.awt.Dimension;
import java.sql.Connection;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.PreparedStatement;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.Date;
/**
 *
 * @author Nhat Anh
 */
public class Admin extends javax.swing.JFrame {
    String filename = null;
    byte[] person_image;
    Connection connection = null;
    //private JTable table; 
    /**
     * Creates new form Admin
     */
    ArrayList<User> userList;
    public Admin() {
        this.person_image = null;
        initComponents();
        userList = userList();
        show_user(userList);
        show_attendance();
        show_salary();
        //show_result();
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
    }
    String s;
    //String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String url ="jdbc:mysql://localhost:3306/employeemanagement";
    String user = "root";
    String pass = "123456";
   public ArrayList<Salary> SalaryList(){
       ArrayList<Salary> SalaryList = new ArrayList();
       try{
            //Class.forName(driver);
            Connection con = DriverManager.getConnection(url,user,pass);
            String query = " WITH Total AS (\n" +
"              SELECT COUNT(Month) as Total, Attendance.Employee_ID, month, Employee.Admin_ID\n" +
"              FROM Attendance\n" +
"              INNER JOIN Employee\n" +
"              ON Attendance.Employee_ID = Employee.Employee_ID\n" +
"              GROUP BY Attendance.Employee_ID, month, Employee.Admin_ID\n" +
"               \n" +
"              )\n" +
"              SELECT (total * Salary_Amount + Bonus + Allowance ) as Salary_per_month, Salary.Salary_Amount, Salary.Bonus, Salary.Allowance, Salary.Employee_ID, Salary.Month, Total.Admin_ID\n" +
"              FROM Salary\n" +
"              INNER JOIN Total\n" +
"              ON Salary.Employee_ID = Total.Employee_ID AND Total.month = Salary.Month \n" +
"              WHERE Total.Admin_ID like '%"+SQLInjection.LoginAdmin.txtUsername.getText()+"%' ";
           //PreparedStatement pst = con.prepareStatement(qu    ery);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            Salary salary;
            while(rs.next()){
                salary = new Salary(rs.getString("Employee_ID"), rs.getString("Salary_Amount"), rs.getString("Bonus"), rs.getString("Allowance"), rs.getString("Salary_per_month"), rs.getString("Month"));
                SalaryList.add(salary);
            }
       } catch (Exception e){
            //System.out.println("Show thanh cong");
            JOptionPane.showMessageDialog(null, e);
        }
       return SalaryList;
   }
   public void show_salary(){
       ArrayList<Salary> list = SalaryList();
       DefaultTableModel model = (DefaultTableModel)TableDisplaySalary.getModel();
       Object[] row = new Object[6];
       for ( int  i = 0; i < list.size(); i++){
           row[0] = list.get(i).getEmployee_ID();
           row[1] = list.get(i).getSalary_Amount();
           row[2] = list.get(i).getBonus();
           row[3] = list.get(i).getAllowance();
           row[4] = list.get(i).getSalary_per_month();
           row[5] = list.get(i).getMonth();
           model.addRow(row);
       }
   }
   public ArrayList<Attendance> attendanceList(){
        ArrayList<Attendance> attendanceList = new ArrayList();
        try{
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               String query = "select a.Attend_ID, e.Employee_ID, e.LastName, e.FirstName, a.Month, a.Date from Employee as e, Attendance as a where e.Employee_ID = a.Employee_ID AND e.Admin_ID like '%"+SQLInjection.LoginAdmin.txtUsername.getText()+"%'"; //AND e.Employee_ID = '" +txtSearch1.getText()+"'"; 
               //PreparedStatement pst = con.prepareStatement(query); 
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery(query);
              // pst.setString(1, txtSearch1.getText());
               Attendance attendance;
               while(rs.next()){
                   attendance = new Attendance(rs.getString("Attend_ID"), rs.getString("Employee_ID"), rs.getString("LastName"), rs.getString("FirstName"), rs.getString("Month"), rs.getString("Date"));
                   attendanceList.add(attendance);
               }
        } catch (Exception e){
            //System.out.println("Show thanh cong");
            JOptionPane.showMessageDialog(null, e);
        }
        return attendanceList;
    } 
    public void show_attendance(){
        ArrayList<Attendance> list = attendanceList();
        DefaultTableModel model = (DefaultTableModel)TableDisplayAttend.getModel();
        Object[] row = new Object[6];
        for(int i = 0; i < list.size(); i++){
            row[0] = list.get(i).getAttend_ID();
            row[1] = list.get(i).getEmployee_ID();
            row[4] = list.get(i).getLastName();
            row[5] = list.get(i).getFirstName();
            row[2] = list.get(i).getMonth();
            row[3] = list.get(i).getDate();
            model.addRow(row);
        }
    }
    public ArrayList<User> userList(){
        ArrayList<User> userList = new ArrayList();
        try{
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               String query = "select E.Employee_ID, E.Admin_ID, E.LastName, E.FirstName, E.Gender, E.DoB, E.Address, \n" +
                                "EC.Phone, EC.Email,\n" +
                                "E.Employee_Image  from Employee AS E , Em_Contact AS EC\n" +
                                "where E.Admin_ID like '%"+SQLInjection.LoginAdmin.txtUsername.getText()+"%' AND E.Employee_ID = EC.Employee_ID";
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery(query);
               User user;
               while(rs.next()){
                   user = new User(rs.getString("Employee_ID"), rs.getString("Admin_ID"), rs.getString("LastName"), rs.getString("FirstName"), rs.getString("Gender"), rs.getString("Dob"), rs.getString("Address"), rs.getString("Phone"), rs.getString("Email"), rs.getBytes("Employee_Image"));
                   userList.add(user);
               }
        } catch (Exception e){
            //JOptionPane.showMessageDialog(null, e);
        }
        return userList;
    } 
    public void show_user(ArrayList<User> list){
        DefaultTableModel model = (DefaultTableModel)TableDisplayEmp.getModel();
    //    model.setRowCount(0);
        Object[] row = new Object[10];
        for(int i = 0; i < list.size(); i++){
            row[0] = list.get(i).getEmployee_ID();
            row[1] = list.get(i).getAdmin_ID();
            row[3] = list.get(i).getLastName();
            row[2] = list.get(i).getFirstName();
            row[4] = list.get(i).getGender();
            row[5] = list.get(i).getDob();
            row[6] = list.get(i).getAddress();
            row[7] = list.get(i).getContact();
            row[8] = list.get(i).getEmail();
            //row[9] = list.get(i).getDepartment();
            model.addRow(row);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("serial")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        Employee = new javax.swing.JPanel();
        btnReset = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        ImageEmployee = new javax.swing.JLabel();
        AddImage = new javax.swing.JButton();
        lbFullname = new javax.swing.JLabel();
        lbGender = new javax.swing.JLabel();
        lbDob = new javax.swing.JLabel();
        lbAddress = new javax.swing.JLabel();
        lbEmployee_ID = new javax.swing.JLabel();
        lbContact = new javax.swing.JLabel();
        lbEmail = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtEmployee_ID = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtContact = new javax.swing.JTextField();
        txtGender = new javax.swing.JComboBox<>();
        lbLastName = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        lbAdmin_ID = new javax.swing.JLabel();
        txtAdmin_ID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        txtDob = new com.toedter.calendar.JDateChooser();
        Attendance = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtSearch1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableDisplayAttend = new javax.swing.JTable();
        Salary = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtSearch2 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableDisplaySalary = new javax.swing.JTable();
        Payroll = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtEmpID = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSalary = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAllowance = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtMonth = new javax.swing.JTextField();
        btnAddSalary = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtBonus = new javax.swing.JTextField();
        txtResult = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        SearchEmp = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtSearchS = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAreaSearch = new javax.swing.JTextArea();
        RbProtection = new javax.swing.JRadioButton();
        RbNoProtection = new javax.swing.JRadioButton();
        btnSearchEmp = new javax.swing.JButton();
        btnInfomation = new javax.swing.JButton();
        btnAttendance = new javax.swing.JButton();
        btnPayroll = new javax.swing.JButton();
        btnSalary = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableDisplayEmp = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ADMIN");

        btnBack.setBackground(new java.awt.Color(253, 186, 248));
        btnBack.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(41, 120, 181));
        btnBack.setText("Logout");
        btnBack.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack)
                .addGap(776, 776, 776)
                .addComponent(jLabel1)
                .addContainerGap(1023, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(jLabel1))
                .addGap(655, 655, 655))
        );

        jLayeredPane1.setLayout(new java.awt.CardLayout());

        Employee.setBackground(new java.awt.Color(245, 255, 255));
        Employee.setForeground(new java.awt.Color(192, 254, 252));

        btnReset.setBackground(new java.awt.Color(253, 186, 248));
        btnReset.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(41, 120, 181));
        btnReset.setText("RESET");
        btnReset.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnRemove.setBackground(new java.awt.Color(253, 186, 248));
        btnRemove.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(41, 120, 181));
        btnRemove.setText("REMOVE");
        btnRemove.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(253, 186, 248));
        btnSave.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(41, 120, 181));
        btnSave.setText("UPDATE");
        btnSave.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(253, 186, 248));
        btnAdd.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(41, 120, 181));
        btnAdd.setText("ADD");
        btnAdd.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(245, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ImageEmployee.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AddImage.setBackground(new java.awt.Color(253, 186, 248));
        AddImage.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        AddImage.setForeground(new java.awt.Color(41, 120, 181));
        AddImage.setText("Add Image");
        AddImage.setActionCommand("");
        AddImage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        AddImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddImageActionPerformed(evt);
            }
        });

        lbFullname.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbFullname.setText("FirstName:");

        lbGender.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbGender.setText("Gender:");

        lbDob.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbDob.setText("Date of birth:");

        lbAddress.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbAddress.setText("Address:");

        lbEmployee_ID.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbEmployee_ID.setText("Employee_ID:");

        lbContact.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbContact.setText("Contact:");

        lbEmail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbEmail.setText("Email:");

        txtFirstName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFirstNameActionPerformed(evt);
            }
        });

        txtAddress.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddressActionPerformed(evt);
            }
        });

        txtEmployee_ID.setEditable(false);
        txtEmployee_ID.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtEmployee_ID.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtEmployee_ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployee_IDActionPerformed(evt);
            }
        });

        txtEmail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        txtContact.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContactActionPerformed(evt);
            }
        });

        txtGender.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female", "Other", " " }));
        txtGender.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));

        lbLastName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbLastName.setText("LastName:");

        txtLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLastNameActionPerformed(evt);
            }
        });

        lbAdmin_ID.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbAdmin_ID.setText("Admin_ID:");

        txtAdmin_ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdmin_IDActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText("Seacrch:");

        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbAdmin_ID)
                    .addComponent(lbEmployee_ID)
                    .addComponent(lbAddress)
                    .addComponent(lbDob)
                    .addComponent(lbGender)
                    .addComponent(lbFullname)
                    .addComponent(lbEmail)
                    .addComponent(lbContact)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtContact)
                    .addComponent(txtEmail)
                    .addComponent(txtAddress)
                    .addComponent(txtEmployee_ID)
                    .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbLastName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtAdmin_ID)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDob, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(ImageEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(112, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AddImage, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(136, 136, 136))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbFullname)
                            .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbLastName)
                            .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbGender)
                            .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbDob)
                            .addComponent(txtDob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbAddress)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbEmployee_ID)
                            .addComponent(txtEmployee_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ImageEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbAdmin_ID)
                            .addComponent(txtAdmin_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbEmail))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbContact))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(AddImage, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout EmployeeLayout = new javax.swing.GroupLayout(Employee);
        Employee.setLayout(EmployeeLayout);
        EmployeeLayout.setHorizontalGroup(
            EmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmployeeLayout.createSequentialGroup()
                .addGroup(EmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmployeeLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EmployeeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        EmployeeLayout.setVerticalGroup(
            EmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmployeeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(EmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset)
                    .addComponent(btnRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(142, 142, 142))
        );

        jLayeredPane1.add(Employee, "card2");

        Attendance.setBackground(new java.awt.Color(245, 255, 255));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Search:");

        txtSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearch1ActionPerformed(evt);
            }
        });
        txtSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearch1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearch1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearch1KeyTyped(evt);
            }
        });

        TableDisplayAttend.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TableDisplayAttend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attend_ID", "Employee_ID", "LastName", "FirstName", "Month", "Date"
            }
        ));
        TableDisplayAttend.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(TableDisplayAttend);

        javax.swing.GroupLayout AttendanceLayout = new javax.swing.GroupLayout(Attendance);
        Attendance.setLayout(AttendanceLayout);
        AttendanceLayout.setHorizontalGroup(
            AttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendanceLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(AttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(AttendanceLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        AttendanceLayout.setVerticalGroup(
            AttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendanceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(121, Short.MAX_VALUE))
        );

        jLayeredPane1.add(Attendance, "card4");

        Salary.setBackground(new java.awt.Color(245, 255, 255));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("Search: ");

        txtSearch2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtSearch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearch2ActionPerformed(evt);
            }
        });
        txtSearch2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearch2KeyReleased(evt);
            }
        });

        TableDisplaySalary.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TableDisplaySalary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Emplooyee_ID", "Salary_Amount", "Bonus", "Allowance", "SalaryPermonth", "Month"
            }
        ));
        TableDisplaySalary.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane3.setViewportView(TableDisplaySalary);

        javax.swing.GroupLayout SalaryLayout = new javax.swing.GroupLayout(Salary);
        Salary.setLayout(SalaryLayout);
        SalaryLayout.setHorizontalGroup(
            SalaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SalaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 884, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(SalaryLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        SalaryLayout.setVerticalGroup(
            SalaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalaryLayout.createSequentialGroup()
                .addGroup(SalaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SalaryLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SalaryLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(56, 56, 56)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jLayeredPane1.add(Salary, "card4");

        Payroll.setBackground(new java.awt.Color(240, 255, 255));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Employee_ID:");

        txtEmpID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("SalaryAmount:");

        txtSalary.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setText("Allowance:");

        txtAllowance.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setText("Month:");

        txtMonth.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        btnAddSalary.setBackground(new java.awt.Color(253, 186, 248));
        btnAddSalary.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnAddSalary.setForeground(new java.awt.Color(41, 120, 181));
        btnAddSalary.setText("ADD");
        btnAddSalary.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAddSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSalaryActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setText("Bonus:");

        txtBonus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtBonus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBonusActionPerformed(evt);
            }
        });

        txtResult.setEditable(false);
        txtResult.setBackground(new java.awt.Color(255, 255, 255));
        txtResult.setFont(new java.awt.Font("Arial", 0, 48)); // NOI18N
        txtResult.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtResultActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel11.setText("SALARY_PER_MONTH");

        jButton1.setBackground(new java.awt.Color(253, 186, 248));
        jButton1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(41, 120, 181));
        jButton1.setText("Reset");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PayrollLayout = new javax.swing.GroupLayout(Payroll);
        Payroll.setLayout(PayrollLayout);
        PayrollLayout.setHorizontalGroup(
            PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PayrollLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAllowance)
                    .addComponent(txtMonth)
                    .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalary)
                    .addComponent(txtBonus))
                .addGap(114, 114, 114)
                .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(txtResult, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
            .addGroup(PayrollLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(btnAddSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PayrollLayout.setVerticalGroup(
            PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PayrollLayout.createSequentialGroup()
                .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PayrollLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PayrollLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addGap(14, 14, 14)))
                .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PayrollLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(39, 39, 39)
                        .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtBonus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(txtAllowance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(txtMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PayrollLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(txtResult, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(83, 83, 83)
                .addGroup(PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(100, Short.MAX_VALUE))
        );

        jLayeredPane1.add(Payroll, "card5");

        SearchEmp.setBackground(new java.awt.Color(245, 255, 255));
        SearchEmp.setToolTipText("");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText("Employe");

        txtSearchS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        btnSearch.setBackground(new java.awt.Color(253, 186, 248));
        btnSearch.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(41, 120, 181));
        btnSearch.setText("Search ");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        txtAreaSearch.setColumns(20);
        txtAreaSearch.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtAreaSearch.setRows(5);
        jScrollPane4.setViewportView(txtAreaSearch);

        RbProtection.setBackground(new java.awt.Color(248, 255, 255));
        buttonGroup1.add(RbProtection);
        RbProtection.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RbProtection.setText("With Protection");

        RbNoProtection.setBackground(new java.awt.Color(248, 255, 255));
        buttonGroup1.add(RbNoProtection);
        RbNoProtection.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RbNoProtection.setText("Without Protection ");

        javax.swing.GroupLayout SearchEmpLayout = new javax.swing.GroupLayout(SearchEmp);
        SearchEmp.setLayout(SearchEmpLayout);
        SearchEmpLayout.setHorizontalGroup(
            SearchEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchEmpLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(SearchEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(SearchEmpLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearchS, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addGap(18, 18, 18)
                        .addComponent(RbProtection)
                        .addGap(96, 96, 96)
                        .addComponent(RbNoProtection)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        SearchEmpLayout.setVerticalGroup(
            SearchEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchEmpLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(SearchEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtSearchS, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(RbProtection)
                    .addComponent(RbNoProtection))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        jLayeredPane1.add(SearchEmp, "card6");

        btnSearchEmp.setBackground(new java.awt.Color(253, 186, 248));
        btnSearchEmp.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnSearchEmp.setForeground(new java.awt.Color(41, 120, 181));
        btnSearchEmp.setText("Search");
        btnSearchEmp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSearchEmp.setMaximumSize(new java.awt.Dimension(65, 21));
        btnSearchEmp.setMinimumSize(new java.awt.Dimension(65, 21));
        btnSearchEmp.setPreferredSize(new java.awt.Dimension(65, 21));
        btnSearchEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchEmpActionPerformed(evt);
            }
        });

        btnInfomation.setBackground(new java.awt.Color(253, 186, 248));
        btnInfomation.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnInfomation.setForeground(new java.awt.Color(41, 120, 181));
        btnInfomation.setText("Infomation");
        btnInfomation.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnInfomation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfomationActionPerformed(evt);
            }
        });

        btnAttendance.setBackground(new java.awt.Color(253, 186, 248));
        btnAttendance.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnAttendance.setForeground(new java.awt.Color(41, 120, 181));
        btnAttendance.setText("Attendance");
        btnAttendance.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        btnAttendance.setMaximumSize(new java.awt.Dimension(65, 21));
        btnAttendance.setMinimumSize(new java.awt.Dimension(65, 21));
        btnAttendance.setPreferredSize(new java.awt.Dimension(65, 21));
        btnAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttendanceActionPerformed(evt);
            }
        });

        btnPayroll.setBackground(new java.awt.Color(253, 186, 248));
        btnPayroll.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnPayroll.setForeground(new java.awt.Color(41, 120, 181));
        btnPayroll.setText("Payroll");
        btnPayroll.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPayroll.setMaximumSize(new java.awt.Dimension(65, 21));
        btnPayroll.setMinimumSize(new java.awt.Dimension(65, 21));
        btnPayroll.setPreferredSize(new java.awt.Dimension(65, 21));
        btnPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayrollActionPerformed(evt);
            }
        });

        btnSalary.setBackground(new java.awt.Color(253, 186, 248));
        btnSalary.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnSalary.setForeground(new java.awt.Color(41, 120, 181));
        btnSalary.setText("Salary");
        btnSalary.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalary.setMaximumSize(new java.awt.Dimension(65, 21));
        btnSalary.setMinimumSize(new java.awt.Dimension(65, 21));
        btnSalary.setPreferredSize(new java.awt.Dimension(65, 21));
        btnSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryActionPerformed(evt);
            }
        });

        TableDisplayEmp.setBackground(new java.awt.Color(245, 255, 255));
        TableDisplayEmp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TableDisplayEmp.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TableDisplayEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee_ID", "Admin_ID", "LastName", "FirstName", "Gender", "Dob", "Address", "Contact", "Email", "Department", "Image"
            }
        ));
        TableDisplayEmp.setGridColor(new java.awt.Color(255, 255, 255));
        TableDisplayEmp.setSelectionBackground(new java.awt.Color(245, 255, 255));
        TableDisplayEmp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableDisplayEmpMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableDisplayEmp);
        if (TableDisplayEmp.getColumnModel().getColumnCount() > 0) {
            TableDisplayEmp.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnInfomation, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSearchEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInfomation)
                    .addComponent(btnAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1810, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
     public ImageIcon ResizeImage(String imgPath){
        ImageIcon MyImage = new ImageIcon(imgPath);
        Image img = MyImage.getImage();
        Image newImage = img.getScaledInstance(ImageEmployee.getWidth(), ImageEmployee.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImage);
        return image;
    }
     int rowSelected;
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        try{
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass); 
               if(  TableDisplayEmp.getSelectedRow() > 0 ){
                   rowSelected = TableDisplayEmp.getSelectedRow();
               }
               String value = (TableDisplayEmp.getModel().getValueAt(rowSelected, 0).toString());
               
               //System.out.println( rowSelected );
               String query1 = "UPDATE Em_Contact SET Phone = '"+txtContact.getText()+"', Email = '"+txtEmail.getText()+"' WHERE Employee_ID = '"+txtEmployee_ID.getText()+"' ";
               //PreparedStatement pst1 = con.prepareStatement(query1); 
               Statement st1 = con.createStatement();
               ResultSet rs1 = st1.executeQuery(query1);
//               pst1.setString(1, txtContact.getText());
//               pst1.setString(2, txtEmail.getText());
//               pst1.setString(3, txtEmployee_ID.getText());
               //pst1.executeUpdate(); 
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               String date = sdf.format(txtDob.getDate());
               String query = "UPDATE Employee SET LastName = '"+txtLastName.getText()+"', FirstName = '"+txtFirstName.getText()+"', Gender = '"+txtGender.getSelectedItem().toString()+"', Dob = '"+date+"', Address = '"+txtAddress.getText()+"' WHERE Employee_ID = '"+txtEmployee_ID.getText()+"'";
               //PreparedStatement pst = con.prepareStatement(query); 
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery(query);
               //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               //String date = sdf.format(txtDob.getDate());           
//               pst.setString(1, txtLastName.getText());
//               pst.setString(2, txtFirstName.getText());
//               String Gender = txtGender.getSelectedItem().toString();
//               pst.setString(3, Gender);     
//               pst.setString(4, date);
//               pst.setString(5, txtAddress.getText());
//               pst.setBytes(6, person_image);
//               pst.setString(6, txtEmployee_ID.getText());
//               pst.executeUpdate();
               
               
               userList = userList();
               
               DefaultTableModel dm = (DefaultTableModel)TableDisplayEmp.getModel();
                while(dm.getRowCount() > 0)
                {
                    dm.removeRow(0);
                }
                
                show_user(userList);
               JOptionPane.showMessageDialog(this,"Save Successfully!!!");
            } catch (Exception e){
                //System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(this,e.getMessage());
            }
    }//GEN-LAST:event_btnSaveActionPerformed
    //DefaultListModel mod = new DefaultListModel();
    
    private String getAutoIDEmployee(String adminID) throws SQLException{ 
        String IDEmploy = null;
        String Dep =  adminID.substring(2); //  ADRD.TA
        IDEmploy += "EM" + Dep;
        
        // truy van lay thang ID cuoi cung theo phong ban
        
        Connection con = DriverManager.getConnection(url,user,pass);
        String query = "select TOP 1 * from Employee where Employee.Admin_ID LIKE '%"+Dep+"%' order by Employee.Employee_ID desc";
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        rs.next();
        String IDLastFromDB = rs.getString(1);
        
        int IDNext = Integer.parseInt(IDLastFromDB.substring(8));
        String kq = ""; 
        if (IDNext < 10){ 
            kq += "00" + ++IDNext;
        } else kq += "0" + ++IDNext;
        return IDLastFromDB.substring(0, 7) + kq;
    }
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
                
            try{
               String EmployeeID = getAutoIDEmployee(txtAdmin_ID.getText());
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               getAutoIDEmployee(txtAdmin_ID.getText());
               String query = "INSERT INTO Employee (Employee_ID, Admin_ID, LastName, FirstName, Dob, Gender, Address, Employee_Image) VALUES(?,?,?,?,?,?,?,?)";
               PreparedStatement pst = con.prepareStatement(query);
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               String date = sdf.format(txtDob.getDate());
               
               pst.setString(1, EmployeeID);
               pst.setString(2, txtAdmin_ID.getText());
               pst.setString(3, txtLastName.getText());
               pst.setString(4, txtFirstName.getText());
               String Gender = txtGender.getSelectedItem().toString();
               pst.setString(6, Gender);      
               pst.setString(5, date);
               pst.setString(7, txtAddress.getText());             
               pst.setBytes(8, person_image);   
               
               pst.executeUpdate();
               //if(checkInsert > 0) JOptionPane.showMessageDialog(this, "Them thanh cong");
               pst.close();
               
               String query1 = "INSERT INTO Em_Contact (Contact_ID,Phone,Email, Employee_ID) VALUES (?,?,?,?)";
               PreparedStatement pst1 = con.prepareStatement(query1);
               pst1.setString(4, EmployeeID);
               pst1.setString(2, txtContact.getText());
               pst1.setString(3, txtEmail.getText());
               pst1.setString(1, EmployeeID + ".CO");
               pst1.executeUpdate();   
               pst1.close();
               
               String query2 = "INSERT INTO login (Username) VALUES (?)";
               PreparedStatement pst2 = con.prepareStatement(query2);
               pst2.setString(1, EmployeeID);
               pst2.executeUpdate(); 
               pst2.close();
               userList = userList();
               DefaultTableModel model = (DefaultTableModel)TableDisplayEmp.getModel();
               model.setRowCount(0);
               show_user(userList);
               JOptionPane.showMessageDialog(this,"Successfully!!!");
            } catch (Exception e){
                //System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(this, e);
            }

         //add item
        //mod.addElement
        //mod.addElement(FullName); 
        //JList listtd = new JList(mod);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        int opt = JOptionPane.showConfirmDialog(null, "Are you sure to remove?","Remove", JOptionPane.YES_NO_OPTION);
        if (opt == 0){
        try{
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass); 
                if(  TableDisplayEmp.getSelectedRow() > 0 ){
                   rowSelected = TableDisplayEmp.getSelectedRow();
               }
               String value = (TableDisplayEmp.getModel().getValueAt(rowSelected, 0).toString());
               String query = "DELETE Em_Contact WHERE Employee_ID = ?";
               PreparedStatement pst = con.prepareStatement(query);            
               pst.setString(1, txtEmployee_ID.getText());
               int check = pst.executeUpdate();
               if(check > 0){
                   String query1 = "DELETE Employee WHERE Employee_ID = ?";
                   PreparedStatement pst1 = con.prepareStatement(query1); 
                   pst1.setString(1, txtEmployee_ID.getText());
                   pst1.executeUpdate();
               }
               String query2 = "DELETE login WHERE Username = ?";
                   PreparedStatement pst2 = con.prepareStatement(query2); 
                   pst2.setString(1, txtEmployee_ID.getText());
                   pst2.executeUpdate();
                userList = userList();
               DefaultTableModel model = (DefaultTableModel)TableDisplayEmp.getModel();
               model.setRowCount(0);
               show_user(userList);
               JOptionPane.showMessageDialog(this,"Remove Successfully!!!");
            } catch (Exception e){
                JOptionPane.showMessageDialog(this,e.getMessage());
            }
        }
        
    }//GEN-LAST:event_btnRemoveActionPerformed

    public void reset(){
        txtFirstName.setText("");
        txtLastName.setText("");
        txtDob.setDate(null);
        txtContact.setText("");
        txtEmail.setText("");
        txtEmployee_ID.setText("");
        txtAdmin_ID.setText("");
        txtAddress.setText("");  
        txtSearch.setText("");
        ImageEmployee.setIcon(null);
    }
    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        reset();     
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add r handling code here:
       // String Contact = txtContact.getText();       
       LoginAdmin LA = new LoginAdmin();
       LA.setVisible(true);
       setVisible(false);

    }//GEN-LAST:event_btnBackActionPerformed

    private void txtContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContactActionPerformed
        // TODO add your handling code here:
       // String Contact = txtContact.getText();
    }//GEN-LAST:event_txtContactActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
        //String Email = txtEmail.getText();
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtEmployee_IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployee_IDActionPerformed
        // TODO add your handling code here:
        //String ID = txtEmployee_ID.getText();
    }//GEN-LAST:event_txtEmployee_IDActionPerformed

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
       // String Address = txtAddress.getText();
    }//GEN-LAST:event_txtAddressActionPerformed

    private void txtFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
        //String FirstName = txtFirstName.getText();
    }//GEN-LAST:event_txtFirstNameActionPerformed

    private void AddImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddImageActionPerformed
        // TODO add your handling code here:
       JFileChooser chooser = new JFileChooser();
       chooser.showOpenDialog(null);
       File f = chooser.getSelectedFile();
       filename = f.getAbsolutePath();
       ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(ImageEmployee.getWidth(), ImageEmployee.getHeight(), Image.SCALE_SMOOTH));
       ImageEmployee.setIcon(imageIcon);
       try {
           File image = new File(filename);
           FileInputStream fis = new FileInputStream(image);
           ByteArrayOutputStream bos = new ByteArrayOutputStream();
           byte[] buf = new byte[1024];
           for(int readNum; (readNum = fis.read(buf))!=-1;){
               bos.write(buf, 0 , readNum);
               
           }
           person_image = bos.toByteArray();
       } catch(Exception e){
           JOptionPane.showMessageDialog(null, e);
       }
    }//GEN-LAST:event_AddImageActionPerformed

    private void btnInfomationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfomationActionPerformed
        // TODO add your handling code here:
        Employee.setVisible(true);
        Attendance.setVisible(false);
        Salary.setVisible(false);
        Payroll.setVisible(false);
        SearchEmp.setVisible(false);
    }//GEN-LAST:event_btnInfomationActionPerformed

    private void btnAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttendanceActionPerformed
        // TODO add your handling code here:
        Attendance.setVisible(true);
        Employee.setVisible(false);
        Salary.setVisible(false);
        Payroll.setVisible(false);
        SearchEmp.setVisible(false);
    }//GEN-LAST:event_btnAttendanceActionPerformed

    private void btnPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayrollActionPerformed
        // TODO add your handling code here:
        Salary.setVisible(true);
        Employee.setVisible(false);
        Attendance.setVisible(false);
        Payroll.setVisible(false);
        SearchEmp.setVisible(false);
        //TableDisplayEmp.setVisible(false);
    }//GEN-LAST:event_btnPayrollActionPerformed

    private void TableDisplayEmpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableDisplayEmpMouseClicked
        // TODO add your handling code here:
        try{
            
        }catch(Exception e){
            
        }
        int i = TableDisplayEmp.getSelectedRow();
        TableModel model = TableDisplayEmp.getModel();
        txtFirstName.setText(model.getValueAt(i,3).toString());
        txtLastName.setText(model.getValueAt(i, 2).toString());
        String Gender = model.getValueAt(i,4).toString();
            switch(Gender){
                case "Male":
                    txtGender.setSelectedIndex(0);
                    break;
                case "Female":
                    txtGender.setSelectedIndex(1);
                    break;
                case "Other":
                    txtGender.setSelectedIndex(2);
                    break;
            }
        //txtDob.setText(model.getValueAt(i,3).toString());
        txtAddress.setText(model.getValueAt(i,6).toString());
        txtEmployee_ID.setText(model.getValueAt(i,0).toString());
        txtAdmin_ID.setText(model.getValueAt(i,1).toString());
        txtEmail.setText(model.getValueAt(i,8).toString());
        txtContact.setText(model.getValueAt(i,7).toString());
        //txtSearch1.setText(model.getValueAt(i, 0).toString());
         try {
//             int  srow = TableDisplayEmp.getSelectedRow();
//             Date date = (Date) new SimpleDateFormat("yyyy-MM-dd").parse((String)model.getValueAt(srow, 5));
     //        System.out.println(model.getValueAt(i,5).toString());
      //  Date date = (Date) new SimpleDateFormat("yyyy-MM-dd").parse((String)model.getValueAt(i, 5));
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse((String) model.getValueAt(i, 5));
            // System.out.println(date1);
        txtDob.setDate(date1);
        byte[] img = (userList.get(i).getE_Image()); // what Nhật Anh =)) đoạn này biểu sao ko sai, 
        if(img == null){
           ImageEmployee.setIcon(null);
        }
        else {
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(ImageEmployee.getWidth(), ImageEmployee.getHeight(), Image.SCALE_SMOOTH));
        ImageEmployee.setIcon(imageIcon);    
        }     
                    
       } catch(Exception e){
       //    System.out.println(e);
           JOptionPane.showMessageDialog(null, e.getMessage());
       }
    }//GEN-LAST:event_TableDisplayEmpMouseClicked

    private void txtLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLastNameActionPerformed
        // TODO add your handling code here:
        //String LastName = txtLastName.getText();
    }//GEN-LAST:event_txtLastNameActionPerformed

    private void txtAdmin_IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdmin_IDActionPerformed
        // TODO add your handling code here:
        //String Admin_ID = txtAdmin_ID.getText();
    }//GEN-LAST:event_txtAdmin_IDActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        
         String search = txtSearch.getText();
        try{
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               //getAutoIDEmployee(txtAdmin_ID.getText());
               String query = "select E.Employee_ID, E.Admin_ID, E.LastName, E.FirstName, E.Gender, E.DoB, E.Address, EC.Phone, EC.Email,Adminm.Department, E.Employee_Image  from Employee AS E, Adminm , Em_Contact AS EC where E.Admin_ID = Adminm.Admin_ID AND EC.Employee_ID = E.Employee_ID AND E.Employee_ID LIKE '%"+search+"%'";
               PreparedStatement pst = con.prepareStatement(query);
               //pst.setString(1, txtSearch.getText());
               ResultSet rs = pst.executeQuery();
               userList = new ArrayList<>();
               User user;
               while(rs.next()){
       
                   user = new User(rs.getString("Employee_ID"), rs.getString("Admin_ID"), rs.getString("LastName"), rs.getString("FirstName"), rs.getString("Gender"), rs.getString("Dob"), rs.getString("Address"), rs.getString("Phone"), rs.getString("Email"), rs.getBytes("Employee_Image"));
                   userList.add(user);
               }
               
               DefaultTableModel dm = (DefaultTableModel)TableDisplayEmp.getModel();
                while(dm.getRowCount() > 0)
                {
                    dm.removeRow(0);
                }
 
               show_user(userList);
   
              
            } catch (Exception e){
                //System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, e);
            }
        
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearch1ActionPerformed
        // TODO add your handling code here:
        show_attendance();
        //String search1 = txtSearch1.getText();
        try{    
               show_attendance();
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               //getAutoIDEmployee(txtAdmin_ID.getText());
               String query = "select a.Attend_ID, e.Employee_ID, e.LastName, e.FirstName, a.Month, a.Date from Employee as e, Attendance as a where e.Employee_ID = a.Employee_ID AND e.Employee_ID LIKE '%"+txtSearch1.getText()+"%'";
               Statement st = con.createStatement();
               //pst.setString(1, txtSearch1.getText());
               ResultSet rs = st.executeQuery(query);
               TableDisplayAttend.setModel(DbUtils.resultSetToTableModel(rs));
              
            } catch (Exception e){
                //System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, e);
            }
    }//GEN-LAST:event_txtSearch1ActionPerformed

    private void txtSearch1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearch1KeyReleased
        // TODO add your handling code here:
//        show_attendance();
//        //String search1 = txtSearch1.getText();
//        try{    
//               show_attendance();
//               Class.forName(driver);
//               Connection con = DriverManager.getConnection(url,user,pass);
//               //getAutoIDEmployee(txtAdmin_ID.getText());
//               String query = "select a.Attend_ID, e.Employee_ID, e.LastName, e.FirstName, a.Month, a.Date from Employee as e, Attendance as a where e.Employee_ID = a.Employee_ID AND e.Employee_ID LIKE '%"+txtSearch1.getText()+"%'";
//               Statement st = con.createStatement();
//               //pst.setString(1, txtSearch1.getText());
//               ResultSet rs = st.executeQuery(query);
//               TableDisplayAttend.setModel(DbUtils.resultSetToTableModel(rs));
//              
//            } catch (Exception e){
//                //System.out.println(e.getMessage());
//                JOptionPane.showMessageDialog(null, e);
//            }
    }//GEN-LAST:event_txtSearch1KeyReleased
   
    private void txtSearch1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearch1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearch1KeyTyped

    private void txtSearch1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearch1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearch1KeyPressed

    @SuppressWarnings("empty-statement")
    private void txtSearch2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearch2KeyReleased
        // TODO add your handling code here:
        show_salary();
        String search2 = txtSearch2.getText();
        try{
            
             //Class.forName(driver);
             Connection con = DriverManager.getConnection(url,user,pass);
             String query = " WITH Total AS (\n" +
"              SELECT COUNT(Month) as total, Employee_ID, month\n" +
"              FROM Attendance\n" +
"              GROUP BY Employee_ID, month\n" +
"			  )\n" +
"			  SELECT Salary.Employee_ID, Salary.Salary_Amount, Salary.Bonus, Salary.Allowance, (total * Salary_Amount + Bonus + Allowance ) as Salary_per_month ,Salary.Month\n" +
"			  FROM Salary\n" +
"			  INNER JOIN Total\n" +
"			  ON Salary.Employee_ID = Total.Employee_ID AND Total.month = Salary.Month\n" +
"			  WHERE Salary.Employee_ID LIKE '%"+search2+"%'";
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query);
             TableDisplaySalary.setModel(DbUtils.resultSetToTableModel(rs));
             
        } catch (Exception e){
           JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtSearch2KeyReleased

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // TODO add your handling code here:
 
    }//GEN-LAST:event_txtSearchKeyPressed

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txtSearchMouseClicked
               
    private void btnAddSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSalaryActionPerformed
        // TODO add your handling code here:
               String EmpID = txtEmpID.getText();
               String Month = txtMonth.getText();
        try{     
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               String query = "INSERT INTO Salary (Salary_ID, Employee_ID, Salary_Amount, Bonus, Allowance, Month) VALUES(?,?,?,?,?,?)";
               PreparedStatement pst = con.prepareStatement(query);                             
               pst.setString(1, txtEmpID.getText()+"."+txtMonth.getText());
               pst.setString(2, txtEmpID.getText());
               pst.setString(3, txtSalary.getText());
               pst.setString(4, txtBonus.getText());
               pst.setString(5, txtAllowance.getText());             
               pst.setString(6, txtMonth.getText());   
               int i = pst.executeUpdate();
               DefaultTableModel model = (DefaultTableModel)TableDisplaySalary.getModel();
               
               model.setRowCount(0);
               show_salary();
               pst.close();
               show_result();
               //show_user(userList);
               JOptionPane.showMessageDialog(this,"Successfully!!!");
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, e);
            }        
    }//GEN-LAST:event_btnAddSalaryActionPerformed
    
    public void show_result(){             
               String EmpID = txtEmpID.getText();
               String Month = txtMonth.getText();
        try{     
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               String query1 = "WITH Total AS (\n" +
"              SELECT COUNT(Month) as total, Employee_ID, month\n" +
"              FROM Attendance\n" +
"              GROUP BY Employee_ID, month\n" +
"			  )\n" +
"			  SELECT Salary.Employee_ID, Salary.Salary_Amount, Salary.Bonus, Salary.Allowance, (total * Salary_Amount + Bonus + Allowance ) as Salary_per_month ,Salary.Month\n" +
"			  FROM Salary\n" +
"			  INNER JOIN Total\n" +
"			  ON Salary.Employee_ID = Total.Employee_ID AND Total.month = Salary.Month\n" +
"			  WHERE Salary.Employee_ID LIKE '%"+EmpID+"%' AND Total.month = '"+Month+"'";
               PreparedStatement pst1 = con.prepareStatement(query1); 
               //pst1.setString(1, txtEmpID.getText());
               //pst1.setString(2, txtMonth.getText());
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery(query1);
               while(rs.next()){
                   //System.out.print(rs);
                   txtResult.setText(rs.getString("Salary_per_month"));
               }              
        } catch (Exception e){
                //System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(this, e);
            }        
    }
    private void txtBonusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBonusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBonusActionPerformed

    private void btnSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryActionPerformed
        // TODO add your handling code here:
        Payroll.setVisible(true);
        Salary.setVisible(false);
        Employee.setVisible(false);
        Attendance.setVisible(false);
        SearchEmp.setVisible(false);
    }//GEN-LAST:event_btnSalaryActionPerformed

    private void txtResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtResultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtResultActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        txtEmpID.setText("");
        txtSalary.setText("");
        txtBonus.setText("");
        txtAllowance.setText("");
        txtMonth.setText("");
        txtResult.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtSearch2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearch2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearch2ActionPerformed

    private void btnSearchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchEmpActionPerformed
        // TODO add your handling code here:
        Payroll.setVisible(false);
        Salary.setVisible(false);
        Employee.setVisible(false);
        Attendance.setVisible(false);
        SearchEmp.setVisible(true);
        
    }//GEN-LAST:event_btnSearchEmpActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String Search = txtSearchS.getText();
         try{    
            if (RbNoProtection.isSelected()){
               //Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               String query = "select a.Attend_ID, e.Employee_ID, e.LastName, e.FirstName, a.Month, a.Date from Employee as e, Attendance as a where e.Employee_ID = a.Employee_ID AND e.Employee_ID = '"+Search+"'";
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery(query);
               
               StringBuilder results = new StringBuilder();
               ResultSetMetaData metaData = rs.getMetaData();
               int numberOfColumns = metaData.getColumnCount();
//                for (int i = 1; i <= numberOfColumns; i++) {
//                    results.append(metaData.getColumnName(i)).append("            \t");
//                }
//                results.append("\n");
                while (rs.next()) {
                    for (int i = 1; i <= numberOfColumns; i++) {
                        results.append(rs.getObject(i)).append("\t");
                    }  
                    results.append("\n");
                }
                txtAreaSearch.setText(results.toString());
                    con.close();
                }
            if (RbProtection.isSelected()){
                //Class.forName(driver);
                Connection con = DriverManager.getConnection(url,user,pass);
                String query = "select a.Attend_ID, e.Employee_ID, e.LastName, e.FirstName, a.Month, a.Date from Employee as e, Attendance as a where e.Employee_ID = a.Employee_ID AND e.Employee_ID = ? ";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, txtSearchS.getText());
                ResultSet rs = pst.executeQuery();
                StringBuilder results = new StringBuilder();
               ResultSetMetaData metaData = rs.getMetaData();
               int numberOfColumns = metaData.getColumnCount();
//                for (int i = 1; i <= numberOfColumns; i++) {
//                    results.append(metaData.getColumnName(i)).append("            \t");
//                }
//                results.append("\n");
                while (rs.next()) {
                    for (int i = 1; i <= numberOfColumns; i++) {
                        results.append(rs.getObject(i)).append("\t");
                    }  
                    results.append("\n");
                }
                txtAreaSearch.setText(results.toString());
                con.close();
            }
              
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
    }//GEN-LAST:event_btnSearchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddImage;
    private javax.swing.JPanel Attendance;
    private javax.swing.JPanel Employee;
    private javax.swing.JLabel ImageEmployee;
    private javax.swing.JPanel Payroll;
    private javax.swing.JRadioButton RbNoProtection;
    private javax.swing.JRadioButton RbProtection;
    private javax.swing.JPanel Salary;
    private javax.swing.JPanel SearchEmp;
    private javax.swing.JTable TableDisplayAttend;
    private javax.swing.JTable TableDisplayEmp;
    private javax.swing.JTable TableDisplaySalary;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddSalary;
    private javax.swing.JButton btnAttendance;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnInfomation;
    private javax.swing.JButton btnPayroll;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSalary;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchEmp;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbAddress;
    private javax.swing.JLabel lbAdmin_ID;
    private javax.swing.JLabel lbContact;
    private javax.swing.JLabel lbDob;
    private javax.swing.JLabel lbEmail;
    private javax.swing.JLabel lbEmployee_ID;
    private javax.swing.JLabel lbFullname;
    private javax.swing.JLabel lbGender;
    private javax.swing.JLabel lbLastName;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAdmin_ID;
    private javax.swing.JTextField txtAllowance;
    private javax.swing.JTextArea txtAreaSearch;
    private javax.swing.JTextField txtBonus;
    private javax.swing.JTextField txtContact;
    private com.toedter.calendar.JDateChooser txtDob;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtEmployee_ID;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JComboBox<String> txtGender;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMonth;
    private javax.swing.JTextField txtResult;
    private javax.swing.JTextField txtSalary;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearch1;
    private javax.swing.JTextField txtSearch2;
    private javax.swing.JTextField txtSearchS;
    // End of variables declaration//GEN-END:variables
}
