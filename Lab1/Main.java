package com.cartof;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};
        int n = (int) (Math.random() * 1_000_000);
        n = n * 3;
        n = 0b10101 + n;
        n = 0xFF + n;
        n = n * 6;
        System.out.println(n);
        int sumCif = 0;
        while (n != 0) {
            sumCif=sumCif+n%10;
            n=n/10;
        }
        while(sumCif>10) {
            int tempSum=0;
            while(sumCif!=0) {
                tempSum=tempSum+sumCif%10;
                sumCif=sumCif/10;
            }
            sumCif=tempSum;
        }
        System.out.println(sumCif);
        System.out.println("Willy-nilly, this semester I will learn " + languages[sumCif]);
    }
}
