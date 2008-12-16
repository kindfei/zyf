package test.terracota;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestQueue {
//	private static LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
	private static Queue<Object> queue = new Queue<Object>();

	static class Taker extends Thread {
		private boolean active = true;
		private String name = getName();

		@Override
		public void run() {
			while (active) {
				try {
					System.out.println(name + "-" + queue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static class Putter extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				try {
					queue.put(i + "");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		int putterSize = Integer.parseInt(args[0]);
		for (int i = 0; i < putterSize; i++) {
			new Putter().start();
		}
		int takerSize = Integer.parseInt(args[1]);
		for (int i = 0; i < takerSize; i++) {
			new Taker().start();
		}
	}
}

class Queue<E> {
	private final AtomicInteger count = new AtomicInteger(0);
	private Node<E> head;
	private Node<E> last;
	private ReentrantLock putLock = new ReentrantLock();
	private ReentrantLock takeLock = new ReentrantLock();
//	private Condition notEmpty = takeLock.newCondition();
//	private State notEmpty = new State(takeLock);
	private Case notEmpty = new Case(takeLock);

	Queue() {
		last = head = new Node<E>(null);
	}

	static class Node<T> {
		volatile T task;

		Node<T> next;

		Node(T task) {
			this.task = task;
		}
	}

	private void signalNotEmpty() {
		takeLock.lock();
		try {
			notEmpty.signal();
		} finally {
			takeLock.unlock();
		}
	}

	private void insert(E task) {
		last = last.next = new Node<E>(task);
	}

	private E extract() {
		Node<E> first = head.next;
		head = first;
		E task = first.task;
		first.task = null;
		return task;
	}

	public void put(E task) throws InterruptedException {
		if (task == null)
			throw new NullPointerException();
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

	public E take() throws InterruptedException {
		E task;
		int c = -1;
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			try {
				if (count.get() == 0)
					notEmpty.await();
			} catch (InterruptedException e) {
				notEmpty.signal();
				throw e;
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
}

class State {
	private Condition cond;

	private Node head;
	private Node last;

	private volatile boolean signal = false;

	State(ReentrantLock lock) {
		this.cond = lock.newCondition();
		last = head = new Node();
	}

	static class Node {
		Node next;

		Node() {
		}
	}

	private Node create() {
		return last = last.next = new Node();
	}

	private void remove() {
		head = head.next;
	}

	public void await() throws InterruptedException {
		Node n = create();
		do {
			cond.await();
		} while (!signal || n != head.next);
		signal = false;
		remove();
	}

	public void signal() {
		signal = true;
		cond.signalAll();
	}
}

class Case {
	private ReentrantLock lock;

	private Node head;
	private Node last;

	Case(ReentrantLock lock) {
		this.lock = lock;
		last = head = new Node(null);
	}

	static class Node {
		Condition c;
		Node next;

		Node(Condition c) {
			this.c = c;
		}
	}

	private void insert(Condition c) {
		last = last.next = new Node(c);
	}

	private Condition extract() {
		Node first = head.next;
		head = first;
		Condition c = first.c;
		first.c = null;
		return c;
	}

	public void await() throws InterruptedException {
		Condition c = lock.newCondition();
		insert(c);
		c.await();
	}

	public void signal() {
		if (head.next == null) return;
		extract().signal();
	}
}
