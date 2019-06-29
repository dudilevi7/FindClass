package com.findclass.renan.findclass;

public class Class {

    String mClassNumber;
    String mVacant;
    String mInsideuilding;

    public Class(String mClassNumber, String mVacant, String mInsideuilding) {
        this.mClassNumber = mClassNumber;
        this.mVacant = mVacant;
        this.mInsideuilding = mInsideuilding;
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
}
