package com.bizconnect;

public class CodeTest {

    int index, maxLen;

    public static void main(String[] args) {

        int[][] a1 = new int[][]{{0, 1, 0}, {1, 1, 1}, {1, 1, 0}, {0, 1, 1}};

        CodeTest ct = new CodeTest();
        System.out.println(ct.longestPalindrome("tatttefetettte"));
    }

    public String longestPalindrome(String s) {
        int len = s.length();
        if (len < 2) {
            return s;
        }
        for (int i = 0; i < len - 1; i++) {
            find(s, i, i);   // 홀수
            find(s, i, i + 1); // 짝수
        }
        return s.substring(index, index + maxLen);
    }

    public void find(String s, int i, int j) {
        while (i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)) {
            i--;
            j++;
        }
        if (maxLen < j - i - 1) {
            index = i + 1;
            maxLen = j - i - 1;
        }
    }
}
