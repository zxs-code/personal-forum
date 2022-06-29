package com.github.code.zxs.resource.config;

public class Solution {

    public int binarySearch(int[] arr, int target) {
        int start = 0, end = arr.length - 1;
        while (start <= end) {
            int middle = (start + end) / 2;
            if (arr[middle] == target)
                return middle;
            else if (arr[middle] > target)
                end = middle - 1;
            else
                start = middle + 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11};
        int i = new Solution().binarySearch(arr, 10);
        System.out.println(i);
        arr = new int[]{1, 3, 52, 345, 2434, 2436, 2436, 2356, 93343, 103433, 11343434};
        i = new Solution().binarySearch(arr, 7);
        System.out.println(i);
        i = new Solution().binarySearch(arr, 2434);
        System.out.println(i);
    }
}
