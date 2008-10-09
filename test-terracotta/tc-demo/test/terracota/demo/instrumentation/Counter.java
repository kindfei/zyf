package test.terracota.demo.instrumentation;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

public class Counter
{
    private int count = 0;

    public synchronized void count() 
    {
       count++; 
    }

    public String toString() 
    {
      return "count is " + count;
    }
}
