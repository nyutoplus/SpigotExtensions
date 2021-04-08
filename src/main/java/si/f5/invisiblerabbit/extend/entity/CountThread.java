package si.f5.invisiblerabbit.extend.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

public class CountThread extends Thread {

    private static int threadnumber = 0;

    boolean aliveflag;

    boolean works;

    boolean startwait;
    boolean finishwait;

    ChunkRegister register;
    int count;

    Map<String, Integer> entityCount;

    StartTiming start;
    EndTiming end;

    Lock endlocks;

    public CountThread(StartTiming st) {
	setName("EntityCountThread-" + (threadnumber++));
	aliveflag = true;
	startwait = true;
	finishwait = true;
	start = st;
	end = new EndTiming();
	endlocks = new ReentrantLock();
    }

    @Override
    public void run() {
	while (aliveflag) {
	    startwait = true;
	    while (startwait) {
		start.waitTask();
	    }
	    if (aliveflag) {
		works = true;
		count = 0;
		entityCount = new HashMap<String, Integer>();
		while (works) {
		    Chunk tmp = register.getWorkChunk();
		    if (tmp == null) {
			works = false;
			continue;
		    }
		    Entity[] entity = tmp.getEntities();
		    count += entity.length;
		    for (Entity e : entity) {
			String name = e.getType().toString() + (e.getCustomName() == null ? "" : "_HAS_NAME");
			entityCount.put(name, entityCount.getOrDefault(name, 0) + 1);
		    }
		}
	    }
	    endlocks.lock();
	    try {
		finishwait = false;
		end.notifyAllTask();
	    } finally {
		endlocks.unlock();
	    }
	}
    }

    public void countEntity(ChunkRegister register) {
	this.register = register;
	startwait = false;
	finishwait = true;
    }

    public void joinFinishd() {
	try {
	    boolean ends;
	    endlocks.lock();
	    try {
		ends = finishwait;
	    } finally {
		endlocks.unlock();
	    }
	    while (ends) {
		end.waitTask();
		endlocks.lock();
		try {
		    ends = finishwait;
		} finally {
		    endlocks.unlock();
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public int getCount() {
	return count;
    }

    public Map<String, Integer> getCountMap() {
	return entityCount;
    }

    public void unload() {
	aliveflag = false;
	try {
	    start.notifyAllTask();
	    end.notifyAllTask();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
