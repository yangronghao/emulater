package com.company;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {
    public static int MOD = (int) (1e9 + 7);

    public static void main(String[] args) {
        // write your code here

        String s = "fqtvkfkt";
        String[] queries = {"cha", "r", "act", "ers"};
        String[] words = {"a", "aa", "aa", "aaaa"};
        List<List<Integer>> pairs = new ArrayList<>();
        List<Integer> pairs1 = new ArrayList<>();
        List<Integer> pairs2 = new ArrayList<>();
        List<Integer> pairs3 = new ArrayList<>();
        List<Integer> pairs4 = new ArrayList<>();
        List<Integer> pairs5 = new ArrayList<>();
        List<Integer> pairs6 = new ArrayList<>();
        List<Integer> pairs7 = new ArrayList<>();
        List<String> pairs8 = new ArrayList<>();
        pairs1.add(2);
        pairs1.add(4);
        pairs2.add(5);
        pairs2.add(7);
        pairs3.add(1);
        pairs3.add(0);
        pairs4.add(0);
        pairs4.add(0);
        pairs5.add(4);
        pairs5.add(7);
        pairs6.add(0);
        pairs7.add(4);
        pairs7.add(1);
        pairs8.add("cha");
        pairs8.add("r");
        pairs8.add("act");
        pairs8.add("ers");
        pairs6.add(3);
        pairs.add(pairs1);
        pairs.add(pairs2);
        pairs.add(pairs3);
        pairs.add(pairs4);
        pairs.add(pairs5);
        pairs.add(pairs6);
        pairs.add(pairs7);

        int num[][] = {{10, 50}, {60, 120}, {140, 210}};
        int nu[] = {1, 2, 5, 7, 9};
        int n[] = {1, 2, 3};
        int num2[][] = {{0, 15}};
        int a = -2147483648;
        int b = 1 << 31;
        System.out.println(b);
        System.out.println(a);
        System.out.println(a - 1);

        // System.out.println(subsets(n));
        System.out.println(MOD);

    }
}

