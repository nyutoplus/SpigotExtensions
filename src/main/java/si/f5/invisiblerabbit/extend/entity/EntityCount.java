package si.f5.invisiblerabbit.extend.entity;

import java.text.NumberFormat;
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

    StartTiming st;

    NumberFormat format = NumberFormat.getNumberInstance();

    public EntityCount(int maxThread) {
	st = new StartTiming();
	ct = new CountThread[maxThread];
	for (int i = 0; i < ct.length; i++) {
	    ct[i] = new CountThread(st);
	    ct[i].start();
	}
    }

    public void setChunks(Chunk[] c) {
	ChunkRegister cr = new ChunkRegister(c);
	for (int i = 0; i < ct.length; i++) {
	    ct[i].countEntity(cr);
	}
	st.notifyAllTask();
	for (int i = 0; i < ct.length; i++) {
	    ct[i].joinFinishd();
	}
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
