package test.terracota.demo.hashmap;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

public class Main
{
    public static final Main instance = new Main();

    private Map<String, Date> map = new HashMap<String, Date>();

    private void put(String name, Date d)
    {
        synchronized (map) {
            map.put(name, d);
        }
    } 

    public void run() throws IOException
    {
        System.out.print("Enter name> "); System.out.flush();
        String name = new BufferedReader(new InputStreamReader(System.in)).readLine();

        Date d = map.get(name);
        if (d != null) {
            System.out.println(name + " was added on " + d);
            return;  // note the return here
        }
  
        put(name, d = new Date());
        System.out.println("Added " + name + " on " + d);
    }

    public void list()
    {
        // note that it is usually not a good idea to do IO inside of a 
        // synchronized block; this is only for demo purposes
        synchronized (map) {
            for (Map.Entry<String, Date> entry : map.entrySet()) {
                System.out.println(entry.getKey()  + " : " + entry.getValue());
            }
        }
    }



    public static void main(String[] args) throws IOException
    {
        if (args.length > 0 && args[0].equals("list")) {
            instance.list();
        } else {
            instance.run();
        }
    }
}
