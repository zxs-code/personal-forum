package com.github.code.zxs.resource.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution2 {
    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (o1, o2) -> o1[0] != o2[0] ? o2[0] - o1[0] : o1[1] - o2[1]);
        List<int[]> ints = new ArrayList<>();
        for (int[] arr : people) {
            ints.add(arr[1], arr);
        }
        return ints.toArray(new int[0][0]);
    }
}