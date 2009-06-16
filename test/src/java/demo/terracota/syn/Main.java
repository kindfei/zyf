package demo.terracota.syn;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.io.*;
import java.util.concurrent.locks.*;

public class Main
{
    public static final Main instance = new Main();
    public ReentrantReadWriteLock lock = new ReentrantReadWriteLock();;
    public String msg;

    public void run() throws Exception
    {
         if (lock.writeLock().tryLock()) {
             System.out.println("Waiting for input..."); 
             while (true) { printInput(); }
         } else {
             while (true) { getInput(); }
         }
    }

    private synchronized void getInput() throws IOException
    {
        System.out.print("Enter a message> "); System.out.flush();
        msg = new BufferedReader(new InputStreamReader(System.in)).readLine();
        notify();
    }

    private synchronized void printInput() throws InterruptedException
    {
        while (msg == null) { wait(); } 
        System.out.println(msg);
        msg = null;
    }

    public static void main(String[] args) throws Exception
    {
       instance.run(); 
    }
}
