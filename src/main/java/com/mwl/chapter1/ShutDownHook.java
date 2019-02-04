package com.mwl.chapter1;

import java.util.concurrent.TimeUnit;

public class ShutDownHook {
    public static void main(String[] args) throws  Exception{
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("shutdownhook execute start...");
            System.out.println("Netty NioEventLoop shutdown");
            try {
                TimeUnit.SECONDS.sleep(3);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("shudownhook end...");
        },""));
        TimeUnit.SECONDS.sleep(7);
        System.exit(0);
    }
}
