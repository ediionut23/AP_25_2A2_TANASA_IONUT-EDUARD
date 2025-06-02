package org.example;

public class TestClass {
    public static void main(String[] args) {
        System.out.println("Hello from TestClass!");
    }

    @Test
    public static void test1() {
        System.out.println("Test 1 executed!");
    }

    @Test
    public static void test2() {
        System.out.println("Test 2 executed!");
    }

    public static void notATest() {
        System.out.println("Not a test");
    }
}
