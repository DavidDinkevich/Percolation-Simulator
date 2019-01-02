package percolation;

import static utils.Utils.round;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import percolation.PercolationBlock.BLOCK_TYPE;

import utils.Dimension3;
import utils.Utils;

public final class PercolationChunkUtils {

	private PercolationChunkUtils() {
	}
	
	public static List<PercolationBlock> getNumBlocks(
			PercolationChunk pc, BLOCK_TYPE type) {
		
		List<PercolationBlock> blocks = Collections.emptyList();
		
		for (PercolationBlock block : pc.getBlocks()) {
			if (block.getType() == type) {
				if (blocks.isEmpty())
					blocks = new ArrayList<>();
				blocks.add(block);
			}
		}
		
		return blocks;
	
	}

	public static List<PercolationBlock> getPercolatedWaterBlocks(
			PercolationChunk pchunk) {
		
		List<PercolationBlock> groundWaterBlocks = Collections.emptyList();
		List<List<List<PercolationBlock>>> list3d = pchunk.getBlocksList3D();
		Dimension3 size = pchunk.getNumBlocks();
		
		for (int x = 0; x < size.getWidth(); x++) {
			for (int z = 0; z < size.getDepth(); z++) {
				// Get the block
				PercolationBlock block = list3d.get(x).get(0).get(z);
				// Check if it's water
				if (block.getType() == BLOCK_TYPE.WATER) {
					// Create list if necessary
					if (groundWaterBlocks.isEmpty())
						groundWaterBlocks = new ArrayList<>();
					
					groundWaterBlocks.add(block);
				}
			}
		}
		
		return groundWaterBlocks;
	}
	
	public static float getPercentPercolatedWaterBlocks(
			PercolationChunk pchunk, int numDecimals) {
		
		Dimension3 totalBlocks = pchunk.getNumBlocks();
		final int faceArea = (int) (totalBlocks.getWidth() * totalBlocks.getDepth());
		final float numPercedBlocks = getPercolatedWaterBlocks(pchunk).size();
		final float percentPerced = numPercedBlocks / faceArea * 100f;
		final float roundedPercent =  Utils.round(percentPerced, 1);
		
		return roundedPercent;
		
	}
	
	public static float getPercentOfBlockType(
			PercolationChunk pchunk, BLOCK_TYPE type, int numDecimals) {
		
		final int numBlocks = pchunk.getBlocks().size();
		final int numType = getNumBlocks(pchunk, type).size();
		return round((float) numType / numBlocks * 100f, 1);
	
	}

}
