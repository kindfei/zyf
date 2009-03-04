package test.terracota.demo.dirtyread;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

import java.util.Date;

public class Main
{
    public static Main instance = new Main ();
    private int counter = 0;

    public synchronized void count() 
    {
        counter++;
        System.out.println("Counter is: " + counter);
    }

    // dirty reads
    public void print() throws InterruptedException
    {
        while (true) {
          System.out.println("[" + new Date() + "] Counter is: " + counter);
          Thread.currentThread().sleep(200);
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        if (args.length > 0) {
          instance.print();
        } else {
          instance.count();
        }
    }
}
