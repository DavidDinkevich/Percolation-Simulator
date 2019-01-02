package utils;

import processing.core.PApplet;

public final class Utils {
	
	public static final float PI = (float) Math.PI;
	public static final float TWO_PI = (float) (Math.PI * 2D);
	
	private static final PApplet applet = new PApplet();
	
	private Utils() { throw new AssertionError(); }
	
	public static float map(float val, float min0, float max0, float min1, float max1) {
		return PApplet.map(val, min0, max0, min1, max1);
	}
	
	public static float constrain(float val, float min, float max) {
		return val < min ? min : val > max ? max : val;
	}
	
	public static float degreesToRadians(float degrees) {
		return degrees * (PI/180.0f);
	}
	
	public static float radiansToDegrees(float radians) {
		return radians * (180.0f/PI);
	}
	
	public static float random(float max) {
		return applet.random(max);
	}
	
	public static float random(float min, float max) {
		return applet.random(min, max);
	}
	
	public static int random(int max) {
		return (int)applet.random(max);
	}
	
	public static int random(int min, int max) {
		return (int)applet.random(min, max);
	}
	
	public static float dist(Vec2 p1, Vec2 p2) {
		return PApplet.dist(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	public static float round(float num, int decimalPlaces) {
		// decimalPlaces has to be > 0
		if (decimalPlaces < 0) {
			throw new IllegalArgumentException("decimalPlaces has to be > 0");
		}
		
		if (decimalPlaces > 7) {
			throw new IllegalArgumentException(decimalPlaces + " decimal "
					+ "places is too precise for a Float");
		}
		
		final float placesMultiplier = (float) Math.pow(10f, decimalPlaces);
		
		// Round to nearest hundredth
		return Math.round(num * placesMultiplier) / placesMultiplier;
		
	}
	
}
