package percolation;

import static percolation.PercolationChunkUtils.getNumBlocks;
import static percolation.PercolationChunkUtils.getPercolatedWaterBlocks;

import java.util.HashMap;
import java.util.Map;

import percolation.PercolationBlock.BLOCK_TYPE;


public class PercolationChunkInfo {
	
	public static enum PROPERTY {
		P, TOTAL_BLOCKS, CLOSED_BLOCKS, OPEN_BLOCKS, WATER_BLOCKS, 
		PERCOLATED_BLOCKS, CURRENT_STEP
	}
	
	private Map<PROPERTY, Float> properties;
	
	public PercolationChunkInfo(PercolationChunk chunk) {
		properties = new HashMap<>();
		
		set(chunk);
	}
	
	public PercolationChunkInfo() {
		properties = new HashMap<>();
	}
	
	public void set(PercolationChunk pchunk) {
		if (!pchunk.isInitialized()) {
			throw new IllegalStateException("PercolationChunk not initialized");
		}
		
		// Current step
		properties.put(PROPERTY.CURRENT_STEP, (float) pchunk.getCurrentStep());
		
		// Total num blocks
		final float numBlocks = pchunk.getBlocks().size();
		
		properties.put(PROPERTY.TOTAL_BLOCKS, numBlocks);
		
		// Closed, open, and water blocks
		final float numClosed = getNumBlocks(pchunk, BLOCK_TYPE.CLOSED).size();
		final float numOpen = getNumBlocks(pchunk, BLOCK_TYPE.OPEN).size();
		final float numWater = numBlocks - numClosed - numOpen; // Faster
		
		properties.put(PROPERTY.CLOSED_BLOCKS, numClosed);
		properties.put(PROPERTY.OPEN_BLOCKS, numOpen);
		properties.put(PROPERTY.WATER_BLOCKS, numWater);
		
		// Determine percolation
		final float numPercedBlocks = getPercolatedWaterBlocks(pchunk).size();
		properties.put(PROPERTY.PERCOLATED_BLOCKS, numPercedBlocks);
	}
	
	public float getProperty(PROPERTY prop) {
		return properties.get(prop);
	}
	
}
