package org.example;

import java.lang.reflect.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        String className = "org.example.TestClass";

        Class<?> clazz = Class.forName(className);
        System.out.println( clazz.getName());

        Method[] methods = clazz.getDeclaredMethods();
        System.out.println("\nMethods:");
        for (Method m : methods) {
            System.out.println(" - " + m.getName() + "(" +
                    Arrays.toString(m.getParameterTypes()) + ") : " + m.getReturnType().getSimpleName());
        }

        System.out.println("\n @Test methods...");
        for (Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                if (Modifier.isStatic(m.getModifiers()) && m.getParameterCount() == 0) {
                    System.out.println("Invoking: " + m.getName());
                    m.invoke(null);
                } else {
                    System.out.println( m.getName() + " (must be static and no-args)");
                }
            }
        }
    }
}
