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
class Attendance {
    private String Attend_ID, Employee_ID, Month, Date, LastName, FirstName;
    
    public Attendance(String Attend_ID, String Employee_ID, String Month, String Date, String LastName, String FirstName){
        this.Attend_ID = Attend_ID;
        this.Date = Date;
        this.Employee_ID = Employee_ID;
        this.Month = Month;
        this.FirstName = FirstName;
        this.LastName = LastName;
}
    public String getAttend_ID(){
        return Attend_ID;
    }
    public String getDate(){
        return Date;
    }
    public String getEmployee_ID(){
        return Employee_ID;
    }
    public String getMonth(){
        return Month;
    }
    public String getFirstName(){
        return FirstName;
    }public String getLastName(){
        return LastName;
    }
    
}
