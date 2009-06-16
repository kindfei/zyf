package demo.terracota.readlock;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.io.*;

public class Main implements Runnable
{
    public static final Main instance = new Main();

    public void run() 
    {
	enterMonitor();
    }

    public synchronized void enterMonitor() 
    {
        System.out.println("I'm in the synchronized block"); System.out.flush();
        try { Thread.currentThread().sleep(5000); } 
		catch (InterruptedException ie) { System.out.println("Interrupted"); }
    }

    public static void main(String[] args) throws Exception
    {
       for (int i = 0; i < 10; i++) {
         new Thread(instance).start(); 
       }
    }
}
