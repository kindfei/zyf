package samples.terracota.listener;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

import java.io.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static Main instance = new Main();
    private transient List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private int counter = 0;

    public void count() 
    {
        synchronized (this) {
            counter++;
        }
        fireChanged(); 
    }

    public synchronized int getCount() { return counter; }

    public synchronized void addListener(ChangeListener listener)
    {
        if (listeners == null) { 
            listeners = new ArrayList<ChangeListener>();
        }
        listeners.add(listener);
    }

    public void fireChanged()
    {
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    public static class MainListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent event) 
        {
            System.out.println("Main was updated.  Count is " + ((Main) event.getSource()).getCount());
        }
    }


    public static void main(String[] args) throws IOException
    {
        instance.addListener(new MainListener());
        while (true) {
            System.out.print("Press return to count..."); System.out.flush();
            new BufferedReader(new InputStreamReader(System.in)).readLine();
            instance.count();
        }
    }
}
