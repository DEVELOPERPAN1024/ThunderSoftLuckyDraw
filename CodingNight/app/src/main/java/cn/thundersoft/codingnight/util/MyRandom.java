package cn.thundersoft.codingnight.util;

import java.util.ArrayList;
import java.util.List;

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

}