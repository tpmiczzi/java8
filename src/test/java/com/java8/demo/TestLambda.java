package com.java8.demo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestLambda extends DemoApplicationTests {

    @Test
    public void testLambdaTwoParams() {
        Map<String, String> map = new HashMap() {{
            put("first", "100");
            put("second", "200");
            put("third", "300");
        }};

        Map<String, Integer> newMap = new HashMap<>();

        List<Long> list = new ArrayList<>();

        map.forEach((key, value) -> newMap.put(key, Integer.valueOf(value)));

        newMap.forEach((key, value) -> System.out.println(key + " - " + value));

        map.forEach((key, value) -> list.add(Long.valueOf(value)));

        list.forEach(value -> System.out.println(value));
    }
}
