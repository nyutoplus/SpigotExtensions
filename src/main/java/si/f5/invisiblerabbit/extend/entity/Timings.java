package si.f5.invisiblerabbit.extend.entity;

import java.util.concurrent.atomic.AtomicBoolean;

public class Timings {

    private AtomicBoolean flag;

    private long limitMillis;

    private int limitNanos;

    public Timings() {
	this(1000, 0);
    }

    public Timings(long limitMillis) {
	this(limitMillis, 0);
    }

    public Timings(long limitMillis, int limitNanos) {
	flag = new AtomicBoolean(true);
	this.limitMillis = limitMillis < 0 ? 0 : limitMillis;
	this.limitNanos = limitNanos < 0 ? 0 : limitNanos > 999999 ? 0 : limitNanos;
    }

    public synchronized void waitTask() {
	try {
	    while (flag.get()) {
		wait(limitMillis, limitNanos);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public synchronized void notifyAllTask() {
	try {
	    flag.set(false);
	    notifyAll();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void setFlags() {
	flag.set(true);
    }
}
