package com.github.code.zxs.resource.config;

//  Definition for singly-linked list.
class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

class Solution2 {
    public int peopleAwareOfSecret(int n, int delay, int forget) {
        int mod = (int) 1e9 + 7;
        int[] total = new int[n];
        total[0] = 1;
        for (int i = 1; i < n; i++) {
            int add = 0;
            if (i >= delay) add += total[i - delay];
            if (i >= forget) add -= total[i - forget];
            total[i] = (total[i - 1] + add) % mod;
        }
        return total[n - 1] - total[n - forget];
    }

    public static void main(String[] args) {
        new Solution2().peopleAwareOfSecret(6, 2, 4);
    }

}