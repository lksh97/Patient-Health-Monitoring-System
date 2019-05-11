/*
Created By : Vaibhav Shukla
*/
package phms;

import java.io.InputStream;

public class ModelTable {
    String first,last, gender, mobile, occupation, address;
    int id;

    public ModelTable(int id, String first, String last, String gender, String mobile, String occupation, String address) {
        this.first = first;
        this.last = last;
        this.gender = gender;
        this.mobile = mobile;
        this.occupation = occupation;
        this.address = address;
        this.id = id;
    }

    ModelTable(int aInt, String string, String string0, String string1, String string2, String string3, String string4, InputStream binaryStream) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
   
}
