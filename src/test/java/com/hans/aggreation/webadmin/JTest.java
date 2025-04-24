package com.hans.aggreation.webadmin;

import org.junit.jupiter.api.Test;

import java.util.TimeZone;

public class JTest {

    @Test
    public void test() {
        String s= null;
        System.out.println(TimeZone.getTimeZone("UTC"));
        System.out.println(TimeZone.getTimeZone(""));

    }
}
