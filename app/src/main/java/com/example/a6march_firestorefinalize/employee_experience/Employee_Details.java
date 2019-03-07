package com.example.a6march_firestorefinalize.employee_experience;

public class Employee_Details {

    private String name;
    private String phone;
    private String my_score_ref;
    private String status;
    private String password;

    //constructor for firestore ui
    public Employee_Details() {
    }

    //constructor for out inside

    //this particular constructor for early setup, REGISTRATION
    public Employee_Details(String name, String phone, String my_score_ref) {
        this.name = name;
        this.phone = phone;
        this.my_score_ref = my_score_ref;
    }

    public Employee_Details(String name, String phone, String my_score_ref, String status, String password) {
        this.name = name;
        this.phone = phone;
        this.my_score_ref = my_score_ref;
        this.status = status;
        this.password = password;
    }

    ///2 constructors

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMy_score_ref() {
        return my_score_ref;
    }

    public void setMy_score_ref(String my_score_ref) {
        this.my_score_ref = my_score_ref;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
