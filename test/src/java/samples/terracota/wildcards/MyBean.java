package samples.terracota.wildcards;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.util.Date;

public class MyBean
{
    private String name;
    private Date d;

    public MyBean(String name)
    {
        this(name, new Date());
    }

    public MyBean(String name, Date d)
    {
        this.name = name;
        this.d = d;
    }

    public synchronized String getName() { return name; }

    public synchronized void setName(String name) { this.name = name; }

    public synchronized Date getDate() { return d; }

    public synchronized void setDate(Date d) { this.d = d; }

    public synchronized String toString()
    {
        return name + " was added on " + d;
    }
}
