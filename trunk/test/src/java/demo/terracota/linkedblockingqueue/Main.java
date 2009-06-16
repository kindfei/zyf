package demo.terracota.linkedblockingqueue;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Date;

public class Main
{
    public static final Main instance = new Main();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private BlockingQueue<Date> queue = new LinkedBlockingQueue<Date>();

    public void listen() throws InterruptedException
    {
        while (true) { 
            System.out.println("Received Date: " + queue.take());
        }
    } 

    public void run() throws InterruptedException
    {
        if (lock.writeLock().tryLock()) { 
            System.out.println("Waiting...");
            listen();
        } else {
            queue.put(new Date());
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        instance.run();
    }
}
