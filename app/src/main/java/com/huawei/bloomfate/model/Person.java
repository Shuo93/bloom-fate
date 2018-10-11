package com.huawei.bloomfate.model;

public class Person {

   private PersonBasic basic;

   private Edu education;

   private Job occupation;

   public PersonBasic getBasic() {
      return basic;
   }

   public void setBasic(PersonBasic basic) {
      this.basic = basic;
   }

   public Edu getEducation() {
      return education;
   }

   public void setEducation(Edu education) {
      this.education = education;
   }

   public Job getOccupation() {
      return occupation;
   }

   public void setOccupation(Job occupation) {
      this.occupation = occupation;
   }
}
