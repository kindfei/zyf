package test.terracota.demo.rrwl;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.util.concurrent.locks.*;

public class Main implements Runnable
{
    public static final Main instance = new Main();
    private int counter = 0;
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public void read() 
    {
        new Thread(instance).start();
        new Thread(instance).start();
    }

    public void write() 
    {
        while (true) {
            rwl.writeLock().lock();
            try {
               System.out.println("Incrementing counter.  Counter is " + (++counter)); 
            } finally {
                 rwl.writeLock().unlock();
            }
            try { Thread.currentThread().sleep(3000); } catch (InterruptedException ie) {  } 
        }
    }

    public void run()
    {
        while (true) {
            rwl.readLock().lock();
            try {
                System.out.println("counter is " + counter); 
            } finally {
                rwl.readLock().unlock();
            }
            try { Thread.currentThread().sleep(1000); } catch (InterruptedException ie) {  } 
        }
    }

     
    public static void main(String[] args) 
    {
        if (args.length > 0)  { instance.write(); return; }
        instance.read();
    }
}
