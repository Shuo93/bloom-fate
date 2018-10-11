package com.huawei.bloomfate.model;

public class Edu extends Security {

    private String school;
    private String degree;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    enum Degree {
        BACHELOR,
        MASTER,
        DOCTOR
    }
}
