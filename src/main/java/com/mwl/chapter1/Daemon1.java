package com.mwl.chapter1;

import java.util.concurrent.TimeUnit;

public class Daemon1 {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.DAYS.sleep(Long.MAX_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Daemon-T");
//        默认为false，程序不会退出。为false的时候，程序运行完存在非守护线程因此程序需要等待此程序退出才会退出
        t.setDaemon(true);
        t.start();
        TimeUnit.SECONDS.sleep(8);
        System.out.println("系统退出，程序执行" + (System.nanoTime() - startTime) / 1000 / 1000 / 1000 + " s");
    }
}
