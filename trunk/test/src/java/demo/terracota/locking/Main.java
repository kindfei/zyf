package demo.terracota.locking;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

import java.util.HashMap;

public class Main
{
    public static Main instance = new Main();
    private HashMap<String, String>  map = new HashMap<String, String>();

    
    // Demonstrates the use of write locking for a synchronized method
    public synchronized void method1()
    {
        map.put("key1", "key1");
        System.out.println("Successfully put key1: key1");
    }

    // Demonstrates the use of write locking for a method with synchronized block(s).  
    public void method2()
    {
        // this block will be autolocked
        synchronized (this) {
            map.put("key2", "key2");
        }
        System.out.println("Successfully put key2: key2");

        // note that this block will also be autolocked
        synchronized (this) {
            map.put("key3", "key3");
        }
        System.out.println("Successfully put key3: key3");
    }

    // Demonstrates the use of a concurrent lock.
    public void method3()
    {
        synchronized (this) {
            map.put("key4", "key4");
        } 
        System.out.println("Successfully put key4: key4");
    }

    // Demonstrates the use of the auto-synchronized feature.
    public void method4()
    {
        map.put("key5", "key5");
        System.out.println("Successfully put key5: key5");
    }

    /**
     * Demonstrates a read.  Notice that a read does not require any locking.
     * 
     * The use of a non-locked read is dangerous - no guarantees are made 
     * for consistency or correctness.  In other words, readers may get data
     * that is in any state and may not be consistent.
     * 
     * It is not recommended to read data without locking.  If you are looking
     * for performance, consider using a concurrent or read lock which should
     * be fast enough.
     */
    public void method5()
    {
        System.out.println("Key1 is: " + map.get("key1"));
    }

    // Demonstrates a read with an auto-synchronized lock.  
    public void method6()
    {
        System.out.println("Key2 is: " + map.get("key2"));
    }

    // Demonstrates the use of a read lock on a synchronized method.  
    public synchronized void method7()
    {
        System.out.println("Key3 is: " + map.get("key3"));
    }

    // Demonstrates the use of auto-locking with synchronized blocks in the method.  
    public void method8()
    {
        String out;
        synchronized (this) { 
            out = map.get("key4");
        }
        System.out.println("Key4 is: " + out);
    }

    public static void main(String[] args) 
    {
        instance.method1();
        instance.method2();
        instance.method3();
        instance.method4();
        instance.method5();
        instance.method6();
        instance.method7();
        instance.method8();
    }
}
