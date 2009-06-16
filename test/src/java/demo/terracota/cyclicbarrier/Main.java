package demo.terracota.cyclicbarrier;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.util.concurrent.*;

public class Main
{
    public static final Main instance = new Main();

    private CyclicBarrier barrier = new CyclicBarrier(2);

    public void run() throws BrokenBarrierException, InterruptedException 
    {
        System.out.println("Waiting for other node to join...");
        barrier.await();
        System.out.println("Started.");
    }

    public static void main(String[] args) 
        throws BrokenBarrierException, InterruptedException
    {
        instance.run();
    }
}
