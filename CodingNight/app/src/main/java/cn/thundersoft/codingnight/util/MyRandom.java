package cn.thundersoft.codingnight.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pandroid on 11/26/16.
 */

public class MyRandom implements Runnable {

    private static int random;
    private static int f = 127;
    private static int m = (int) Math.pow(2, 16);
    private static int[] r = getR();
    private static int x = 13;

    @Override
    public void run() {
        for (; !Thread.interrupted(); ) {
            f = ((f / 2) + r[f]) % m;
            random = r[f];
        }
    }

    private static int[] getR() {
        // 将0-65536这65536个数按照一定顺序存入r[]中
        int[] r = new int[m];
        r[0] = 13849;
        for (int i = 1; i < m; i++) {
            r[i] = ((2053 * r[i - 1]) + 13849) % m;
        }
        int k = r[65535];
        r[65535] = r[(f + 1) % m];
        r[(f + 1) % m] = k;
        return r;
    }

    private static void changeR(int[] r, int f) {
        int[] r1 = new int[r.length];
        System.arraycopy(r, 0, r1, 0, r.length);
        for (int i = 0; i < r.length; i++) {
            r[i] = r1[(i + f) % m];
        }
    }

    public static double getRandom_0_1() {
        double[] dd = new double[13];
        for (int i = 0; i < dd.length; i++) {
            Runnable runnable = new MyRandom();
            Thread thread = new Thread(runnable);
            thread.start();
            try {
                Thread.sleep(x + 1);
            } catch (InterruptedException e) {
                e.getMessage();
            }
            thread.interrupt();
            double rr = (double) random / (double) m;
            x = f % 13;
            changeR(r, 11 + (f / 7));
            dd[i] = rr;
            if ((i > 0) && (dd[i] == dd[i - 1])) {
                changeR(r, 13 + (f / 11));
            }
        }
        double ran = dd[12];
        return ran;
    }

    public static List<Integer> getRandomList(int max, int count) {
        //默认需要重新查询
        boolean isReload = true;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            //除了第一次都需要重新reload
            if (i != 0) {
                isReload = false;
            }
            //获取随机数
            double rs = getRandom_0_1();
            int randomPosition = ((int) (rs * max));
            //check and 获取
            if (checkIfAlreadyLucky(randomPosition, isReload)) {
                i--;
                continue;
            } else {
                list.add(randomPosition);
            }
        }
        return list;
    }

    private static boolean checkIfAlreadyLucky(int position, boolean isReload) {
        List<Integer> mAlreadyLuckyList = new ArrayList<>();
        if (isReload) {
            //重新查询已经中奖的名单
            mAlreadyLuckyList.add(1);
        }
        for (int i = 0; i < mAlreadyLuckyList.size(); i++) {
            if (position == mAlreadyLuckyList.get(i)) {
                return true;
            }
        }
        return false;
    }
}