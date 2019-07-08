package com.findclass.renan.findclass;

public class Class {

    String mClassNumber;
    boolean mVacant;
    String mInsideuilding;
    String mHours;
    String mUser;

    public Class(String mClassNumber, boolean mVacant, String mInsideuilding,String mHours,String mUser) {
        this.mClassNumber = mClassNumber;
        this.mVacant = mVacant;
        this.mInsideuilding = mInsideuilding;
        this.mHours = mHours;
        this.mUser = mUser;
    }

    public String getmClassNumber() {
        return mClassNumber;
    }

    public boolean getmVacant() {
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
