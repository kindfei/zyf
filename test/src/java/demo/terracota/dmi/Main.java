package demo.terracota.dmi;

/*
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.io.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main
{
    public static Main instance = new Main ();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void receive() throws InterruptedException
    {
        // actually, we just do nothing
        System.out.println("Waiting for dmi method calls...");

        // this just blocks the thread forever
        Thread.currentThread().join();
    }

    public void send() throws IOException
    {
        String msg;

        while (true) { 
          System.out.print("Enter a message> "); System.out.flush();
          print(new BufferedReader(new InputStreamReader(System.in)).readLine());
        } 
    }
 
    // this method gets called on the originating node from the send() method, and, 
    // since it is marked as dmi, that call is packaged up and executed on all
    // all other nodes that have this shared object resident 
    public void print(String message)
    {
        System.out.println(message);
    }

    public void run() throws Exception
    {
        // a reentrantreadwrite lock acts an excellent arbiter for one reader, 
        // many writers 
        if (lock.writeLock().tryLock()) {
            receive();
        } else {
            send();
        }
    }

    public static void main(String[] args) throws Exception
    {
        instance.run();
    }
}
