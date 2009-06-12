package samples.terracota.helloworld;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

public class Main
{
    public static Main instance = new Main ();
    private int counter = 0;

    public synchronized void count() 
    {
        counter++;
        System.out.println("Counter is: " + counter);
    }

    public static void main(String[] args) 
    {
        instance.count();
    }
}
