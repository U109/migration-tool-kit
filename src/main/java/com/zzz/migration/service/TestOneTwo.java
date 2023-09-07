package com.zzz.migration.service;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author zhangzhongzhen wrote on 2023/8/26
 * @version 1.0
 */
public class TestOneTwo {
    static Semaphore semaphore = new Semaphore(1);
    static Semaphore semaphore2 = new Semaphore(0);

    public static void main(String[] args) {

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
        fixedThreadPool.execute(new Task1());
        fixedThreadPool.execute(new Task2());
        fixedThreadPool.shutdown();
    }

    static class Task1 implements Runnable{

        int i = 1;
        @SneakyThrows
        @Override
        public void run() {
            while (i < 100){
                semaphore.acquire();
                System.out.println(i);
                i=i+2;
                semaphore2.release();
            }

        }
    }

    static class Task2 implements Runnable{
        int i = 2;
        @SneakyThrows
        @Override
        public void run() {
            while (i <= 100){
                semaphore2.acquire();
                System.out.println(i);
                i=i+2;
                semaphore.release();
            }
        }
    }
}

