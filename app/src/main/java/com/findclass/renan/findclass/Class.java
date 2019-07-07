package com.findclass.renan.findclass;

public class Class {

    String mClassNumber;
    String mVacant;
    String mInsideuilding;
    String mHours;
    String mUser;

    public Class(String mClassNumber, String mVacant, String mInsideuilding,String mHours,String mUser) {
        this.mClassNumber = mClassNumber;
        this.mVacant = mVacant;
        this.mInsideuilding = mInsideuilding;
        this.mHours = mHours;
        this.mUser = mUser;
    }

    public String getmClassNumber() {
        return mClassNumber;
    }

    public String getmVacant() {
        return mVacant;
    }

    public String getmInsideuilding() {
        return mInsideuilding;
    }

    public String getmHours() {
        return mHours;
    }

    public String getmUser() {
        return mUser;
    }

}
