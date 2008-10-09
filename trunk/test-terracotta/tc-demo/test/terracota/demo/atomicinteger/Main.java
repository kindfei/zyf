package test.terracota.demo.atomicinteger;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.util.concurrent.atomic.*;

public class Main
{
    public static final Main instance = new Main();

    private AtomicInteger counter = new AtomicInteger(0);

    public void run()
    {
        System.out.println("Count is: " + counter.getAndIncrement());
    }

    public static void main(String[] args)
    {
        instance.run();
    }
}
