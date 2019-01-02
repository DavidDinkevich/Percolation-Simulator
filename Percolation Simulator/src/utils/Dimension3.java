package utils;

import java.io.Serializable;

/**
 * Embodies a 3D Dimension3, (width + height + depth). This class is immutable.
 * @author David Dinkevich
 */
public class Dimension3 implements Serializable {
	
	private static final long serialVersionUID = 7249892867940294132L;
	
	public static final Dimension3 ZERO = new Dimension3(0f);
	public static final Dimension3 ONE = new Dimension3(1f);
	public static final Dimension3 TEN = new Dimension3(10f);
	public static final Dimension3 ONE_HUNDRED = new Dimension3(100f);
	
	protected float width, height, depth;
	
	public Dimension3(float w, float h, float d) {
		width = w;
		height = h;
		depth = d;
	}
	
	public Dimension3(float size) {
		this(size, size, size);
	}
	
	public Dimension3() {
		this(0f);
	}
	
	public Dimension3(Dimension3 other) {
		this(other.width, other.height, other.depth);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o instanceof Dimension3) {
			Dimension3 other = (Dimension3) o;
			return Float.compare(other.width, width) == 0 &&
					Float.compare(other.height, height) == 0 &&
					Float.compare(other.depth, depth) == 0;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + Float.floatToIntBits(width);
		result = 31 * result + Float.floatToIntBits(height);
		result = 31 * result + Float.floatToIntBits(depth);
		return result;
	}
	
	@Override
	public String toString() {
		return "[w = " + width + ", h = " + height + ", d = " + depth + " ]";
	}
	
	/*
	 * Performing mathematical operations (static)
	 */
	
	public static Dimension3 add(Dimension3 a, Dimension3 b) {
		return new Dimension3(a.width + b.width, a.height + b.height, a.depth + b.depth);
	}
	
	public static Dimension3 add(Dimension3 a, float f) {
		return new Dimension3(a.width + f, a.height + f, a.depth + f);
	}
	
	public static Dimension3 sub(Dimension3 a, Dimension3 b) {
		return new Dimension3(a.width - b.width, a.height - b.height, a.depth - b.depth);
	}
	
	public static Dimension3 sub(Dimension3 a, float f) {
		return new Dimension3(a.width - f, a.height - f, a.depth - f);
	}
	
	public static Dimension3 mult(Dimension3 a, Dimension3 b) {
		return new Dimension3(a.width * b.width, a.height * b.height, a.depth * b.depth);
	}
	
	public static Dimension3 mult(Dimension3 a, float f) {
		return new Dimension3(a.width * f, a.height * f, a.depth * f);
	}
	
	public static Dimension3 div(Dimension3 a, Dimension3 b) {
		return new Dimension3(a.width / b.width, a.height / b.height, a.depth / b.depth);
	}
	
	public static Dimension3 div(Dimension3 a, float f) {
		return new Dimension3(a.width / f, a.height / f, a.depth / f);
	}
	
	/*
	 * Getters
	 */
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getDepth() {
		return depth;
	}
	
	/**
	 * Returns true if either the width, height, or depth of the
	 * given {@link Dimension3} is negative.
	 */
	public static boolean containsNegatives(Dimension3 d) {
		return d.getWidth() < 0f || d.getHeight() < 0f || d.getDepth() < 0f;
	}
	
	/**
	 * Returns the given {@link Dimension3} if none of its values
	 * (width, height, depth) are negative. Otherwise, an
	 * {@link IllegalArgumentException} is thrown.
	 * @param d the {@link Dimension3}
	 * @return the {@link Dimension3}
	 */
	public static Dimension3 requireNonNegative(Dimension3 d) {
		if (containsNegatives(d))
			throw new IllegalArgumentException("This Dimension3 cannot contain"
					+ " negative values.");
		return d;
	}
	
	
	public static class Mutable extends Dimension3 {

		private static final long serialVersionUID = 2356027853230892596L;

		public Mutable(float w, float h, float d) {
			super(w, h, d);
		}

		public Mutable(float size) {
			super(size);
		}
		
		public Mutable() {
		}
		
		public Mutable(Dimension3 d) {
			set(d);
		}
		
		/**
		 * Set the width of this mutable Dimension3.
		 * @param w the new width
		 * @return this mutable Dimension3
		 */
		public Mutable setWidth(float w) {
			width = w;
			return this;
		}
		
		/**
		 * Set the height of this mutable Dimension3.
		 * @param h the new height
		 * @return this mutable Dimension3
		 */
		public Mutable setHeight(float h) {
			height = h;
			return this;
		}
		
		/**
		 * Set the depth of this mutable Dimension3.
		 * @param d the new depth
		 * @return this mutable Dimension3
		 */
		public Mutable setDepth(float d) {
			depth = d;
			return this;
		}

		/**
		 * Set the components of this mutable Dimension3.
		 * @param w the new width
		 * @param h the new height
		 * @param d the new depth
		 * @return this mutable Dimension3
		 */
		public Mutable set(float w, float h, float d) {
			width = w;
			height = h;
			depth = d;
			return this;
		}
		
		/**
		 * Set the components of this mutable Dimension3.
		 * @param v the new width, height, and depth
		 * @return this mutable Dimension3
		 */
		public Mutable set(float v) {
			return set(v, v, v);
		}
		
		/**
		 * Set the width, height, and depth of this mutable Dimension3
		 * to those of the given Dimension3.
		 * @param d the new Dimension3
		 * @return this mutable Dimension3
		 */
		public Mutable set(Dimension3 d) {
			return set(d.width, d.height, d.depth);
		}
		
		/*
		 * Mathematical computation
		 */
		
		/**
		 * Returns the result of adding the given {@link Dimension3}
		 * to this mutable Dimension3.
		 */
		public Mutable add(Dimension3 other) {
			return set(width + other.width, height + other.height, 
					depth + other.depth);
		}
		
		/**
		 * Returns the result of adding the given float
		 * to this mutable Dimension3.
		 */
		public Mutable add(float n) {
			return set(width + n, height + n, depth + n);
		}
		
		/**
		 * Returns the result of subtracting the given {@link Dimension3}
		 * from this mutable Dimension3.
		 */
		public Dimension3 sub(Dimension3 other) {
			return set(width - other.width, height - other.height, 
					depth - other.depth);
		}
		
		/**
		 * Returns the result of subtracting the given float
		 * from this mutable Dimension3.
		 */
		public Dimension3 sub(float n) {
			return set(width - n, height - n, depth - n);
		}
		
		/**
		 * Returns the result of multiplying this {@link Dimension3}
		 * by the given Dimension3.
		 */
		public Dimension3 mult(Dimension3 other) {
			return set(width * other.width, height * other.height,
					depth * other.depth);
		}
		
		/**
		 * Returns the result of multiplying this {@link Dimension3}
		 * by the given float.
		 */
		public Dimension3 mult(float n) {
			return set(width * n, height * n, depth * n);
		}
		
		/**
		 * Returns the result of dividing this {@link Dimension3}
		 * by the given Dimension3.
		 */
		public Dimension3 div(Dimension3 other) {
			return set(width / other.width, height / other.height,
					depth / other.depth);
		}
		
		/**
		 * Returns the result of dividing this {@link Dimension3}
		 * by the given float.
		 */
		public Dimension3 div(float n) {
			return set(width / n, height / n, depth / n);
		}
		
		/**
		 * Returns a mutable version of the given {@link Dimension3} if
		 * none of its values (width, height, depth) are negative. Otherwise, an
		 * {@link IllegalArgumentException} is thrown.
		 * @param d the {@link Dimension3}
		 * @return the {@link Dimension3.Mutable} version of the 
		 * given {@link Dimension3}.
		 */
		public static Dimension3.Mutable requireNonNegative(Dimension3 d) {
			if (containsNegatives(d))
				throw new IllegalArgumentException("The given "
						+ "Dimension3 cannot contain negative values.");
			return new Dimension3.Mutable(d);
		}
	}
}
