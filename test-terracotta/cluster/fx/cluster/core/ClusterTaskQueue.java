package fx.cluster.core;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ClusterTaskQueue {
	private final AtomicInteger count = new AtomicInteger(0);
    private transient Node head;
    private transient Node last;
	private ReentrantLock putLock = new ReentrantLock();
	private ReentrantLock takeLock = new ReentrantLock(true);
	private Object notEmpty = new Object();
	
	ClusterTaskQueue() {
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
    	synchronized (notEmpty) {
    		notEmpty.notify();
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
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            try {
        		if (count.get() == 0)
	            	synchronized (notEmpty) {
	            		while (count.get() == 0)
	                		notEmpty.wait();
					}
            } catch (InterruptedException ie) {
                throw ie;
            }

            task = extract();
            count.getAndDecrement();
        } finally {
            takeLock.unlock();
        }
        return task;
    }
}
