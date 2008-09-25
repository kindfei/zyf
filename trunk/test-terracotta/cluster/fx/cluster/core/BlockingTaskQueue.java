package fx.cluster.core;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingTaskQueue {
	private final AtomicInteger count = new AtomicInteger(0);
    private transient Node head;
    private transient Node last;
	private ReentrantLock putLock = new ReentrantLock();
	private ReentrantLock takeLock = new ReentrantLock();
	private Condition notEmpty = takeLock.newCondition();
	
	BlockingTaskQueue() {
		last = head = new Node(null);
	}
	
    static class Node {
        volatile Task task;
        
        Node next;
        
        Node(Task task) {
        	this.task = task;
        }
    }

    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }
    
    private void insert(Task task) {
        last = last.next = new Node(task);
    }

    private Task extract() {
        Node first = head.next;
        head = first;
        Task task = first.task;
        first.task = null;
        return task;
    }
    
    public void put(Task task) throws InterruptedException {
        if (task == null) throw new NullPointerException();
        int c = -1;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            insert(task);
            c = count.getAndIncrement();
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();
    }
    
    public Task take() throws InterruptedException {
    	Task task;
        int c = -1;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            try {
                while (count.get() == 0)
                    notEmpty.await();
            } catch (InterruptedException ie) {
                notEmpty.signal();
                throw ie;
            }

            task = extract();
            c = count.getAndDecrement();
            if (c > 1)
                notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
        return task;
    }
    
    public static void main(String[] args) {
    	final BlockingTaskQueue queue = new BlockingTaskQueue();
    	
    	new Thread() {
    		@Override
    		public void run() {
    			try {
					for (int i = 0; i < 100; i++) {
						queue.put(new Task(ExecuteMode.TASK_QUEUE, i + ""));
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}.start();
    	
    	class TakeThread extends Thread {
    		@Override
    		public void run() {
    			try {
					for (;;) {
						System.out.println(Thread.currentThread().getName() + " - " + queue.take().getContent());
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	
    	new TakeThread().start();
    	new TakeThread().start();
    	new TakeThread().start();
    	new TakeThread().start();
    	new TakeThread().start();
    	new TakeThread().start();
    	new TakeThread().start();
    	new TakeThread().start();
	}
}
