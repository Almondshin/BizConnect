package com.bizconnect;

import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class CodeTest2 {

//    public static void main(String[] args) {
//        int[] height = {1,1,0,2,1,0,1,3,2,1,2,1};
//
//        CodeTest2 ct2 = new CodeTest2();
//        System.out.println(ct2.trap(height));
//    }

//    public int trap(int[] height){
//        int volume = 0;
//        int left = 0;
//        int right = height.length -1;
//
//        int leftMax = height[left];
//        int rightMax = height[right];
//
//
//        while (left < right){
//            leftMax = Math.max(height[left] , leftMax);
//            rightMax = Math.max(height[right], rightMax);
//
//            if(leftMax <= rightMax){
//                // 왼쪽 막대 최대 높이와의 차이를 더하고 왼쪽 포인터 이동
//                volume += leftMax - height[left];
//                left += 1;
//            } else {
//                volume += rightMax - height[right];
//                right -= 1;
//            }
//        }
//            return volume;
//    }


    public static void main(String[] args) {
        CodeTest2 ct = new CodeTest2();

        System.out.println("첫번째 : " + ct.soulution(3, new int[]{1, 2, 1, 3, 3, 2}));

        System.out.println(ct.soulution(4, new int[]{3, 3, 2, 2, 1, 1, 4, 4,}));
    }

    public int soulution(int n, int[] orders) {

        int answer = 0;

        int[][] position = new int[n+1][2];

        for (int i = 0; i < position.length; i++) {
            position[i][0] = -1;
            position[i][1] = -1;
        }

        for (int i = 0; i < orders.length; i++) {
            if (position[orders[i]][0] == -1){
                position[orders[i]][0] = i;
            } else {
                position[orders[i]][1] = i;
            }
        }

        boolean[] taken = new boolean[orders.length];
        for (int i = 0; i < orders.length; i++) {
            if (!taken[i]){
                int book = orders[i];
                int nextPosition = position[book][1];

                if (nextPosition != -1){
                    for (int j = i + 1; j <nextPosition; j++){
                        if (!taken[j]) {
                            answer++;
                        }
                    }
                    taken[nextPosition] = true;
                }
                taken[i] = true;
            }

        }

        answer %= 1000000000;

        return answer;

    }
}
