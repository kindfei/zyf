package core.cluster;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cluster task queue
 * @author zhangyf
 *
 */
public class ClusterTaskQueue {
	private final AtomicInteger count = new AtomicInteger(0);
    private transient Node head;
    private transient Node last;
	private ReentrantLock putLock = new ReentrantLock();
	private ReentrantLock takeLock;
	private ReentrantLock waitLock = new ReentrantLock();
	private Condition notEmpty = waitLock.newCondition();
	
	private boolean fairTake;
	
	ClusterTaskQueue(boolean fairTake) {
		last = head = new Node(null);
		
		this.fairTake = fairTake;
		takeLock =  new ReentrantLock(fairTake);
	}
	
	boolean isFairTake() {
		return fairTake;
	}
	
    static class Node {
        volatile ClusterTask task;
        
        Node next;
        
        Node(ClusterTask task) {
        	this.task = task;
        }
    }

    private void signalNotEmpty() {
    	waitLock.lock();
    	try {
    		notEmpty.signal();
    	} finally {
        	waitLock.unlock();
    	}
    }
    
    private void insert(ClusterTask task) {
        last = last.next = new Node(task);
    }

    private ClusterTask extract() {
        Node first = head.next;
        head = first;
        ClusterTask task = first.task;
        first.task = null;
        return task;
    }
    
    public void putTask(ClusterTask task) throws InterruptedException {
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
    
    public ClusterTask takeTask() throws InterruptedException {
    	ClusterTask task;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            try {
        		if (count.get() == 0) {
        			waitLock.lockInterruptibly();
	        		try {
	            		if (count.get() == 0)
	                		notEmpty.await();
	        		} finally {
		            	waitLock.unlock();
	        		}
        		}
            } catch (InterruptedException e) {
                throw e;
            }

            task = extract();
            count.getAndDecrement();
        } finally {
            takeLock.unlock();
        }
        return task;
    }
}
