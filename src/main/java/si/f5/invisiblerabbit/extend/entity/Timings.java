package si.f5.invisiblerabbit.extend.entity;

public class Timings {

    private boolean flag = true;

    private static long limitMillis = 2000;

    public void waitTask() {
	try {
	    synchronized (this) {
		while (flag) {
		    wait(limitMillis);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void notifyAllTask() {
	try {
	    synchronized (this) {
		flag = false;
		notifyAll();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void setFlags() {
	synchronized (this) {
	    flag = true;
	}
    }
}
