package com.smile.tools.keyword;

public class FinalSingleSon extends FinalSingle {

    @Override
    public void addAge1(int num) {
        age1 += num;
    }

    //错误无法覆盖父类final方法
//    @Override
//    public void addAge2(int num) {
//        age2 += num;
//    }

}
