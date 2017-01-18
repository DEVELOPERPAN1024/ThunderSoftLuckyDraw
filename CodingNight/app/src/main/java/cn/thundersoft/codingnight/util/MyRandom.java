package cn.thundersoft.codingnight.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.thundersoft.codingnight.models.Person;

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
//        double[] dd = new double[13];
//        for (int i = 0; i < dd.length; i++) {
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
        f = (int) (System.currentTimeMillis() % 1000);
        x = f % 13;
        changeR(r, 11 + ((f) / 7));
        double ran = rr;
        return ran;
    }


    public static List<Person> getRandomList(List<Person> allPersons, int count) {
        List<Person> list = new ArrayList<>();
        while (list.size() < count) {
            double rs = getRandom_0_1();
            int randomPosition = ((int) (rs * (allPersons.size()-1)));
            Person p = allPersons.get(randomPosition);
            list.add(p);
            allPersons.remove(p);
        }
        return list;
    }

    public static Person getRandomPersion(List<Person> allPersons) {
        double rs = getRandom_0_1();
        int randomPosition = ((int) (rs * (allPersons.size() - 1)));
        Person p = allPersons.get(randomPosition);
        allPersons.remove(p);
        return p;
    }

    public static List<Person> getRandomListFake(List<Person> allPersons, int count) {
        List<Person> list = new ArrayList<>();
        while (list.size() < count) {
            int f = (int) (System.currentTimeMillis() % 1000);
            int randomPosition = f % (allPersons.size() - 1);
            Person p = allPersons.get(randomPosition);
            list.add(p);
            allPersons.remove(p);
        }
        allPersons.addAll(list);
        return list;
    }

    /**
     * 计算红包
     * @param totalCount 红包总数
     * @param totalMoney 红包总金额
     * @return 计算好的红包列表
     */
    public static List<Integer> getMoneys(int totalCount, int totalMoney) {
        // 要确保总金额是偶数
        // 保证每人最少得到平均的20%
        double minPercent = 0.20f;
        int moneyForDraw = (int)(totalMoney * (1 - minPercent));
        int minMoney = (int)(totalMoney * minPercent / totalCount);
        List<Integer> moneys = new ArrayList<>();
        int count = totalCount;
        Random random = new Random();
        int randomMoney;
        while (count > 0) {
            if (count == 1) {
                moneys.add(moneyForDraw + minMoney); // 最后一次  取剩下的
            } else {
                // 取整 每次计算时，当前得到的最大值为当前平均数的2倍（不然后面计算的太吃亏，这样分布还比较正常）
                randomMoney = (int) (random.nextDouble() * (moneyForDraw / count * 2));
                moneys.add(randomMoney + minMoney);
                moneyForDraw -= randomMoney;
            }
            count--;
        }
        return moneys;
    }


}