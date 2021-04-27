package si.f5.invisiblerabbit.extend.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;

public class EntityCount {
    int maxThread;
    Chunk[] chunk;
    int count;
    int step;
    Map<String, Integer> perEntityCount;

    CountThread[] ct;

    Timings st;
    Timings en;

    public EntityCount(int maxThread) {
	st = new Timings();
	en = new Timings();
	ct = new CountThread[maxThread];
	for (int i = 0; i < ct.length; i++) {
	    ct[i] = new CountThread(st, en);
	    ct[i].start();
	}
    }

    public synchronized void setChunks(Chunk[] c) {
	ChunkRegister cr = new ChunkRegister(c);
	en.setFlags();
	for (int i = 0; i < ct.length; i++) {
	    ct[i].countEntity(cr);
	}
	st.notifyAllTask();
	for (int i = 0; i < ct.length; i++) {
	    ct[i].joinFinishd();
	}
	st.setFlags();
	en.notifyAllTask();
    }

    public int getCount() {
	int tmp = 0;
	for (CountThread i : ct) {
	    tmp += i.getCount();
	}
	return tmp;
    }

    public Map<String, Integer> getPerEntityCount() {
	Map<String, Integer> tmp = new HashMap<String, Integer>();
	for (CountThread i : ct) {
	    Map<String, Integer> val = i.getCountMap();
	    for (String j : val.keySet()) {
		tmp.put(j, tmp.getOrDefault(j, 0) + val.get(j));
	    }
	}
	return tmp;
    }

    public void unload() {
	for (CountThread i : ct) {
	    i.unload();
	}
    }
}
