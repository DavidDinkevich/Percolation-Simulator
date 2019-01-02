package graphics;

import processing.core.PApplet;

/**
 * @author David Dinkevich
 */
public class Brush {
	public static final int BLACK, WHITE, RED, BLUE, LIGHT_BLUE, GREEN,
							YELLOW, ORANGE, PURPLE, PINK, BROWN;

	// Specifically for making colors
	private static final PApplet applet;
	
	static {
		applet = new PApplet();
		
		BLACK = 0;
		WHITE = 255;
		RED = makeColor(255f, 0f, 0f);
		BLUE = makeColor(0f, 0f, 255f);
		LIGHT_BLUE = makeColor(50f, 150f, 255f);
		GREEN = makeColor(0f, 255f, 0f);
		YELLOW = makeColor(255f, 255f, 0f);
		ORANGE = makeColor(255f, 100f, 0f);
		PURPLE = makeColor(150f, 50f, 255f);
		PINK = makeColor(255f, 0f, 255f);
		BROWN = makeColor(120f, 75f, 0f);
	}
	
	/*
	 * Making colors
	 */
	
	public static int makeColor(float r, float g, float b) {
		return applet.color(r, g, b);
	}
	
	public static int makeColor(float r, float g, float b, float a) {
		return applet.color(r, g, b, a);
	}
	
	/*
	 * Extracting individual colors
	 */
	
//	public static float getRed(int col) {
//		return applet.red(col);
//	}
//	
//	public static float getGreen(int col) {
//		return applet.green(col);
//	}
//
//	public static float getBlue(int col) {
//		return applet.blue(col);
//	}
		
	protected int fill;
	protected int stroke;
	protected float strokeWeight;
	protected float fillAlpha;
	protected float strokeAlpha;
	protected boolean renderFill;
	protected boolean renderStroke;
	
	protected Brush(Builder b) {
		fill = b.fill;
		stroke = b.stroke;
		strokeWeight = b.strokeWeight;
		fillAlpha = b.fillAlpha;
		strokeAlpha = b.strokeAlpha;
		renderFill = b.renderFill;
		renderStroke = b.renderStroke;
	}
	public Brush() {
		fill = 100;
		stroke = 100;
		strokeWeight = 1f;
		fillAlpha = 255;
		strokeAlpha = 255;
		renderFill = true;
		renderStroke = true;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Brush))
			return false;
		Brush b = (Brush)o;
		return     b.fill == fill
				&& b.stroke == stroke
				&& b.strokeWeight == strokeWeight
				&& b.fillAlpha == fillAlpha
				&& b.strokeAlpha == strokeAlpha
				&& b.renderFill == renderFill
				&& b.renderStroke == renderStroke;
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + fill;
		result = 31 * result + stroke;
		result = 31 * result + Float.floatToIntBits(strokeWeight);
		result = 31 * result + Float.floatToIntBits(fillAlpha);
		result = 31 * result + Float.floatToIntBits(strokeAlpha);
		result = 31 * result + Boolean.hashCode(renderFill);
		result = 31 * result + Boolean.hashCode(renderStroke);
		return result;
	}

	public int getFill() {
		return fill;
	}
	public boolean renderFill() {
		return renderFill;
	}
	public boolean renderStroke() {
		return renderStroke;
	}
	public int getStroke() {
		return stroke;
	}
	public float getStrokeWeight() {
		return strokeWeight;
	}
	public float getFillAlpha() {
		return fillAlpha;
	}
	public float getStrokeAlpha() {
		return strokeAlpha;
	}
	
	public static class Builder extends Brush {
		public Builder(Brush b) {
			set(b);
		}
		public Builder() {
			fillAlpha = 255; // Prevent alpha from being transparent (0) by default
			strokeAlpha = 255;
		}
		
		public Builder set(Brush b) {
			fill = b.fill;
			stroke = b.stroke;
			strokeWeight = b.strokeWeight;
			fillAlpha = b.fillAlpha;
			strokeAlpha = b.strokeAlpha;
			renderFill = b.renderFill;
			renderStroke = b.renderStroke;
			return this;
		}
		
		public Brush buildBrush() {
			return new Brush(this);
		}
		
		public Builder setFill(int fill) {
			this.fill = fill;
			return this;
		}
		public Builder setStroke(int stroke) {
			this.stroke = stroke;
			return this;
		}
		public Builder setStrokeWeight(float strokeWeight) {
			this.strokeWeight = strokeWeight;
			return this;
		}
		public Builder setFillAlpha(float alpha) {
			this.fillAlpha = alpha;
			return this;
		}
		public Builder setStrokeAlpha(float alpha) {
			this.strokeAlpha = alpha;
			return this;
		}
		public Builder setRenderFill(boolean renderFill) {
			this.renderFill = renderFill;
			return this;
		}
		public Builder setRenderStroke(boolean renderStroke) {
			this.renderStroke = renderStroke;
			return this;
		}
	}
}