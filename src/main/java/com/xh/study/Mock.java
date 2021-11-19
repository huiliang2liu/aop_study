package com.xh.study;

import org.mockito.Mockito;

public class Mock {
    public static void main(String[] args) {
        Runnable runnable = Mockito.mock(Runnable.class);
        System.out.println(runnable.getClass());

    }
}
