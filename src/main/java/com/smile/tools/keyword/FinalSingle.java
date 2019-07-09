package com.smile.tools.keyword;

public class FinalSingle {

    final int AGE = 10;

    int age1 = 5;

    int age2 = 10;

    public void addAge1(int num) {
        //错误
        //AGE = AGE + 10;
    }

    public final void addAge2(int num) {
        age2 += num;
    }

}
