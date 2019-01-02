package percolation;

import graphics.Brush;
import graphics.GraphicsCube;

import utils.Dimension3;
import utils.Vec3;

public class PercolationBlock extends GraphicsCube {
	
	public static enum BLOCK_TYPE {
		OPEN, CLOSED, WATER;
		
		public Brush getBrush() {
			switch (this) {
			case OPEN: return BRUSH_OPEN_BLOCK;
			case CLOSED: return BRUSH_CLOSED_BLOCK;
			case WATER: return BRUSH_WATER_BLOCK;
			}
			return null;
		}
	}
	
	public static final Brush BRUSH_OPEN_BLOCK;
	public static final Brush BRUSH_CLOSED_BLOCK;
	public static final Brush BRUSH_WATER_BLOCK;
	
	static {
		Brush.Builder b = new Brush.Builder();
		
		b.setStroke(Brush.BLACK).setFillAlpha(255f).setFill(Brush.WHITE);
		BRUSH_OPEN_BLOCK = b.buildBrush();
		
		b.setFillAlpha(255f).setFill(Brush.BROWN);
		BRUSH_CLOSED_BLOCK = b.buildBrush();
		
		b.setFill(Brush.BLUE);
		BRUSH_WATER_BLOCK = b.buildBrush();
	}
	
	private BLOCK_TYPE type;

	public PercolationBlock(Vec3 loc, Dimension3 size, BLOCK_TYPE type) {
		super(loc, size);
		setType(type);
	}


	public PercolationBlock(BLOCK_TYPE type) {
		this(Vec3.ZERO, Dimension3.TEN, type);
	}
	
	public BLOCK_TYPE getType() {
		return type;
	}
	
	public void setType(BLOCK_TYPE type) {
		this.type = type;
		setBrush(type.getBrush());
	}
	
}
