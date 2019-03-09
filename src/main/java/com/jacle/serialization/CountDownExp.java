package com.jacle.serialization;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用countdown来完成线程同步
 * 同步问题主要考虑有几个同步，就是用几个countdown
 */
public class CountDownExp
{
    public static void main(String[] args) throws InterruptedException {
        //开始的时候一次同步，结束的时候一次同步；
        CountDownLatch begin=new CountDownLatch(1);
        CountDownLatch end=new CountDownLatch(10);

        ExecutorService pool= Executors.newFixedThreadPool(10);

        for(int i=0;i<10;i++)
        {
            final int m=i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        begin.await();
                        Thread.sleep(new Random().nextInt(5000));
                        System.out.println(m+"arrived");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally
                    {
                        end.countDown();
                    }

                }
            });
        }

        begin.countDown();
        end.await();
        System.out.println("all running finished!");

    }
}
