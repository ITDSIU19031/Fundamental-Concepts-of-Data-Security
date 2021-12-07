/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLInjection;

/**
 *
 * @author Nhat Anh
 */
class User {
    private String Employee_ID, Admin_ID ,FirstName, LastName, Gender, Dob, Address, Contact, Email, Date;//, Department;
    private byte[] picture;
    
    public User(String Employee_ID, String Admin_ID, String FirstName, String LastName, String Gender, String Dob, String Address,String Contact,String Email, byte[] E_Image){
        this.Address = Address;
        this.Contact = Contact;
        this.Dob = Dob;
        this.Email = Email;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Gender = Gender;
        this.Admin_ID = Admin_ID;
        this.Employee_ID = Employee_ID;
        //this.Department = Department;
        this.picture = E_Image;
    }
    public String getAdmin_ID(){
        return Admin_ID;
    }
    public String getEmployee_ID(){
        return Employee_ID;
    }
    public String getAddress(){
        return Address;
    }
    public String getContact(){
        return Contact;
    }
    public String getDob(){
        return Dob;
    }
    public String getEmail(){
        return Email;
    }
    public String getLastName(){
        return LastName;
    }
    public String getFirstName(){
        return FirstName;
    }
    public String getGender(){
        return Gender;
    }
//    public String getDepartment(){
//        return Department;
//    }
    public byte[] getE_Image(){
        return picture;
    }
}
    
