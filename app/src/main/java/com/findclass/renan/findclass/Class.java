package com.findclass.renan.findclass;

public class Class {

    String mClassNumber;
    String mVacant;
    String mInsideuilding;
    String mHours;

    public Class(String mClassNumber, String mVacant, String mInsideuilding,String mHours) {
        this.mClassNumber = mClassNumber;
        this.mVacant = mVacant;
        this.mInsideuilding = mInsideuilding;
        this.mHours = mHours;
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
}
