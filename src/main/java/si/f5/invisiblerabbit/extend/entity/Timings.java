package si.f5.invisiblerabbit.extend.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Timings {

    private boolean flag;

    private long limitMillis;

    private int limitNanos;

    private Object lock;

    private Lock flagLock;

    public Timings() {
	this(1000, 0);
    }

    public Timings(long limitMillis) {
	this(limitMillis, 0);
    }

    public Timings(long limitMillis, int limitNanos) {
	flag = true;
	lock = new Object();
	flagLock = new ReentrantLock();
	this.limitMillis = limitMillis < 0 ? 0 : limitMillis;
	this.limitNanos = limitNanos < 0 ? 0 : limitNanos > 999999 ? 0 : limitNanos;
    }

    public void waitTask() {
	boolean tmp;
	try {
	    flagLock.lock();
	    tmp = flag;
	} finally {
	    flagLock.unlock();
	}
	while (tmp) {
	    try {
		synchronized (lock) {
		    lock.wait(limitMillis, limitNanos);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    try {
		flagLock.lock();
		tmp = flag;
	    } finally {
		flagLock.unlock();
	    }
	}
    }

    public void notifyAllTask() {
	try {
	    flagLock.lock();
	    flag = false;
	} finally {
	    flagLock.unlock();
	}
	try {
	    synchronized (lock) {
		lock.notifyAll();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void setFlags() {
	try {
	    flagLock.lock();
	    flag = true;
	} finally {
	    flagLock.unlock();
	}
    }
}
