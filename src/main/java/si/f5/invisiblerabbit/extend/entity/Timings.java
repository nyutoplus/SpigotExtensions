package si.f5.invisiblerabbit.extend.entity;

public class Timings {

    private boolean flag = true;

    public void waitTask() {
	try {
	    synchronized (this) {
		while (flag) {
		    wait();
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
