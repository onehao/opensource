package com.onehao.acts.testgen.service.engine;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

class ForbesMath {
    private static int[][] binom;
    private static int maxk;

    public static void initialize(int new_size, int new_maxk) {
        maxk = new_maxk + 1;
        binom = new int[new_size + 1][];

        binom[0] = new int[1];
        binom[0][0] = 1;
        for (int i = 1; i < binom.length; i++) {
            binom[i] = new int[Math.min(i + 1, maxk)];
            for (int j = 0; j < Math.min(i, maxk); j++) {
                binom[i][j] += binom(i - 1, j);
                if (j + 1 < Math.min(i, maxk)) {
                    binom[i][(j + 1)] += binom(i - 1, j);
                }
            }
        }
    }

    public static int binom(int n, int k) {
        if (n < k) {
            return 0;
        }
        return binom[n][Math.min(k, n - k)];
    }
}
