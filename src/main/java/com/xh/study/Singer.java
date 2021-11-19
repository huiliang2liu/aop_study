package com.xh.study;

public class Singer implements Singable {
    @Override
    public void sing() {
        System.out.println("I am singing...");
    }
}