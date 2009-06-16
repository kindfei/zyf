package demo.terracota.futuretask;

/*
  All content copyright (c) 2003-2008 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main
{
    public static final Main instance = new Main();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private BlockingQueue<FutureTask> queue = new LinkedBlockingQueue<FutureTask>();

    public void listen() throws InterruptedException
    {
        while (true) { 
            queue.take().run();
        }
    } 

    public void run() throws Exception
    {
        if (lock.writeLock().tryLock()) { 
            System.out.println("Waiting...");
            listen();
            return;
        }
       
        FutureTask task = new FutureTask(new MyCallable());
        System.out.println("Submitting task...");
        queue.put(task);
        System.out.println("Waiting for response...");
        System.out.println("Task completed at: " + task.get().toString());
    }

    private static class MyCallable implements Callable
    {
         public Object call() throws InterruptedException
         {
             System.out.println(new Date().toString() + ": Sleeping 2 seconds...");
             Thread.sleep(2000);
             System.out.println("Hello world");

             return new Date();
         } 
    }

    public static void main(String[] args) throws Exception
    {
        instance.run();
    }
}
