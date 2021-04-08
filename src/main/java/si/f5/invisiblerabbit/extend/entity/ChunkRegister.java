package si.f5.invisiblerabbit.extend.entity;

import org.bukkit.Chunk;

public class ChunkRegister {
    Chunk[] chunks;
    int step;

    public ChunkRegister(Chunk[] chunks) {
	this.chunks = chunks;
	step = 0;
    }

    public synchronized Chunk getWorkChunk() {
	if (chunks.length <= step) return null;
	Chunk tmp = chunks[step];
	step++;
	return tmp;
    }
}
