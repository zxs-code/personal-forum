package com.github.code.zxs.resource.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution3 {
    private int minScore = Integer.MAX_VALUE;

    public int minimumScore(int[] nums, int[][] edges) {
        Map<Integer, List<Integer>> nextMap = new HashMap<>();
        for (int[] edge : edges) {
            int min = Math.min(edge[0], edge[1]);
            int max = Math.max(edge[0], edge[1]);
            nextMap.putIfAbsent(min, new ArrayList<>());
            nextMap.get(min).add(max);
        }
        List<Integer> list = new ArrayList<>();
        dfs(0,nextMap, nums[0],list);
        list.add(nums[0]);
        dfs(0,nextMap,0,list);
        return minScore;
    }

    public void dfs(Integer node, Map<Integer, List<Integer>> nextMap, int xor, List<Integer> result) {
        if (result.size() == 3) {
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            for (int num : result) {
                max = Math.max(max, num);
                min = Math.min(min, num);
            }
            minScore = Math.min(max - min, minScore);
            return;
        }

        if (!nextMap.containsKey(node))
            return;
        for (int next : nextMap.get(node)) {
            dfs(next, nextMap, xor ^ next, result);
            result.add(xor);
            dfs(next, nextMap, 0, result);
            result.remove(result.size() - 1);
        }
    }

}