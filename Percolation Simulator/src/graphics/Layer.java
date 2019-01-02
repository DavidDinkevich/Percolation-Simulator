package graphics;

import java.util.List;
import java.util.ArrayList;

import processing.core.PGraphics;
import processing.event.MouseEvent;

import utils.Dimension;
import utils.Dimension3;
import utils.Utils;
import utils.Vec2;
import utils.Vec3;

public abstract class Layer {
	
	private Canvas parentCanvas;
	private Vec2.Mutable loc;
	private Dimension size;

	private int br, bg, bb;
	private float transparency;
	
	private List<Drawable> drawables;
	private Brush.Builder brush;
	private boolean initialized;
	
	private Camera cam;
	
	public Layer(Canvas parent, Vec2 loc, Dimension size) {
		parentCanvas = parent;
		this.loc = new Vec2.Mutable(loc);
		this.size = Dimension.requireNonNegative(size);
		brush = new Brush.Builder();
		drawables = new ArrayList<>();
	}
	
	public void init() {
		if (initialized) {
//			throw new IllegalStateException("Cannot initialize a Layer twice");
		}
		initialized = true;
	}
	
	public final void draw() {

		getGraphics().beginDraw();
		
		// DEFAULT SETTINGS
		
		// Center screen
		getGraphics().translate(
				getGraphics().width / 2f,
				getGraphics().height / 2f
		);
		getGraphics().scale(1f, -1f, -1f);
//		------
		
		// Background
		getGraphics().background(br, bg, bb, transparency);
		
		beforeObjectsDrawn();
		
		// Draw drawables
		if (!getDrawables().isEmpty()) {
			for (Drawable d : drawables) {
				d.draw(this);
			}
		}
		
		afterObjectsDrawn();
		
		getGraphics().endDraw();
		
		parentCanvas.layer(this);
	}
	
	/**
	 * Executed in the draw method, before the {@link Drawable}s are drawn
	 * to the {@link PGraphics}.
	 */
	protected abstract void beforeObjectsDrawn();
	
	/**
	 * Executed in the draw method, after the {@link Drawable}s are drawn
	 * to the {@link PGraphics}.
	 */
	protected abstract void afterObjectsDrawn();
	
	public void mousePressed() {
		if (cam != null)
			cam.mousePressed();
	}
	public void keyPressed() {
		if (cam != null)
			cam.keyPressed();
	}
	
	public void mouseWheel(MouseEvent e) {
		if (cam != null) {
			cam.mouseWheel(e);
		}
	}

	public void mouseDragged() {
		if (cam != null) {
			cam.mouseDragged();
		}
	}
	
	public Brush getBrush() {
		return brush;
	}
	
	private void applyBrush(PGraphics g) {
		if (brush.renderFill()) {
			g.fill(brush.getFill(), brush.getFillAlpha());
		} else {
			g.noFill();
		}
		if (brush.renderStroke()) {
			g.strokeWeight(brush.getStrokeWeight());
			g.stroke(brush.getStroke(), brush.getStrokeAlpha());
		} else {
			g.noStroke();
		}
	}
	
	public void setBrush(Brush brush) {
		this.brush.set(brush);
		applyBrush(getGraphics());
	}
	
	public abstract PGraphics getGraphics();
	
	/*
	 * CONVENIENCE RENDERING METHODS (3D)
	 */
	
//	public void background(float r, float g, float b, float a) {
//		getGraphics().background(r, g, b, a);
//	}
	
	public void box(Dimension3 size) {
		getGraphics().box(size.getWidth(), size.getHeight(), size.getDepth());
	}
	
	public void sphere(float diam) {
		getGraphics().sphere(diam);
	}
	
	public void line(Vec3 p1, Vec3 p2) {
		getGraphics().line(
				// Point 1
				p1.getX(), p1.getY(), p1.getZ(), 
				// Point 2
				p2.getX(), p2.getY(), p2.getZ()
		);
	}
	
	public void text(Vec3 loc, String text, float size) {
		getGraphics().textSize(size);
		getGraphics().text(text, loc.getX(), loc.getY(), loc.getZ());
	}
	
	public void text(Vec3 loc, String text) {
		getGraphics().text(text, loc.getX(), loc.getY(), loc.getZ());
	}
	
	public void translate(Vec3 vec) {
		getGraphics().translate(vec.getX(), vec.getY(), vec.getZ());
	}
	
	public void rotate(Vec3 r) {
		getGraphics().rotateX(r.getX());
		getGraphics().rotateY(r.getY());
		getGraphics().rotateZ(r.getZ());
	}
	
	public void scale(Vec3 scale) {
		getGraphics().scale(scale.getX(), scale.getY(), scale.getZ());
	}
	
	/*
	 * CONVENIENCE RENDERING METHODS (2D)
	 */
	
	public void ellipse(Vec2 loc, float diam) {
		getGraphics().ellipse(loc.getX(), loc.getY(), diam, diam);
	}
	
	public void ellipse(Vec2 loc, Dimension size) {
		getGraphics().
			ellipse(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
	}
	
	public void rect(Vec2 loc, Dimension size) {
		getGraphics().rect(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
	}
	
	public void text(Vec2 loc, String text, float size) {
		getGraphics().textSize(size);
		getGraphics().text(text, loc.getX(), loc.getY());
	}
	
	public void text(Vec2 loc, String text) {
		getGraphics().text(text, loc.getX(), loc.getY());
	}
	
	public void translate(Vec2 vec) {
		getGraphics().translate(vec.getX(), vec.getY());
	}
	
	public void scale(Vec2 vec) {
		getGraphics().scale(vec.getX(), vec.getY());
	}
	
	// END CONVENIENCE RENDERING METHODS (2D)

	
	public List<Drawable> getDrawables() {
		return drawables;
	}
	
	public Canvas getParentCanvas() {
		return parentCanvas;
	}
	
	public int getBackground() {
		return Brush.makeColor(br, bg, bb, transparency);
	}
	
	public void setBackground(int r, int g, int b, float a) {
		br = r;
		bg = g;
		bb = b;
		transparency = a;
	}
	
	public boolean isTransparent() {
		return transparency <= 0f;
	}
	
	public boolean isOpaque() {
		return transparency >= 255f;
	}

	public void setTransparent(boolean transparent) {
		transparency = transparent ? 0f : 255f;
	}
	
	public void setTransparency(float trans) {
		transparency = Utils.constrain(trans, 0f, 255f);
	}
	
	public Vec2 getLoc() {
		return loc;
	}

	public void setLoc(Vec2 loc) {
		this.loc.set(loc);
	}

	public Dimension getSize() {
		return size;
	}
	
	public Camera getCamera() {
		return cam;
	}
	
	public void setCamera(Camera cam) {
		this.cam = cam;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
}
