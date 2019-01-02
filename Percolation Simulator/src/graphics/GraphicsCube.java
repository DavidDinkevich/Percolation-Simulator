package graphics;

import processing.core.PGraphics;

import utils.Dimension3;
import utils.Vec3;

public class GraphicsCube extends GraphicsObject3D {
	
	private Dimension3.Mutable size;
	
	public GraphicsCube(Vec3 loc, Dimension3 size, Brush brush) {
		super(loc, brush);
		this.size = Dimension3.Mutable.requireNonNegative(size);
	}

	public GraphicsCube(Vec3 loc, Dimension3 size) {
		this(loc, size, new Brush());
	}
	
	public GraphicsCube() {
		this(Vec3.ZERO, Dimension3.TEN);
	}
	
	@Override
	public void draw(Layer lay) {
		super.draw(lay);
		
		PGraphics g = lay.getGraphics();
		
		g.pushMatrix();
		lay.translate(getLoc());
		lay.rotate(getRotation());
		lay.box(size);
		g.popMatrix();
	}
	
	public Dimension3 getSize() {
		return size;
	}
	
	public void setSize(Dimension3 size) {
		this.size.set(Dimension3.requireNonNegative(size));
	}
	
}
