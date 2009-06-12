package samples.terracota.wildcards;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

public class Main
{
    public static final Main instance = new Main();

    private Map<String, MyBean> map = new HashMap<String, MyBean>();

    private void put(String name, MyBean b)
    {
        synchronized (map) {
            map.put(name, b);
        }
    } 

    public void run() throws IOException
    {
        System.out.print("Enter name> "); System.out.flush();
        String name = new BufferedReader(new InputStreamReader(System.in)).readLine();

        MyBean b = map.get(name);
        if (b != null) {
            System.out.println(b);
            return;  // note the return here
        }
  
        put(name, b = new MyBean(name));
        System.out.println("Added " + b.getName() + " on " + b.getDate());
    }

    public void list()
    {
        // note that it is usually not a good idea to do IO inside of a 
        // synchronized block; this is only for demo purposes
        synchronized (map) {
            for (Map.Entry<String, MyBean> entry : map.entrySet()) {
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
