package si.f5.invisiblerabbit.extend.entity;

public class Timings {

    private boolean flag = true;

    private static long limitMillis = 2000;

    public synchronized void waitTask() {
	try {
	    while (flag) {
		wait(limitMillis);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public synchronized void notifyAllTask() {
	try {
	    flag = false;
	    notifyAll();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public synchronized void setFlags() {
	flag = true;
    }
}
