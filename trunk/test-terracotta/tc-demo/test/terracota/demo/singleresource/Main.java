package test.terracota.demo.singleresource;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

public class Main implements Runnable
{
    public static final Main instance = new Main();
    private transient Object resource = null;

    public void run() 
    {
        System.out.println("I'm waiting for the lock...");
        synchronized (this) {
            if (resource == null) {
                System.out.println("I'm creating the resource");
                resource = new Object();
            }

            // this just holds the lock - you could do other logic here, just make
            // sure you stay inside the lock
            try { Thread.currentThread().join(); } catch (Exception e)  {  }
        }
    }
     
    public static void main(String[] args) throws InterruptedException
    {
        instance.run();
    }
}
