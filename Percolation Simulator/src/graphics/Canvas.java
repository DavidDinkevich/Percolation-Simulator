package graphics;

import processing.event.MouseEvent;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import utils.Dimension;
import utils.Vec2;


public class Canvas extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	private Dimension size;
	
	private List<Character> keysDown;
		
	private List<Layer> layers;
		
	public Canvas(Dimension size) {
		this.size = Dimension.requireNonNegative(size);
		
		keysDown = new ArrayList<>();
		
		layers = new ArrayList<>();
	}
	
	// Compensates for difference in pixel density
	private Dimension getCorrectSize() {
		// Compensate for difference in pixel density
		final int w1 = (int) (size.getWidth() * 1.25f);
		final int h1 = (int) (size.getHeight() * 1.25f);
		return new Dimension(w1, h1);
	}
	
	@Override
	public void setup() {
		final int w = (int) size.getWidth();
		final int h = (int) size.getHeight();
		Dimension correctSize = getCorrectSize();

		setSize(w, h);
		size((int)correctSize.getWidth(), (int)correctSize.getHeight(), P2D);
				
		rectMode(CENTER);
		ellipseMode(CENTER);
		cursor(CROSS);
		frameRate(60f);
		
		// Initialize layers
		for (Layer lay : layers) {
			lay.init();
		}
	}
	
	@Override
	public void draw() {
		// Render layers
		for (Layer lay : layers) {
			lay.draw();
		}
	}
	
	@Override
	public void mousePressed() {
		for (Layer lay : layers) {
			lay.mousePressed();
		}
	}
	
	@Override
	public void keyPressed() {
		if (!keysDown.contains(key))
			keysDown.add(key);
		for (Layer lay : layers) {
			lay.keyPressed();
		}
	}
	
	@Override
	public void keyReleased() {
		keysDown.remove((Character)key);
	}
	
	@Override
	public void mouseDragged() {
//		if (!keyPressed) {
//			Vec2 from = getOldMouseLocOnGrid();
//			Vec2 to = getMouseLocOnGrid();
//			Vec2 offset = Vec2.sub(from, getTranslation());
//			Vec2 dest = Vec2.sub(to, offset);
//			setTranslation(dest);
//		}
		for (Layer lay : layers) {
			lay.mouseDragged();
		}
	}
	
	@Override
	public void mouseWheel(MouseEvent e) {
		for (Layer lay : layers) {
			lay.mouseWheel(e);
		}
	}
	
	/*
	 * Rendering convenience method
	 */
	public void layer(Layer lay) {
		if (!lay.isInitialized()) {
			throw new IllegalArgumentException("Given Layer not initialized");
		}
		image(
				// Graphics
				lay.getGraphics(),
				// Location
				lay.getLoc().getX(), lay.getLoc().getY(),
				// Size
				lay.getSize().getWidth(), lay.getSize().getHeight()
		);		
	}
		
	/**
	 * Returns the given vector as a vector starting from
	 * the translation
	 * @return the mouse location
	 */
	public Vec2 getLocOnGrid(Vec2 trans, Vec2 scale, Vec2 loc) {
		return Vec2.div(Vec2.sub(loc, trans), scale);
	}
	
	/**
	 * Returns the mouse location as a vector from
	 * the top left of the screen (0, 0)
	 * @return the mouse location
	 */
	public Vec2 getMouseLocRaw() {
		return new Vec2(mouseX, mouseY);
	}
	
	/**
	 * Get the location of the mouse as a vector
	 * from the translation.
	 * @return the mouse location
	 * @see Canvas#getTranslation()
	 */
	public Vec2 getMouseLocOnGrid(Vec2 trans, Vec2 scale) {
		return getLocOnGrid(trans, scale, getMouseLocRaw());
	}
	
	/**
	 * Returns the old mouse location as a vector from
	 * the top left of the screen (0, 0)
	 * @return the mouse location
	 */
	public Vec2 getOldMouseLocRaw() {
		return new Vec2(pmouseX, pmouseY);
	}
	
	/**
	 * Get the old location of the mouse as a vector
	 * from the translation.
	 * @return the old mouse location
	 * @see Canvas#getTranslation()
	 */
	public Vec2 getOldMouseLocOnGrid(Vec2 trans, Vec2 scale) {
		return getLocOnGrid(trans, scale, getOldMouseLocRaw());
	}
	
	public boolean containsPointRaw(Vec2 point) {
		return point.getX() >= 0f && point.getX() < width && 
				point.getY() >= 0f && point.getY() < height;
	}
	
	public float deltaTime() {
		return 1f/frameRate;
	}
	
	public boolean keysAreDown(char... keys) {
		for (char c : keys) {
			if (!keysDown.contains(c)) {
				return false;
			}
		}
		return true;
	}
	
	public List<Character> getKeysDown() {
		return keysDown;
	}
	
	public List<Layer> getLayers() {
		return layers;
	}
	
}
