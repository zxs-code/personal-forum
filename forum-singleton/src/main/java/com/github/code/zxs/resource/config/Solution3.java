package com.github.code.zxs.resource.config;

class Solution3 {
    public int idealArrays(int n, int maxValue) {
        int mod = (int) 1e9 + 7;
        int[] dp = new int[maxValue + 1];
        int[][] matrix = new int[maxValue + 1][maxValue + 1];
        for (int j = 1; j <= maxValue; j++) {
            int k = 1;
            while (k * j <= maxValue) {
                matrix[j][k * j] = 1;
                k++;
            }
        }

        for (int j = 1; j <= maxValue; j++)
            dp[j] = 1;

        int[][] ints = matrixPower(matrix, n - 1, mod);
        int[] result = new int[maxValue + 1];
        for (int j = 1; j <= maxValue; j++) {
            for (int k = 1; k <= maxValue; k++) {
                result[j] = (result[j] + (dp[j] * ints[k][j] % mod)) % mod;
            }
        }
        int sum = 0;
        for (int num : result)
            sum = (sum + num) % mod;
        return sum;
    }

    public int[][] matrixPower(int[][] matrix, int n, int mod) {
        int length = matrix.length;
        if (n == 1)
            return matrix;
        int[][] m1, m2;
        if (n % 2 == 0) {
            m1 = matrixPower(matrix, n / 2, mod);
            m2 = m1;
        } else {
            m1 = matrixPower(matrix, n - 1, mod);
            m2 = matrix;
        }
        int[][] result = new int[length][length];
        for (int i = 1; i < length; i++)
            for (int j = 1; j < length; j++)
                result[i][j] = (result[i][j] + (m1[1][i] * m2[j][1]) % mod) % mod;
        return result;
    }

    public static void main(String[] args) {
        int i = new Solution3().idealArrays(15, 943);
        System.out.println(i);
    }
}