package graphics;

import processing.core.PConstants;
import processing.core.PGraphics;

import utils.Dimension;
import utils.Vec2;
import utils.Vec3;

public class Layer3D extends Layer {
	
	private PGraphics graphics;
	private Vec3.Mutable translation;
	private Vec3.Mutable rotation;
	private Vec3.Mutable scale;
		
	public Layer3D(Canvas parent, Vec2 loc, Dimension size) {
		super(parent, loc, size);
		
		translation = new Vec3.Mutable();
		rotation = new Vec3.Mutable();
		scale = new Vec3.Mutable(1f, 1f, 1f);
	}
	
	@Override
	public void init() {
		super.init();

		graphics = getParentCanvas().createGraphics(
				(int) getSize().getWidth(), (int) getSize().getHeight(), 
				PConstants.P3D
		);
	}
	
	@Override
	public void beforeObjectsDrawn() {
		translate(getTranslation());
		rotate(getRotation());
		scale(getScale());
	}
	
	@Override
	public void afterObjectsDrawn() {
	}
	
	@Override
	public PGraphics getGraphics() {
		return graphics;
	}
	
	public Vec3 getTranslation() {
		return translation;
	}
	
	public void setTranslation(Vec3 trans) {
		translation.set(trans);
	}
	
	public Vec3 getRotation() {
		return rotation;
	}
	
	public void setRotation(Vec3 rot) {
		rotation.set(rot);
	}
	
	public Vec3 getScale() {
		return scale;
	}

	public void setScale(Vec3 scale) {
		this.scale.set(scale);
	}
	
}
