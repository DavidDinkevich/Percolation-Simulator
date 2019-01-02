package graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.Dimension3;
import utils.Vec3;

public abstract class Chunk<T extends GraphicsCube> extends GraphicsCube {

	private Dimension3.Mutable numBlocks;
	private Dimension3.Mutable blockSize;
	
	private Dimension3.Mutable spacing;
	
	private List<List<List<T>>> blocks;
	private List<T> list1d;
	
	private boolean initialized;

	public Chunk(Vec3 loc, Dimension3 numBlocks, Dimension3 blockSize) {
		// Size = numBlocks * blockSize
		super(loc, Dimension3.mult(numBlocks, blockSize));
		
		this.numBlocks = Dimension3.Mutable.requireNonNegative(numBlocks);
		this.blockSize = Dimension3.Mutable.requireNonNegative(blockSize);
		
		spacing = new Dimension3.Mutable();
		
		getBrush().setRenderFill(false);
		getBrush().setStroke(255);
	}
	
	/**
	 * Creates all of the blocks as determined by {@link Chunk#getNumBlocks()}.
	 * After this function is called, the number of blocks on each axis
	 * cannot be modified (see {@link Chunk#reset()}), and any attempt to
	 * do so will result in an {@link IllegalStateException}. Also, a {@link Chunk}
	 * cannot be initialized twice--doing so will also result in an 
	 * {@link IllegalStateException}.
	 */
	public void init() {
		if (initialized) {
			throw new IllegalStateException("Cannot initialize a Chunk twice");
		}
		initialized = true;
		
		// Initialize list of blocks
		
		list1d = new ArrayList<>();
		blocks = new ArrayList<>();
		
		for (int x = 0; x < numBlocks.getWidth(); x++) {
			List<List<T>> yList = new ArrayList<>();
			blocks.add(yList);
			for (int y = 0; y < numBlocks.getHeight(); y++) {
				List<T> zList = new ArrayList<>();
				yList.add(zList);
			}
		}

		// Create blocks
				
		for (int x = 0; x < numBlocks.getWidth(); x++) {
			for (int y = 0; y < numBlocks.getHeight(); y++) {
				for (int z = 0; z < numBlocks.getDepth(); z++) {
					T block = newBlock();
					block.setSize(blockSize);
					
					blocks.get(x).get(y).add(block);
					list1d.add(block);
				}
			}
		}
		
		// Sets the location of all the blocks
		setSpacing(spacing);
	}
	
	/**
	 * "De-initializes" this {@link Chunk}. Doing so allows the user to
	 * change the number of blocks on each axis. The {@link Chunk#init()}
	 * function must be called again after using this method.
	 * @see Chunk#init()
	 * @see Chunk#getNumBlocks()
	 */
	public void reset() {
		blocks = null;
		list1d = null;
		initialized = false;
	}
	
	@Override
	public void draw(Layer lay) {
//		super.display(c);
		for (T block : list1d) {
			block.draw(lay);
		}
	}
	
	protected abstract T newBlock();
	
	public void setSpacing(Dimension3 spacing) {		
		
		this.spacing.set(Dimension3.requireNonNegative(spacing));
		
		// Update total size of cube (blockSize * numBlocks + spacing * numSpaces)
		// Note: numSpaces = numBlocks - 1
		Dimension3 sizeNoSpacing = Dimension3.mult(blockSize, numBlocks);
		Dimension3 totalSpacingSize = Dimension3.mult(spacing, Dimension3.sub(numBlocks, Dimension3.ONE));
		Dimension3 totalSize = Dimension3.add(sizeNoSpacing, totalSpacingSize);
		super.setSize(totalSize);
		
		final float w = getSize().getWidth();
		final float h = getSize().getHeight();
		final float d = getSize().getDepth();
		final float bw = blockSize.getWidth();
		final float bh = blockSize.getHeight();
		final float bd = blockSize.getDepth();
		final float cx = getLoc().getX();
		final float cy = getLoc().getY();
		final float cz = getLoc().getZ();

		final float left = cx - w/2f + bw/2f;
		final float bottom = cy - h/2f + bh/2f;
		final float front = cz - d/2f + bd/2f;		
		
		for (int x = 0; x < numBlocks.getWidth(); x++) {
			for (int y = 0; y < numBlocks.getHeight(); y++) {
				for (int z = 0; z < numBlocks.getDepth(); z++) {
					Vec3.Mutable loc = new Vec3.Mutable(
						left + x * bw + x * spacing.getWidth(),
						bottom + y * bh + y * spacing.getHeight(),
						front + z * bd + z * spacing.getDepth()
					);
					
					// Take chunk's loc into account
					loc.add(getLoc());
					
					blocks.get(x).get(y).get(z).setLoc(loc);
				}
			}
		}

	}
	
	public Dimension3 getSpacing() {
		return spacing;
	}
	
	@Override
	public void setSize(Dimension3 newSize) {
		// Make sure there are no negative values
		Dimension3.requireNonNegative(newSize);
		
		// Find the quotient of newSize/size
		// and scale both blockSize and spacing by
		// that ratio.
		
		Dimension3 size = getSize();
		Dimension3 quotient = Dimension3.div(newSize, size);
		
		blockSize.mult(quotient);
		
		// Update sizes of blocks
		for (GraphicsCube block : list1d) {
			block.setSize(blockSize);
		}
		
		// Update spacing
		setSpacing(Dimension3.mult(spacing, quotient));
	}
	
	/**
	 * Change the number of {@link GraphicsCube}s on each axis.
	 * <i>This function cannot be called while {@link Chunk#isInitialized()}
	 * is true. Doing so will result in an {@link IllegalStateException}.</i>
	 * @param numBlocks the new number of {@link GraphicsCube}s on each
	 * axis.
	 */
	public void setNumBlocks(Dimension3 numBlocks) {
		if (initialized)
			throw new IllegalStateException("Cannot reset the number of blocks "
					+ "on each axis while the Chunk is initialized.");
		// Make sure no negative values
		this.numBlocks.set(Dimension3.requireNonNegative(numBlocks));
	}
	
	public Dimension3 getNumBlocks() {
		return numBlocks;
	}
	
	public List<List<List<T>>> getBlocksList3D() {
		return Collections.unmodifiableList(blocks);
	}
	
	public List<T> getBlocks() {
		return list1d;
	}
	
	public Dimension3 getBlockSize() {
		return blockSize;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
}
