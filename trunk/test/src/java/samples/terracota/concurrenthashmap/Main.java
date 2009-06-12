package samples.terracota.concurrenthashmap;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

public class Main
{
    public static final Main instance = new Main();

    private Map<String, Date> map = new ConcurrentHashMap<String, Date>();

    public void run() throws IOException
    {
        System.out.print("Enter name> "); System.out.flush();
        String name = new BufferedReader(new InputStreamReader(System.in)).readLine();

        Date d = map.get(name);
        if (d != null) {
            System.out.println(name + " was added on " + d);
            return;
        }
  
        d = new Date();
        map.put(name, d);
        System.out.println("Added " + name + " on " + d);
    }

    public void list()
    {
        for (Map.Entry<String, Date> entry : map.entrySet()) {
            System.out.println(entry.getKey()  + " : " + entry.getValue());
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
