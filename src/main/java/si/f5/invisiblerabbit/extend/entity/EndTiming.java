package si.f5.invisiblerabbit.extend.entity;

public class EndTiming {

    public void waitTask() {
	try {
	    synchronized (this) {
		wait(1000);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void notifyAllTask() {
	try {
	    synchronized (this) {
		notifyAll();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
