package samples.terracota.rmi;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.lang.reflect.*;

public class Main implements TestInterface
{
    int counter = 0;

    public int count(int count)
    { 
        System.out.println("Incrementing counter by: " + count);

        counter += count;
        return counter;
    }

    public static void main(String[] args) 
    {
        Main t = new Main();
        RemoteInvoker invoker = new RemoteInvoker(t);
        TestInterface proxy = 
            (TestInterface) Proxy.newProxyInstance(t.getClass().getClassLoader(), 
                                                   new Class[] { TestInterface.class } , 
                                                   invoker);
        System.out.println("Proxy returns: " + proxy.count(3));         
    }
}
