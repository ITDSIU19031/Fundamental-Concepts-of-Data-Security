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
class Salary {
    private String Employee_ID, Salary_Amount, Bonus, Allowance, Salary_per_month, Month;
    public Salary (String Employee_ID, String Salary_Amount, String Bonus, String Allowance, String Salary_per_month, String Month){
        this.Employee_ID = Employee_ID;
        this.Salary_Amount = Salary_Amount;
        this.Salary_per_month = Salary_per_month;
        this.Allowance = Allowance;
        this.Bonus = Bonus;
        this.Month = Month;
    }
    public String getEmployee_ID(){
        return Employee_ID;
    }
    public String getSalary_Amount(){
        return Salary_Amount;
    }
    public String getSalary_per_month(){
        return Salary_per_month;
    }
    public String getAllowance(){
        return Allowance;
    }
    public String getBonus(){
        return Bonus;
    }
    public String getMonth(){
        return Month;
    }
}
