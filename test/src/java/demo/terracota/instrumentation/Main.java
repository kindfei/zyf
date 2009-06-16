package demo.terracota.instrumentation;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

public class Main
{
    public static Main instance = new Main();
    private Counter counter =  new Counter();

    public void count() 
    {
        counter.count();
        System.out.println(counter);
    }

    public static void main(String[] args) 
    {
        instance.count();
    }
}
