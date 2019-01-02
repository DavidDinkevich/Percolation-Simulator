package graphics;

import processing.core.PConstants;
import processing.core.PGraphics;

import utils.Dimension;
import utils.Vec2;

public class Layer2D extends Layer {
	
	private PGraphics graphics;
	
	private Vec2.Mutable translation;
	private Vec2.Mutable scale;
	private float rotation;
	
	public Layer2D(Canvas parent, Vec2 loc, Dimension size) {
		super(parent, loc, size);
		
		translation = new Vec2.Mutable();
		scale = new Vec2.Mutable(1f, 1f);
	}
	
	@Override
	public void init() {
		super.init();
		graphics = getParentCanvas().createGraphics(
				(int) getSize().getWidth(), (int) getSize().getHeight(), 
				PConstants.P2D
		);
	}
	
	@Override
	public void beforeObjectsDrawn() {
		translate(translation);
		getGraphics().rotate(rotation);
		scale(scale);
	}
	
	@Override
	public void afterObjectsDrawn() {
	}
	
	@Override
	public PGraphics getGraphics() {
		return graphics;
	}
		
	public Vec2 getTranslation() {
		return translation;
	}
	
	public void setTranslation(Vec2 translation) {
		this.translation.set(translation);
	}
	
	public Vec2 getScale() {
		return scale;
	}
	
	public void setScale(Vec2 scale) {
		this.scale.set(scale);
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
}
