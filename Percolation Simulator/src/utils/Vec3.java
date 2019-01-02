package utils;

import java.io.Serializable;

/**
 * Represents a 3 dimensional mathematical vector.
 * <p>
 * NOTE: this is built off of the open source processing
 * <code>PVector</code> class, which can be found here:
 * https://github.com/processing/processing/blob/master/core/src/processing/core/PVector.java
 * 
 * @author David Dinkevich
 */
public class Vec3 implements Serializable {

	private static final long serialVersionUID = -6592765383687318346L;

	public static final Vec3 ZERO = new Vec3(0f, 0f, 0f);
	
	protected float x, y, z;
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3() {
		x = ZERO.x;
		y = ZERO.y;
		z = ZERO.z;
	}
	public Vec3(Vec3 other) {
		this(other.x, other.y, other.z);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Vec3))
			return false;
		Vec3 v = (Vec3)o;
		return x == v.x && y == v.y && z == v.z;
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + Float.floatToIntBits(x);
		result = 31 * result + Float.floatToIntBits(y);
		result = 31 * result + Float.floatToIntBits(z);
		return result;
	}
	
	@Override
	public String toString() {
		return "[ " + x + " , " + y + " , " + z + " ]";
	}

	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	
	public float getMag() {
		return (float) Math.sqrt(getMagSq());
	}
	
	public float getMagSq() {
		return (x*x + y*y + z*z);
	}
	
	public Vec3 valueAtMag(float mag) {
		return Vec3.mult(normalized(), mag);
	}
	
	/**
	 * Get the value of this {@link Vec3} at a normalized state
	 * (as a unit vector).
	 * NOTE: As this class is <i>immutable,
	 * the x, y, and z values in <i>this {@link Vec3} are
	 * not affected or changed.</i>
	 */
	public Vec3 normalized() {
		float x, y, z;
		final float m = getMag();
		
		if (m > 0) {
			x = this.x/m;
			y = this.y/m;
			z = this.z/m;
		} else {
			x = this.x;
			y = this.y;
			z = this.z;
		}
		return new Vec3(x, y, z);
	}
	
	/**
	 * Get the negated version of this {@link Vec3}.
	 * NOTE: As this class is <i>immutable,
	 * the x, y, and z values in <i>this {@link Vec3} are
	 * not affected or changed.</i>
	 * @return the negated version of this vector.
	 */
	public Vec3 negated() {
		return new Vec3(-x, -y, -z);
	}
	
	/*
	 * Static add/sub/div/mult operations
	 */
	
	public static Vec3 add(Vec3 v1, Vec3 v2) {
		return new Vec3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	public static Vec3 add(Vec3 v1, float n) {
		return new Vec3(v1.x+n, v1.y+n, v1.z+n);
	}
	
	public static Vec3 sub(Vec3 v1, Vec3 v2) {
		return new Vec3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	public static Vec3 sub(Vec3 v1, float n) {
		return new Vec3(v1.x-n, v1.y-n, v1.z-n);
	}
	
	public static Vec3 mult(Vec3 v1, Vec3 v2) {
		return new Vec3(v1.x*v2.x, v1.y*v2.y, v1.z*v2.z);
	}
	public static Vec3 mult(Vec3 v1, float n) {
		return new Vec3(v1.x*n, v1.y*n, v1.z*n);
	}
	
	public static Vec3 div(Vec3 v1, Vec3 v2) {
		return new Vec3(v1.x/v2.x, v1.y/v2.y, v1.y/v2.y);
	}
	public static Vec3 div(Vec3 v1, float n) {
		return new Vec3(v1.x/n, v1.y/n, v1.y/n);
	}
	
	/*
	 * DISTANCE
	 */
	
	/**
	 * Get the Euclidean distance between two {@link Vec3}s.
	 */
	public static float dist(Vec3 v1, Vec3 v2) {
		final float dx = v1.x - v2.x;
		final float dy = v1.y - v2.y;
		final float dz = v1.z - v2.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	/*
	 * DOT
	 */
	
	public static float dot(Vec3 v1, Vec3 v2) {
		return v1.x*v2.x + v1.y*v2.y +  v1.z*v2.z;
	}
	
	/**
	 * Get the value of this vector at the given x rotation (theta).
	 * <p>
	 * As this is an immutable class, the magnitude, y and z (not x!) components
	 * of this vector are <i>not</i> affected.
	 */
	public Vec3 getValueAtRotationX(float theta) {
		final float y1 = y * (float) Math.cos(theta) - z * (float) Math.sin(theta);
		final float z1 = y * (float) Math.sin(theta) + z * (float) Math.cos(theta);
		
		return new Vec3(x, y1, z1);
	}
	
	/**
	 * Get the value of this vector at the given y rotation (theta).
	 * <p>
	 * As this is an immutable class, the magnitude, x and z (not y!) components
	 * of this vector are <i>not</i> affected.
	 */
	public Vec3 getValueAtRotationY(float theta) {
		final float x1 = x * (float) Math.cos(theta) + z * (float) Math.sin(theta);
		final float z1 = -x * (float) Math.sin(theta) + z * (float) Math.cos(theta);
		
		return new Vec3(x1, y, z1);
	}
	
	/**
	 * Get the value of this vector at the given z rotation (theta).
	 * <p>
	 * As this is an immutable class, the magnitude, x and y (not z!) components
	 * of this vector are <i>not</i> affected.
	 */
	public Vec3 getValueAtRotationZ(float theta) {
		final float x1 = x * (float) Math.cos(theta) - y * (float) Math.sin(theta);
		final float y1 = x * (float) Math.sin(theta) + y * (float) Math.cos(theta);
		
		return new Vec3(x1, y1, z);
	}
	
	private static float lerp(float start, float stop, float amt) {
		return start + (stop-start) * amt;
	}
	
	/**
	 * Linear interpolate between two vectors by the given amount.
	 * @param start the vector to start from
	 * @param stop the vector to lerp to
	 */
	public static Vec3 lerp(Vec3 start, Vec3 stop, float amt) {
		final float x, y, z;
		x = lerp(start.x, stop.x, amt);
		y = lerp(start.y, stop.y, amt);
		z = lerp(start.z, stop.z, amt);
		return new Vec3(x, y, z);
	}
	
	/**
	 * Calculates and returns the angle (in radians) between two vectors.
	 *
	 * @param v1
	 *            the first {@link Vec3}.
	 * @param v2
	 *            the second {@link Vec3}
	 * @see https://github.com/processing/processing/blob/master/core/src/processing/core/PVector.java
	 */
	public static float angleBetween(Vec3 v1, Vec3 v2) {
//
//		// We get NaN if we pass in a zero vector which can cause problems
//		// Zero seems like a reasonable angle between a (0,0) vector and
//		// something else
//		if (v1.x == 0 && v1.y == 0)
//			return 0.0f;
//		if (v2.x == 0 && v2.y == 0)
//			return 0.0f;
//
//		final double dot = dot(v1, v2);
//		final double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y);
//		final double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y);
//		// This should be a number between -1 and 1, since it's "normalized"
//		final double amt = dot / (v1mag * v2mag);
//		// But if it's not due to rounding error, then we need to fix it
//		// http://code.google.com/p/processing/issues/detail?id=340
//		// Otherwise if outside the range, acos() will return NaN
//		// http://www.cppreference.com/wiki/c/math/acos
//		if (amt <= -1) {
//			return (float)Math.PI;
//		} else if (amt >= 1) {
//			// http://code.google.com/p/processing/issues/detail?id=435
//			return 0;
//		}
//		return (float) Math.acos(amt);
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Make a new 3D unit vector with a random direction.
	 * @return the random vector
	 */
	public static Vec3 random3D() {
//		final float angle = (float)(Math.random() * Math.PI*2);
//	    return new Vec3((float)Math.cos(angle), (float)Math.sin(angle));
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Represents a <i>mutable</i> vector. This differs from {@link Vec3}
	 * because {@link Vec3} is immutable, while this is mutable.
	 * @author David Dinkevich
	 */
	public static class Mutable extends Vec3 {

		private static final long serialVersionUID = -3587589766653246016L;

		public Mutable(float x, float y, float z) {
			super(x, y, z);
		}
		public Mutable() {
			super();
		}
		public Mutable(Vec3 v) {
			set(v);
		}
		
		/**
		 * Set the x component of this vector.
		 * @param newX the new x
		 * @return this mutable vector.
		 */
		public Mutable setX(float newX) {
			x = newX;
			return this;
		}
		
		/**
		 * Set the y component of this vector.
		 * @param newY the new y
		 * @return this mutable vector.
		 */
		public Mutable setY(float newY) {
			y = newY;
			return this;
		}
		
		/**
		 * Set the z component of this vector.
		 * @param newZ the new z
		 * @return this mutable vector.
		 */
		public Mutable setZ(float newZ) {
			z = newZ;
			return this;
		}
		
		/**
		 * Set the x, y, and z components of this vector to those
		 * of the given vector.
		 * @return this mutable vector.
		 */
		public Mutable set(Vec3 vec) {
			setX(vec.getX());
			setY(vec.getY());
			setZ(vec.getZ());
			return this;
		}
		
		/**
		 * Set the x, y, and z components of this vector.
		 * @param newX the new x
		 * @param newY the new y
		 * @param newZ the new z
		 * @return this mutable vector.
		 */
		public Mutable set(float newX, float newY, float newZ) {
			setX(newX);
			setY(newY);
			setZ(newZ);
			return this;
		}
		
		/**
		 * Normalize the vector to length 1 (make it a unit vector).
		 * As this class is <i>mutable,</i> the x, y, and z values are modified.
		 * @return this mutable vector
		 */
		public Mutable normalize() {
			final float m = getMag();
		    if (m != 0 && m != 1) {
		      div(m);
		    }
		    return this;
		}
		
		/**
		 * Negate this vector.
		 * As this class is <i>mutable,</i> the x, y, and z values are modified.
		 * @return this vector after being negated
		 */
		public Vec3 negate() {
			return set(negated());
		}
		
		/**
		 * Set the magnitude of this vector.
		 * As this class is <i>mutable,</i> the x, y, and z values are modified.
		 * @param mag the new magnitude.
		 * @return this vector with the new magnitude
		 */
		public Mutable setMag(float mag) {
			normalize();
			mult(mag);
			return this;
		}
		
		/**
		 * Linear interpolate this vector to the given vector by the given amount.
		 * As this class is <i>mutable,</i> the x, y, and z values are modified.
		 */
		public Mutable lerp(Vec3 to, float amount) {
			return set(Vec3.lerp(this, to, amount));
		}
		
		/**
		 * Returns the result of adding the given {@link Vec3}
		 * to this mutable vector. As this class is <i>mutable,</i>
		 * the x, y, and z values are modified.
		 */
		public Mutable add(Vec3 vec) {
			return set(x+vec.x, y+vec.y, z+vec.z);
		}
		
		/**
		 * Returns the result of adding this {@link Mutable} {@link Vec3}
		 * to the given float. As this class is <i>mutable,</i>
		 * the x, y, and z values are modified.
		 */
		public Mutable add(float n) {
			return set(x + n, y + n, z + n);
		}
		
		/**
		 * Returns the result of subtracting the given {@link Vec3}
		 * from this mutable vector. As this class is <i>mutable,</i>
		 * the x, y, and z values are modified.
		 */
		public Mutable sub(Vec3 vec) {
			return set(x - vec.x, y - vec.y, z - vec.z);
		}
		
		/**
		 * Returns the result of subtracting the given float
		 * from this mutable vector. As this class is <i>mutable,</i>
		 * the x, y, and z values are modified.
		 */
		public Mutable sub(float n) {
			return set(x - n, y - n, z - n);
		}
		
		/**
		 * Returns the result of multiplying this {@link Mutable} {@link Vec3}
		 * by the given {@link Vec3}. As this class is <i>mutable,</i>
		 * the x, y, and z values are modified.
		 */
		public Mutable mult(Vec3 vec) {
			return set(x * vec.x, y * vec.y, z * vec.z);
		}
		
		/**
		 * Returns the result of multiplying this {@link Mutable} {@link Vec3}
		 * by the given float. As this class is <i>mutable,</i>
		 * the x, y, and z values are modified.
		 */
		public Mutable mult(float n) {
			return set(x * n, y * n, z * n);
		}
		
		/**
		 * Returns the result of dividing this {@link Mutable} {@link Vec3}
		 * by the given {@link Vec3}. As this class is <i>mutable,</i>
		 * the x, y, and z values are modified.
		 */
		public Mutable div(Vec3 vec) {
			return set(x / vec.x, y / vec.y, z / vec.z);
		}
		
		/**
		 * Returns the result of dividing this {@link Mutable} {@link Vec3}
		 * by the given float. As this class is <i>mutable,</i>
		 * the x, y, and z values are modified.
		 */
		public Mutable div(float n) {
			return set(x / n, y / n, z / n);
		}

		/**
		 * Rotate this vector at the given x rotation
		 * <p>
		 * As this is a mutable class, the magnitude, y and z (not x!) components
		 * of this vector are affected.
		 */
		public Mutable rotateX(float theta) {
			return set(getValueAtRotationX(theta));
		}
		
		/**
		 * Rotate this vector at the given y rotation
		 * <p>
		 * As this is a mutable class, the magnitude, x and z (not y!) components
		 * of this vector are affected.
		 */
		public Mutable rotateY(float theta) {
			return set(getValueAtRotationY(theta));
		}
		
		/**
		 * Rotate this vector at the given z rotation
		 * <p>
		 * As this is a mutable class, the magnitude, x and y (not z!) components
		 * of this vector are affected.
		 */
		public Mutable rotateZ(float theta) {
			return set(getValueAtRotationZ(theta));
		}
		
	}
}