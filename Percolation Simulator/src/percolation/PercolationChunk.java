package percolation;

import java.util.ArrayList;
import java.util.List;

import graphics.Chunk;

import utils.Dimension3;
import utils.Vec3;

import static percolation.PercolationBlock.BLOCK_TYPE.*;

public class PercolationChunk extends Chunk<PercolationBlock> {
	
	private List<List<Vec3>> waterBlocks;
	private boolean finishedPercolation;
	private float p;

	public PercolationChunk(Vec3 loc, Dimension3 numBlocks, Dimension3 blockSize, 
			float p) {
		super(loc, numBlocks, blockSize);
		this.p = p;
	}

	@Override
	protected PercolationBlock newBlock() {
		return new PercolationBlock(CLOSED);
	}
	
	private void checkIfInitialized() {
		if (!isInitialized()) {
			throw new IllegalStateException("PercolationChunk not initialized.");
		}		
	}
	
	@Override
	public void init() {
		super.init();
		waterBlocks = new ArrayList<>();
	}
	
	@Override
	public void reset() {
		super.reset();
		
		waterBlocks.clear();
		finishedPercolation = false;
	}
	
	public void randomizeBlockTypes() {
		checkIfInitialized();

		finishedPercolation = false;
		waterBlocks.clear();

		for (PercolationBlock block : getBlocks()) {

			final float rand = (float) Math.random();

			if (rand < p) {
				block.setType(OPEN);
			} else {
				block.setType(CLOSED);
			}
		}
	}

	public void populateTopRowWithWater() {
		checkIfInitialized();
				
		final int topRow = (int) getNumBlocks().getHeight() - 1;
		
		for (int x = 0; x < getNumBlocks().getWidth(); x++) {
			for (int z = 0; z < getNumBlocks().getDepth(); z++) {	
				PercolationBlock block = getBlocksList3D().get(x).get(topRow).get(z);
				if (block.getType() == OPEN) {
					// Create new step array if necessary
					if (waterBlocks.isEmpty())
						waterBlocks.add(new ArrayList<>());
					
					setWater(0, new Vec3(x, topRow, z));
				}
			}
		}		
	}
	
	public void clearWater() {
		checkIfInitialized();

		waterBlocks.clear();
		finishedPercolation = false;
		
		for (PercolationBlock block : getBlocks()) {
			if (block.getType() == WATER) {
				block.setType(OPEN);
			}
		}		
	}
	
	/**
	 * Take one step forward in the percolation process.
	 * @return the number of new water blocks added
	 */
	public int stepForward() {
		
		checkIfInitialized();
		
		// No need to percolate if we're already done
		if (finishedPercolation) {
			return 0;
		}
		
		final int lastStepIndex = waterBlocks.size() - 1;
		List<Vec3> lastBlocksAdded = waterBlocks.get(lastStepIndex);
		boolean blockWasAdded = false;

		for (Vec3 waterBlockLoc : lastBlocksAdded) {
			List<Vec3> surroundingBlocks = getSurroundingBlocks(waterBlockLoc);
			
			for (Vec3 sblockLoc : surroundingBlocks) {
				PercolationBlock sblock = getBlockAt(sblockLoc);
				// We only want to check blocks that are on the same
				// level or below the block AND blocks that are open
				if (sblockLoc.getY() > waterBlockLoc.getY() 
						|| sblock.getType() != OPEN) 
					continue;
				
				// Create a new list for the new step if necessary
				if (!blockWasAdded) {
					blockWasAdded = true;
					waterBlocks.add(new ArrayList<>());
				}
				
				setWater(lastStepIndex + 1, sblockLoc);
			}
		}
		
		finishedPercolation = !blockWasAdded;
		final int numBlocksAdded = finishedPercolation ? 0 : 
			waterBlocks.get(lastStepIndex + 1).size();
		return numBlocksAdded;
		
	}
	
	/**
	 * Take one step backward in the percolation process.
	 * @return the number of new water blocks removed
	 */
	public int stepBack() {
		
		checkIfInitialized();

		// Check if we're already done
		if (getCurrentStep() <= 0) {
			return 0;
		}
		
		// Update boolean
		finishedPercolation = false;
		
		// Remove all water blocks in most recent step
		for (Vec3 waterBlock : waterBlocks.get(getCurrentStep())) {
			PercolationBlock block = getBlockAt(waterBlock);
			block.setType(OPEN);
		}
		
		final int numBlocksRemoved = waterBlocks.get(getCurrentStep()).size();
		
		// Delete blocks
		waterBlocks.remove(getCurrentStep());
				
		return numBlocksRemoved;
		
	}
	
	public int getCurrentStep() {
		return waterBlocks.size() - 1;
	}
	
	// Maximum of 6 surrounding blocks (no diagonals)
	private List<Vec3> getSurroundingBlocks(Vec3 loc) {
		List<Vec3> list = new ArrayList<>();
		
		// Top
		list.add(new Vec3(loc.getX() + 0f, loc.getY() + 1f, loc.getZ() + 0f));
		// Right
		list.add(new Vec3(loc.getX() + 1f, loc.getY() + 0f, loc.getZ() + 0f));
		// Bottom
		list.add(new Vec3(loc.getX() + 0f, loc.getY() - 1f, loc.getZ() + 0f));
		// Left
		list.add(new Vec3(loc.getX() - 1f, loc.getY() + 0f, loc.getZ() + 0f));
		// Behind ( > z )
		list.add(new Vec3(loc.getX() + 0f, loc.getY() + 0f, loc.getZ() + 1f));
		// In front ( < z )
		list.add(new Vec3(loc.getX() + 0f, loc.getY() + 0f, loc.getZ() - 1f));
		
		// Remove invalid locations
		for (int i = list.size()-1; i >= 0; i--) {
			if (!isExistingLoc(list.get(i))) {
				list.remove(i);
			}
		}
		
		return list;
	}
	
	private boolean isExistingLoc(Vec3 loc) {
		return     loc.getX() >= 0 && loc.getX() < getNumBlocks().getWidth()
				&& loc.getY() >= 0 && loc.getY() < getNumBlocks().getHeight()
				&& loc.getZ() >= 0 && loc.getZ() < getNumBlocks().getDepth();
	}
	
	private PercolationBlock getBlockAt(Vec3 blockLoc) {
		final int x = (int) blockLoc.getX();
		final int y = (int) blockLoc.getY();
		final int z = (int) blockLoc.getZ();

		return getBlocksList3D().get(x).get(y).get(z);
	}
	
	private void setWater(int step, Vec3 blockLoc) {
		PercolationBlock block = getBlockAt(blockLoc);
		block.setType(WATER);
//		waterBlocks.add(blockLoc);
		waterBlocks.get(step).add(blockLoc);
	}
		
	public boolean finishedPercolation() {
		return finishedPercolation;
	}
	
	public float getP() {
		return p;
	}
	
	public void setP(float newP) {
		p = newP;
	}

}
