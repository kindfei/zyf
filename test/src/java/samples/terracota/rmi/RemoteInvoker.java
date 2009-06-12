package samples.terracota.rmi;

/**
  All content copyright (c) 2003-2007 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/
import java.util.concurrent.*;
import java.lang.reflect.*;

public class RemoteInvoker implements InvocationHandler, Runnable
{
    private BlockingQueue<MethodArguments> queue = new LinkedBlockingQueue<MethodArguments>();

    private final Object instance;

    public RemoteInvoker(Object instance)
    {
        this.instance = instance;
        start();
    } 

    private static class MethodArguments
    {
        public final Object proxy;
        public final Method method;
        public final Object[] args;
        private MethodResult result;

        public MethodArguments(Object proxy, Method method, Object[] args)
        {
            this.proxy = proxy;
            this.method = method;
            this.args = args;
        }

        public synchronized MethodResult getResult() throws InterruptedException
        {
           while (result == null) { wait(); } 
           return result;
        }

        public synchronized void setResult(MethodResult result)
        {
            this.result = result;
            notify();
        } 
    }

    private static class MethodResult
    {
        public final Object object;
        public final Exception exception;
 
        public MethodResult(Object object, Exception exception)
        {
            this.object = object;
            this.exception = exception;
        }
    }

    private void start()
    {
        Thread t = new Thread(this);
//        t.setDaemon(true);
        t.start();
    }
 
    public void run()
    {
        synchronized (instance) {
            System.out.println("I am servicing requests...");

            MethodArguments arguments;
            while (true) { 
                try {
                    arguments = queue.take();
                    try {
                        Object value = arguments.method.invoke(instance, arguments.args);
                        arguments.setResult(new MethodResult(value, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                        arguments.setResult(new MethodResult(null, e));
                    }
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        }
    } 

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        MethodArguments arguments = new MethodArguments(proxy, method, args); 
        queue.put(arguments);
        MethodResult result;

        try {
           result = arguments.getResult();
        } catch (Throwable e) {
           System.out.println("Busted");
           //e.printStackTrace();
           return null;
        }

        if (result.exception != null) {
            throw result.exception;
        }

        return result.object;
    }
}
