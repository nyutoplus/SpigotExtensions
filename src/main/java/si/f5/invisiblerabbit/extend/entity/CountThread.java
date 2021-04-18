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

    ChunkRegister register;
    int count;

    Map<String, Integer> entityCount;

    Timings start;
    Timings end;
    Timings allend;

    Lock endlocks;

    public CountThread(Timings st, Timings en) {
	setName("EntityCountThread-" + (threadnumber++));
	aliveflag = true;
	start = st;
	end = new Timings();
	allend = en;
	endlocks = new ReentrantLock();
    }

    @Override
    public void run() {
	while (aliveflag) {
	    start.waitTask();
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
	    register = null;
	    end.notifyAllTask();
	    allend.waitTask();
	}
    }

    public void countEntity(ChunkRegister register) {
	this.register = register;
	end.setFlags();
    }

    public void joinFinishd() {
	end.waitTask();
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
