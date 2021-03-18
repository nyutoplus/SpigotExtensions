package si.f5.invisiblerabbit.extend.world;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

public class EntityCount{
	int maxThread;
	Chunk[] chunk;
	int count;
	int step;
	Map<String,Integer> perEntityCount;

	public EntityCount(int maxThread) {
		this.maxThread = maxThread;
	}

	public void setChunks(Chunk[] c) {
		chunk = c;
		count = 0;
		step = 0;
		perEntityCount = new HashMap<String, Integer>();
		Thread[] threads = new Thread[maxThread];
		for(int i=0;i<threads.length;i++) {
			threads[i] = new Thread() {
				@Override
				public void run() {
					boolean flag = true;
					while(flag) {
						Chunk tmp = getWorkChunk();
						if(tmp == null) {
							flag = false;
							continue;
						}
						Entity[] entity = tmp.getEntities();
						addCount(entity.length);
						Map<String,Integer> map = new HashMap<String, Integer>();
						for(Entity e:entity) {
							String name = e.getType().toString() + (e.getCustomName() == null ? "" : "_HAS_NAME");
							map.put(name,map.getOrDefault(name,0) + 1);
						}
						addPerEntityCount(map);
					}
				}
			};
			threads[i].start();
		}
		for(Thread t:threads) {
			try {
				t.join();
			}catch(InterruptedException e) {}
		}
	}

	public int getCount() {
		return count;
	}

	public Map<String,Integer> getPerEntityCount(){
		return perEntityCount;
	}

	public synchronized void addCount(int num) {
		count += num;
	}

	public synchronized void addPerEntityCount(Map<String,Integer> map) {
		for(String i:map.keySet()) {
			perEntityCount.put(i,perEntityCount.getOrDefault(i,0) + map.get(i));
		}
	}

	public synchronized Chunk getWorkChunk() {
		if(chunk.length <= step)return null;
		Chunk tmp = chunk[step];
		step ++;
		return tmp;
	}
}
